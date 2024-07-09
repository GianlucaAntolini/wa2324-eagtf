CREATE TABLE "Ingredient"(
    "id" SERIAL NOT NULL,
    "name" VARCHAR(255) NOT NULL
);
ALTER TABLE
    "Ingredient" ADD PRIMARY KEY("id");

CREATE TYPE difficulty_enum AS ENUM ('easy', 'medium', 'hard');

CREATE TABLE "Recipe"(
    "id" SERIAL NOT NULL,
    "name" VARCHAR(255) NOT NULL,
    "description" TEXT NOT NULL,
    "time_minutes" INTEGER NOT NULL,
    "difficulty" difficulty_enum NOT NULL,
    "image_url" VARCHAR(1000) NOT NULL,
    "creation_date" DATE DEFAULT '1970-01-01' NOT NULL,
    "user_id" INTEGER NOT NULL,
    "approved" BOOLEAN NULL
);

ALTER TABLE
    "Recipe" ADD PRIMARY KEY("id");
CREATE TABLE "User_Liked_Recipe"(
    "user_id" INTEGER NOT NULL,
    "recipe_id" INTEGER NOT NULL
);
ALTER TABLE
    "User_Liked_Recipe" ADD PRIMARY KEY("user_id", "recipe_id");
CREATE TABLE "User_Role"(
    "id" SMALLINT NOT NULL,
    "description" VARCHAR(255) NOT NULL
);
ALTER TABLE
    "User_Role" ADD PRIMARY KEY("id");
CREATE TABLE "Recipe_Ingredient"(
    "recipe_id" INTEGER NOT NULL,
    "ingredient_id" INTEGER NOT NULL
);
ALTER TABLE
    "Recipe_Ingredient" ADD PRIMARY KEY("recipe_id", "ingredient_id");
CREATE TABLE "User"(
    "id" SERIAL NOT NULL,
    "email" VARCHAR(255) NOT NULL,
    "password" VARCHAR(255) NOT NULL,
    "name" VARCHAR(255) NOT NULL,
    "surname" VARCHAR(255) NOT NULL,
    "role_id" SMALLINT NOT NULL,
    "registration_date" DATE NOT NULL
);
CREATE TABLE "Tag"(
    "id" SERIAL NOT NULL,
    "name" VARCHAR(255) NOT NULL
);
ALTER TABLE
    "Tag" ADD PRIMARY KEY("id");
ALTER TABLE
    "User" ADD PRIMARY KEY("id");
CREATE TABLE "Recipe_Tag"(
    "recipe_id" INTEGER NOT NULL,
    "tag_id" INTEGER NOT NULL
);
ALTER TABLE
    "Recipe_Tag" ADD PRIMARY KEY("recipe_id", "tag_id");
ALTER TABLE
    "Recipe_Tag" ADD CONSTRAINT "recipe_tag_recipe_id_foreign" FOREIGN KEY("recipe_id") REFERENCES "Recipe"("id");
ALTER TABLE
    "Recipe_Tag" ADD CONSTRAINT "recipe_tag_tag_id_foreign" FOREIGN KEY("tag_id") REFERENCES "Tag"("id");
ALTER TABLE
    "User_Liked_Recipe" ADD CONSTRAINT "user_liked_recipe_user_id_foreign" FOREIGN KEY("user_id") REFERENCES "User"("id");
ALTER TABLE
    "User" ADD CONSTRAINT "user_role_id_foreign" FOREIGN KEY("role_id") REFERENCES "User_Role"("id");
ALTER TABLE
    "Recipe_Ingredient" ADD CONSTRAINT "recipe_ingredient_ingredient_id_foreign" FOREIGN KEY("ingredient_id") REFERENCES "Ingredient"("id");
ALTER TABLE
    "Recipe" ADD CONSTRAINT "recipe_user_id_foreign" FOREIGN KEY("user_id") REFERENCES "User"("id");
ALTER TABLE
    "User_Liked_Recipe" ADD CONSTRAINT "user_liked_recipe_recipe_id_foreign" FOREIGN KEY("recipe_id") REFERENCES "Recipe"("id");
ALTER TABLE
    "Recipe_Ingredient" ADD CONSTRAINT "recipe_ingredient_recipe_id_foreign" FOREIGN KEY("recipe_id") REFERENCES "Recipe"("id");