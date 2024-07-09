SELECT r.id, r.name, r.description, r.time_minutes, r.difficulty, r.image_url
FROM "Recipe" r
JOIN "User_Liked_Recipe" ulr ON r.id = ulr.recipe_id
WHERE ulr.user_id = :user_id; -- Sostituisci :user_id con l'ID dell'utente di cui desideri conoscere i like
