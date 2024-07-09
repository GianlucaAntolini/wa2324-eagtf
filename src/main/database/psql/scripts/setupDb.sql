

-- CREATE A DB ACCORDING TO THESE SPECIFICATION 
-- Nome db: dinner_dilemma
-- username: admin
-- password: admin


-- meant to be used with psql 
-- psql -U postgres -d postgres  -f create_db_script.sql
-- where postgres sql is the user with the admin privileges 

-- Create the database
CREATE DATABASE dinner_dilemma;

-- Connect to the newly created database
\c dinner_dilemma;

-- Create a new user
CREATE USER admin WITH PASSWORD 'admin';

-- Grant all privileges on the database to the user
GRANT ALL PRIVILEGES ON DATABASE dinner_dilemma TO admin;
-- Grant all privileges on the schema to the user
GRANT ALL PRIVILEGES ON SCHEMA public TO admin;

-- Exit the database connection
\q
