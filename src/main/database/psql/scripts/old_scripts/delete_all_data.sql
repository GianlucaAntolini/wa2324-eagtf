-- Disable foreign key checks temporarily
SET session_replication_role = 'replica';

-- Delete data from tables in the reverse order of foreign key dependencies
DELETE FROM "User_Liked_Recipe";
DELETE FROM "Recipe_Ingredient";
DELETE FROM "Recipe_Tag";
DELETE FROM "Recipe";
DELETE FROM "User";
DELETE FROM "User_Role";
DELETE FROM "Tag";
DELETE FROM "Ingredient";


-- Re-enable foreign key checks
SET session_replication_role = 'origin';

