package nl.quintor.service;

import nl.quintor.model.Ingredient;
import nl.quintor.model.Recipe;
import nl.quintor.repository.IngredientRepository;
import nl.quintor.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository, IngredientRepository ingredientRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
    }

    public Recipe saveRecipe(Recipe recipe) {
        Set<Ingredient> managedIngredients = new HashSet<>();
        for (Ingredient ingredient : recipe.getIngredientList()) {
            Optional<Ingredient> existingIngredient = ingredientRepository.findByName(ingredient.getName());
            if (existingIngredient.isPresent()) {
                managedIngredients.add(existingIngredient.get());
            } else {
                managedIngredients.add(ingredient);
            }
        }
        recipe.setIngredientList(managedIngredients);
        return recipeRepository.save(recipe);
    }

    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAllWithIngredients();
    }

    public Recipe getRecipeById(Long id) {
        return recipeRepository.findById(id).orElse(null);
    }

    public void deleteRecipe(Long id) {
        recipeRepository.deleteById(id);
    }

    public Set<Recipe> searchRecipes(Boolean isVegetarian, Integer servingCapacity, Set<String> includeIngredients,
                                     Set<String> excludeIngredients, String instructions, String ingredientName) {
        return recipeRepository.searchRecipes(isVegetarian, servingCapacity, includeIngredients, excludeIngredients, instructions, ingredientName);
    }
}
