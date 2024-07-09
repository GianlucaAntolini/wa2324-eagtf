SELECT DISTINCT
sq.rid AS id,
sq.rn AS name,
sq.n AS ing_distance,
r.image_url AS image_url,
r.time_minutes AS time_minutes,
r.difficulty AS difficulty 
FROM (
SELECT DISTINCT r.rid, r.rn, COUNT(ri.ingredient_id) AS n

FROM (
SELECT DISTINCT r.id AS rid, r.name AS rn, COUNT(ri.ingredient_id)
FROM "Recipe" r
JOIN "Recipe_Ingredient" ri ON r.id = ri.recipe_id
-- dinamic ceil(n/2)

GROUP BY rid, rn HAVING COUNT(ri.ingredient_id) >= 1
) r
LEFT JOIN "Recipe_Ingredient" ri ON r.rid = ri.recipe_id

-- ingredient dinamic code


WHERE (
-- ingredients
ri.ingredient_id NOT IN (1626)
)


GROUP BY r.rid, r.rn
) sq
JOIN "Recipe_Tag" rt ON sq.rid = rt.recipe_id
JOIN "Recipe" r ON sq.rid = r.id

-- tag dinamic code



ORDER BY sq.n
LIMIT 6
;
