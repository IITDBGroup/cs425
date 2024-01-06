#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
    CREATE USER cs480;
    CREATE DATABASE cs480;
    GRANT ALL PRIVILEGES ON DATABASE cs480 TO cs480;
    CREATE DATABASE tpch;
    GRANT ALL PRIVILEGES ON DATABASE tpch TO cs480;
EOSQL

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" -d cs480 -f /university_schema_postgres.sql
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" -d tpch -f /ddl/ddl_tpch_0_1.sql
