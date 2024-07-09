SELECT DISTINCT sq.rid, r.name, COUNT(i.id)

FROM (
	SELECT DISTINCT ssq.rid AS rid, COUNT(ssq.iid)
	FROM(
		SELECT DISTINCT r.id AS rid, i.id AS iid

		FROM "Recipe" r
		LEFT JOIN "Recipe_Ingredient" ri ON r.id = ri.recipe_id
		LEFT JOIN "Ingredient" i ON ri.ingredient_id = i.id

		WHERE (
			-- ingredients
			LOWER(i.name) = 'blueberries' 
			OR LOWER(i.name) = 'lemon juice'
		) 
	) ssq
	GROUP BY rid HAVING COUNT(ssq.iid) = 2
) sq
JOIN "Recipe" r ON sq.rid = r.id
LEFT JOIN "Recipe_Ingredient" ri ON sq.rid = ri.recipe_id
LEFT JOIN "Ingredient" i ON ri.ingredient_id = i.id
LEFT JOIN "Recipe_Tag" rt ON r.id = rt.recipe_id
LEFT JOIN "Tag" ta ON rt.tag_id = ta.id
WHERE (
	-- tags
	LOWER(ta.name) = 'easy'
)
GROUP BY sq.rid, r.name
ORDER BY COUNT(i.id)
