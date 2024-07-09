SELECT DISTINCT r.id, COUNT(i.id)

FROM "Recipe" r
LEFT JOIN "Recipe_Ingredient" ri ON r.id = ri.recipe_id
LEFT JOIN "Ingredient" i ON ri.ingredient_id = i.id

WHERE (
	-- ingredients
	LOWER(i.name) != 'blueberries' 
	AND LOWER(i.name) != 'lemon juice'
) 
GROUP BY r.id
ORDER BY COUNT(i.id)