# Setup the Example JDBC Application

## Requirements 

- **Java 8 or newer**: If you are using an older Java version, just download the right JDBC driver here: [https://jdbc.postgresql.org/download/](https://jdbc.postgresql.org/download/). Then copy the `.jar` file to `postgresql.jar` in this folder, overwriting the existing file.
- **make**: You strictly do not need make to run the example, but it will make running and compiling the application easier

## Setup

If necessary adapt the constants in `src/edu/iit/MyJDBCExample.java` to match your postgres server's connection settings. If you see an error message like this, then either your server is not running or you did set incorrect connection parameters.

~~~sh
FOR THIS PROGRAM TO WORK YOU HAVE TO HAVE A POSTGRES SERVER RUNNING LOCALLY (OR DOCKER) AT 127.0.0.1 WITH PORT 5432 AND DATABASE university AND USER postgres WITH PASSWORD test
~~~

## Compile the application

~~~sh
make all
~~~

## Run the application

~~~sh
make run
~~~

# Further reading

- [https://jdbc.postgresql.org/download/](https://jdbc.postgresql.org/download/)
- [https://docs.oracle.com/javase/tutorial/jdbc/basics/index.html](https://docs.oracle.com/javase/tutorial/jdbc/basics/index.html)
- [https://www.w3schools.blog/jdbc-tutorial](https://www.w3schools.blog/jdbc-tutorial)
- [https://www.javatpoint.com/java-jdbc](https://www.javatpoint.com/java-jdbc)
