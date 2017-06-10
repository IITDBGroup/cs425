#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
    CREATE USER cs425;
    CREATE DATABASE cs425;
    GRANT ALL PRIVILEGES ON DATABASE cs425 TO cs425;
EOSQL

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" -d cs425 -f /university_schema_postgres.sql
