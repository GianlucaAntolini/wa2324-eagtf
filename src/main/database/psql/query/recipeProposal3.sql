SELECT DISTINCT 
	sq.rid AS recipe_id, 
	sq.rn AS recipe_name, 
	sq.n AS ing_distance
FROM (
		SELECT DISTINCT r.rid, r.rn, COUNT(i.id) AS n

		FROM (
				SELECT DISTINCT r.id AS rid, r.name AS rn, COUNT(ri.ingredient_id)
				FROM "Recipe" r
				JOIN "Recipe_Ingredient" ri ON r.id = ri.recipe_id
				GROUP BY rid, rn HAVING COUNT(ri.ingredient_id) >= 7
			) r
			LEFT JOIN "Recipe_Ingredient" ri ON r.rid = ri.recipe_id
			LEFT JOIN "Ingredient" i ON ri.ingredient_id = i.id

		WHERE (
			-- ingredients
			i.name NOT IN ('blueberries', 'lemon juice', 
						   'granulated sugar', 'vanilla yogurt', 
						   'vanilla extract', 'egg', 'flour', 
						   'lemon zest', 'milk', 'salt', 'sugar', 
						   'water')
		)

		GROUP BY r.rid, r.rn
	) sq
	JOIN "Recipe_Tag" rt ON sq.rid = rt.recipe_id
	JOIN "Tag" ta ON rt.tag_id = ta.id
WHERE (
		-- tags
		ta.name IN ('Easy', 'Oven')
	)

ORDER BY sq.n
LIMIT 50