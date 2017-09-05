# Overview

This repository stores some example SQL scripts and a Dockerfile for setting up a postgres container with the university example database for CS425 at IIT. Some relevant Postgres and Docker links are shown at the bottom of this page. Furthermore, this repository stores jupyter notebooks ([http://jupyter.org/](http://jupyter.org/)) that are developed during class, HTML pages derived from these notebooks, and instructions for how to use Docker to run SQL based notebooks locally. 

# Using the docker images

The image for CS425 [https://hub.docker.com/r/iitdbgroup/cs425/](https://hub.docker.com/r/iitdbgroup/cs425/) is based on the official postgres alpine image [https://hub.docker.com/_/postgres/](https://hub.docker.com/_/postgres/) which is based on the `alpine` linux image [https://hub.docker.com/_/alpine/](https://hub.docker.com/_/alpine/)

To start a postgres server create a container from the image. You can either let Postgres store the data inside the container (no mess, but once the container is removed all changes to database content are lost) or mount a folder from your machine inside the container in which case Postgres will store the database content in this folder. The second method has the advantage that the database will survive the removal of the container and in the future new containers using this database can be created.

## Starting a container that stores the DB inside the container

To start a container with a new database run the following command. Here `--name` assigns a name to the created container, `-e POSTGRES_PASSWORD=test` sets the environment variable `POSTGRES_PASSWD` to value `test` inside the container. The postgres container uses this variable to setup a `postgres` database user with this password. Option `-p 5432:5432` exposes port **5432** from the container as port **5432**. This port is the standard network port of the postgres database server, i.e., clients will connect to the server using this port. Option `-d` puts the container into daemon mode, i.e., it will run in the background.

~~~
docker run --name mypostgres -e POSTGRES_PASSWORD=test -p 5432:5432 -d iitdbgroup/cs425
~~~

## Start a container using a DB that is stored on your local machine

Create a directory on your local machine were you want to store the database. Here I am creating the directory `/mypost_data`, but you probably want to rather create a directory inside your home directory.

~~~
mkdir /mypost_data
~~~

Now start a container mounting this directory as a volume. At the first start the container will initialize this directory as what Postgres calls a cluster. A cluster is a directory managed by the Postgres server for storing database content. Additionally, this directory also stores configuration files for the server.

~~~
docker run --name mypostgres -e POSTGRES_PASSWORD=test -v /mypost_data:/var/lib/postgresql/data -p 5432:5432 -d iitdbgroup/cs425
~~~

The `-v` parameter mounts local directory `/mypost_data` as directory `/var/lib/postgresql/data` in the container. Directory `/var/lib/postgresql/data` is the location which the `iitdbgroup/cs425` image uses to store the postgres cluster directory.


# The University schema

The image loads the university schema from the testbook into database `cs425` using this SQL script: [https://github.com/IITDBGroup/cs425/blob/master/university_schema_postgres.sql](https://github.com/IITDBGroup/cs425/blob/master/university_schema_postgres.sql)

# SQL Clients #

To run SQL commands on the postgres server you need a client application that connects to the server and allows you to write SQL code that is send to the server.

## Starting psql (postgres commandline client) from within the container

**psql** is the standard commandline client of Postgres. If you have created a container `mypostgres` then to run the `psql` client within the container run

~~~
docker exec -ti mypostgres psql -U postgres -d cs425
~~~

This will start the **psql** client and connect to a database `cs425` (storing the textbook university schema) using user `postgres`. Within `psql` you can now run SQL commands or control commands (starting with `\`). For instance `\q` quits the client. See below for an example session.

~~~
$docker exec -ti mypostgres psql -U postgres -d cs425
psql (9.6.3)
Type "help" for help.

cs425=# SELECT * FROM department;
 dept_name  | building |  budget
------------+----------+-----------
 Biology    | Watson   |  90000.00
 Comp. Sci. | Taylor   | 100000.00
 Elec. Eng. | Taylor   |  85000.00
 Finance    | Painter  | 120000.00
 History    | Painter  |  50000.00
 Music      | Packard  |  80000.00
 Physics    | Watson   |  70000.00
(7 rows)

cs425=# \q
$
~~~

Alternatively, you can spawn a new container to run **psql** and connect this container to your the container running the postgres server:

~~~
docker run -it --rm --link mypostgres:postgres iitdbgroup/cs425 psql -h postgres -U postgres -d cs425
~~~

This time **psql** will ask your for the postgres user's password. This is the one you setup when creating the container (`POSTGRES_PASSWORD`).


## pgAdmin

A GUI client for administrating a postgres database. Also useful for editing SQL code:

* [https://www.pgadmin.org/download/](https://www.pgadmin.org/download/)

## Third party clients

For instance, **SQuirreL SQL** ([http://squirrel-sql.sourceforge.net/](http://squirrel-sql.sourceforge.net/)) is a popular 3rd party client written in Java that supports multiple DBMS, **pgcli** ([https://www.pgcli.com/](https://www.pgcli.com/)) is an enhanced version of *psql* written in python.

# Interactive SQL notebooks with Jupyter

Some example notebooks are in the repository under `example-notebook`. The class notebooks will be stored under `classnotebook-2017-Fall`. Jupyter notebooks are interactive python environments that the user accesses through a web interface that allow code and documentation to be interleaved. Sessions can be stored as `.ipynb` files. You can use a docker image `iitdbgroup/sql-notebook` that we provide to run a jupyter notebook server that is available through a browser on your local machine at [http://127.0.0.1:8888/tree?](http://127.0.0.1:8888/tree?). Run the following command from the folder containing the notebook files (`.ipynb`) or any parent folder of this folder. This will create a docker virtual network called `mynbnetwork` and create two containers - one running postgres (`notebpostgres`) and a second one running the notebook server (`mynotebook`). For convenience you can use the scripts `startNotebook.sh` and `stopNotebook.sh`.

~~~
docker run --user root --rm --name=mynotebook -v "$(pwd)":/home/jovyan/ -p 0.0.0.0:8888:8888/tcp --link  mypostgres:postgres -d iitdbgroup/sql-notebook start-notebook.sh --NotebookApp.token=''
~~~

For information about jupyter and cell magic see [http://jupyter.org/](http://jupyter.org/). The particular cell magic used here for running sql is described [here](https://github.com/catherinedevlin/ipython-sql). 

# Postgres Links

* Postgres official webpage: [https://www.postgresql.org/](https://www.postgresql.org/)

## Documentation

* Online Postgres documentation: [https://www.postgresql.org/docs/9.6/static/index.html](https://www.postgresql.org/docs/9.6/static/index.html)

* Postgres docu as a PDF: [https://www.postgresql.org/files/documentation/pdf/9.6/postgresql-9.6-US.pdf](https://www.postgresql.org/files/documentation/pdf/9.6/postgresql-9.6-US.pdf)

## Programming Language APIs

How to connect to postgres programmatically from various languages. There is a list of interfaces at [https://www.postgresql.org/download/products/2-drivers-and-interfaces/](https://www.postgresql.org/download/products/2-drivers-and-interfaces/). Below are the direct links for common languages.

* **Java**: [https://jdbc.postgresql.org/](https://jdbc.postgresql.org/)
* **C**: **libpq** is part of Postgres
* **C++**: [http://pqxx.org/development/libpqxx/](http://pqxx.org/development/libpqxx/)
* **ODBC**: [https://www.postgresql.org/ftp/odbc/versions/](https://www.postgresql.org/ftp/odbc/versions/)
* **Python**: [http://www.psycopg.org/psycopg/](http://www.psycopg.org/psycopg/)


# Docker Links

Docker is a virtualization system that enables images (think of them as virtual machine images) to be run as containers (think of them as VMs, but more lightweight).

* **Docker**: [https://www.docker.com/](https://www.docker.com/)

* **Docker Hub** ([https://hub.docker.com/](https://hub.docker.com/))is a public repository of images. Per default `docker pull` will pull images from this repository.

* **Docker Tutorial**: [https://docs.docker.com/get-started/](https://docs.docker.com/get-started/)
