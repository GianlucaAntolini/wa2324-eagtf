\COPY "User_Role" FROM '../data_source/big dataset/User_Role.csv' WITH (FORMAT CSV, DELIMITER ';', HEADER true);

\COPY "User" FROM '../data_source/big dataset/User.csv' WITH (FORMAT CSV, DELIMITER ';', HEADER true);

\COPY "Recipe" FROM '../data_source/big dataset/Recipe1.csv' WITH (FORMAT CSV, DELIMITER ';', HEADER true);
\COPY "Recipe" FROM '../data_source/big dataset/Recipe2.csv' WITH (FORMAT CSV, DELIMITER ';', HEADER true);

\COPY "Ingredient" FROM '../data_source/big dataset/Ingredient.csv' WITH (FORMAT CSV, DELIMITER ';', HEADER true);

\COPY "Recipe_Ingredient" FROM '../data_source/big dataset/Recipe_Ingredient.csv' WITH (FORMAT CSV, DELIMITER ';', HEADER true);

\COPY "Tag" FROM '../data_source/big dataset/Tag.csv' WITH (FORMAT CSV, DELIMITER ';', HEADER true);

\COPY "Recipe_Tag" FROM '../data_source/big dataset/Recipe_Tag.csv' WITH (FORMAT CSV, DELIMITER ';', HEADER true);
-- fake likes 
\COPY "User_Liked_Recipe" FROM '../data_source/big dataset/User_Liked_Recipe.csv' WITH (FORMAT CSV, DELIMITER ';', HEADER true);

SELECT setval('"Recipe_id_seq"', COALESCE((SELECT MAX(id)+1 FROM "Recipe"), 1), false);
SELECT setval('"Tag_id_seq"', COALESCE((SELECT MAX(id)+1 FROM "Tag"), 1), false);
SELECT setval('"User_id_seq"', COALESCE((SELECT MAX(id)+1 FROM "User"), 1), false);
SELECT setval('"Ingredient_id_seq"', COALESCE((SELECT MAX(id)+1 FROM "Ingredient"), 1), false);