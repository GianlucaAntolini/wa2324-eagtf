-- Check if the user has already liked the recipe
SELECT COUNT(*) AS liked
FROM User_Liked_Recipe
WHERE user_id = :user_id -- Replace :user_id with the user's ID
  AND recipe_id = :recipe_id; -- Replace :recipe_id with the recipe's ID

-- If the user has already liked the recipe, delete the entry
-- If the user hasn't liked the recipe, insert a new entry
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM User_Liked_Recipe WHERE user_id = :user_id AND recipe_id = :recipe_id) THEN
        DELETE FROM User_Liked_Recipe WHERE user_id = :user_id AND recipe_id = :recipe_id;
    ELSE
        INSERT INTO User_Liked_Recipe (user_id, recipe_id) VALUES (:user_id, :recipe_id);
    END IF;
END $$;
