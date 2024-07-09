 -- GetRecipesFromIngTagDinQuery"
 SELECT DISTINCT  r.id, r.name,  r.time_minutes , r.difficulty, r.creation_date , r.user_id   , r.approved, COUNT(ri.recipe_id) as n_ing
   FROM "Recipe" r
   LEFT JOIN "Recipe_Tag" rt ON r.id = rt.recipe_id
   LEFT JOIN "Tag" ta ON rt.tag_id = ta.id
   LEFT JOIN "Recipe_Ingredient" ri ON r.id = ri.recipe_id

   WHERE r.id NOT IN (
           -- list of recipes that also uses other ingredients
           SELECT DISTINCT r.id
   FROM "Recipe" r
           JOIN "Recipe_Ingredient" ri ON r.id = ri.recipe_id
           JOIN "Ingredient" i ON ri.ingredient_id = i.id

     WHERE

       TRUE
       AND
       (
                   -- ingredients
       i.id NOT IN ( 1530, 2560, 2720, 3106, 3616, 3638, 3974, 4877, 5195, 5539, 5547, 5661)
        )
     )
 AND
 (
             -- tags
            ta.id  IN ( 88, 187 )
          )


GROUP BY r.id, r.name
ORDER BY 1
LIMIT 15
;
