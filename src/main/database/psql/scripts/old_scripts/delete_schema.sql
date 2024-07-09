-- Disabilita temporaneamente i controlli delle chiavi esterne
SET session_replication_role = 'replica';

-- Droppa tutte le tabelle in ordine inverso di dipendenza delle chiavi esterne

DROP TABLE IF EXISTS "User_Liked_Recipe";
DROP TABLE IF EXISTS "Recipe_Ingredient";
DROP TABLE IF EXISTS "Recipe_Tag";
DROP TABLE IF EXISTS "Recipe";
DROP TABLE IF EXISTS "User";
DROP TABLE IF EXISTS "User_Role";
DROP TABLE IF EXISTS "Tag";
DROP TABLE IF EXISTS "Ingredient";
-- Riabilita i controlli delle chiavi esterne
SET session_replication_role = 'origin';

