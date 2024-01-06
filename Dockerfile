########################################
# start from alpine image for postgres
FROM postgres:alpine
LABEL org.opencontainers.image.authors="bglavic@uic.edu"

########################################
# copy sql script for initialization
RUN mkdir -p /docker-entrypoint-initdb.d
COPY sql-and-data/ddl /ddl
COPY sql-and-data/data /data
COPY university_schema_postgres.sql /university_schema_postgres.sql
COPY create_db.sh /docker-entrypoint-initdb.d/create_db.sh
COPY setup.sh /usr/local/bin/setup.sh
ENV POSTGRES_PASSWORD=cs480
RUN /usr/local/bin/setup.sh
