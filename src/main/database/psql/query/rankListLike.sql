-- SELECT r.id, r.name,  r.time_minutes, r.difficulty,  COUNT(ulr.recipe_id) AS like_count
SELECT r.id, r.name, r.description, r.time_minutes, r.difficulty, r.image_url, COUNT(ulr.recipe_id) AS like_count
FROM "Recipe" r
LEFT JOIN "User_Liked_Recipe" ulr ON r.id = ulr.recipe_id
GROUP BY r.id, r.name, r.description, r.time_minutes, r.difficulty, r.image_url
ORDER BY like_count DESC;



