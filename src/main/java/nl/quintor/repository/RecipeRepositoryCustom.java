package nl.quintor.repository;

import nl.quintor.model.Recipe;

import java.util.Set;

public interface RecipeRepositoryCustom {
    Set<Recipe> searchRecipes(Boolean isVegetarian, Integer servingCapacity, Set<String> includeIngredients,
                              Set<String> excludeIngredients, String instructions, String ingredientName);
}
