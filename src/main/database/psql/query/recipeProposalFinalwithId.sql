SELECT DISTINCT 
	sq.rid AS recipe_id, 
	sq.rn AS recipe_name, 
	sq.n AS ing_distance
FROM (
		SELECT DISTINCT r.rid, r.rn, COUNT(ri.ingredient_id) AS n

		FROM (
				SELECT DISTINCT r.id AS rid, r.name AS rn, COUNT(ri.ingredient_id)
				FROM "Recipe" r
				JOIN "Recipe_Ingredient" ri ON r.id = ri.recipe_id

        -- needs to be ceil(n/2)

				GROUP BY rid, rn HAVING COUNT(ri.ingredient_id) >= 6
			) r
			LEFT JOIN "Recipe_Ingredient" ri ON r.rid = ri.recipe_id

		WHERE ( -- ingredients
			ri.ingredient_id NOT IN ( 1530, 2560, 2720, 3106, 3616, 3638, 3974, 4877, 5195, 5539, 5547, 5661)
		)
		GROUP BY r.rid, r.rn
	) sq
	JOIN "Recipe_Tag" rt ON sq.rid = rt.recipe_id
  WHERE (
		-- tags
		rt.tag_id IN ( 88, 187 )
	)

ORDER BY sq.n
LIMIT 15
