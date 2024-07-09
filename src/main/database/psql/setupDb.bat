psql -U postgres -f ./scripts/setupDb.sql
echo LOADING DATABASE SCHEMA 
psql -U admin  -d dinner_dilemma -f ./db_schemas/db_schema.sql
echo LOADING THE DATA
cd ./scripts/
psql -U admin  -d dinner_dilemma -f ./import_unix.sql 
