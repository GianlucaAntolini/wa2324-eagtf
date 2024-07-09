-- DELETE A DB ACCORDING TO THESE SPECIFICATION 
-- Nome db: dinner_dilemma
-- username: admin
-- password: admin

-- Revoke all privileges from the user
REVOKE ALL PRIVILEGES ON DATABASE dinner_dilemma FROM admin;

-- Disconnect all other users from the database
SELECT pg_terminate_backend(pid) FROM pg_stat_activity WHERE datname = 'dinner_dilemma' AND pid <> pg_backend_pid();

-- Drop the database
DROP DATABASE IF EXISTS dinner_dilemma;

-- Drop the user
DROP USER IF EXISTS admin;
