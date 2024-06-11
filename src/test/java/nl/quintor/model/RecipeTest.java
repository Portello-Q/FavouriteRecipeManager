package nl.quintor.model;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class RecipeTest {

    @Test
    public void testRecipeGettersAndSetters() {
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipe.setName("Test Recipe");
        recipe.setType("Dessert");
        recipe.setServingCapacity(4);
        recipe.setInstructions("Mix and bake.");
        recipe.setIsVegetarian(true);

        Ingredient ingredient = new Ingredient();
        ingredient.setName("Sugar");
        Set<Ingredient> ingredients = new HashSet<>();
        ingredients.add(ingredient);
        recipe.setIngredientList(ingredients);

        assertThat(recipe.getId()).isEqualTo(1L);
        assertThat(recipe.getName()).isEqualTo("Test Recipe");
        assertThat(recipe.getType()).isEqualTo("Dessert");
        assertThat(recipe.getServingCapacity()).isEqualTo(4);
        assertThat(recipe.getInstructions()).isEqualTo("Mix and bake.");
        assertThat(recipe.getIsVegetarian()).isEqualTo(true);
        assertThat(recipe.getIngredientList()).contains(ingredient);
    }
}
