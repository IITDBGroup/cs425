########################################
# start from alpine image for postgres
FROM postgres:alpine
MAINTAINER bglavic@iit.edu

########################################
# copy sql script for initialization
RUN mkdir -p /docker-entrypoint-initdb.d
ADD university_schema_postgres.sql /university_schema_postgres.sql
ADD create_db.sh /docker-entrypoint-initdb.d/create_db.sh
