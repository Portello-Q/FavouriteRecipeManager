package nl.quintor.web.rest.transformer;

import nl.quintor.model.Recipe;
import nl.quintor.web.rest.dto.RecipeDto;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RecipeTransformerTest {

    @Test
    public void testToDto() {
        Recipe recipe = new Recipe();
        recipe.setName("Test Recipe");
        recipe.setType("Main Course");
        recipe.setServingCapacity(4);
        recipe.setInstructions("Mix and cook.");
        recipe.setIsVegetarian(true);

        RecipeDto recipeDto = RecipeTransformer.toDto(recipe);

        assertThat(recipeDto.getName()).isEqualTo("Test Recipe");
        assertThat(recipeDto.getType()).isEqualTo("Main Course");
        assertThat(recipeDto.getServingCapacity()).isEqualTo(4);
        assertThat(recipeDto.getInstructions()).isEqualTo("Mix and cook.");
        assertThat(recipeDto.getIsVegetarian()).isTrue();
    }

    @Test
    public void testToEntity() {
        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setName("Test Recipe");
        recipeDto.setType("Main Course");
        recipeDto.setServingCapacity(4);
        recipeDto.setInstructions("Mix and cook.");
        recipeDto.setIsVegetarian(true);

        Recipe recipe = RecipeTransformer.toEntity(recipeDto);

        assertThat(recipe.getName()).isEqualTo("Test Recipe");
        assertThat(recipe.getType()).isEqualTo("Main Course");
        assertThat(recipe.getServingCapacity()).isEqualTo(4);
        assertThat(recipe.getInstructions()).isEqualTo("Mix and cook.");
        assertThat(recipe.getIsVegetarian()).isTrue();
    }
}
