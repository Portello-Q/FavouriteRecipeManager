package nl.quintor.web.rest.transformer;

import nl.quintor.model.Ingredient;
import nl.quintor.web.rest.dto.IngredientDto;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class IngredientTransformerTest {

    @Test
    public void testToEntity() {
        IngredientDto ingredientDto = new IngredientDto();
        ingredientDto.setName("Salt");

        Ingredient ingredient = IngredientTransformer.toEntity(ingredientDto);

        assertThat(ingredient).isNotNull();
        assertThat(ingredient.getName()).isEqualTo("Salt");
    }

    @Test
    public void testToDto() {
        Ingredient ingredient = new Ingredient();
        ingredient.setName("Salt");

        IngredientDto ingredientDto = IngredientTransformer.toDto(ingredient);

        assertThat(ingredientDto).isNotNull();
        assertThat(ingredientDto.getName()).isEqualTo("Salt");
    }
}
