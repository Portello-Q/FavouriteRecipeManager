package nl.quintor.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class IngredientTest {

    @Test
    public void testIngredientGettersAndSetters() {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(1L);
        ingredient.setName("Salt");

        assertThat(ingredient.getId()).isEqualTo(1L);
        assertThat(ingredient.getName()).isEqualTo("Salt");
    }

    @Test
    public void testIngredientWithRecipes() {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(1L);
        ingredient.setName("Salt");

        Recipe recipe1 = new Recipe();
        recipe1.setId(1L);
        recipe1.setName("Recipe 1");

        Recipe recipe2 = new Recipe();
        recipe2.setId(2L);
        recipe2.setName("Recipe 2");

        List<Recipe> recipes = new ArrayList<>();
        recipes.add(recipe1);
        recipes.add(recipe2);

        ingredient.setRecipes(recipes);

        assertThat(ingredient.getRecipes()).isNotNull();
        assertThat(ingredient.getRecipes()).hasSize(2);
        assertThat(ingredient.getRecipes()).contains(recipe1, recipe2);
    }
}
