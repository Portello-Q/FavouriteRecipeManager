package nl.quintor.web.rest.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class RecipeDtoTest {

    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    public void testValidRecipeDto() {
        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setName("Test Recipe");
        recipeDto.setType("Dessert");
        recipeDto.setServingCapacity(4);
        recipeDto.setInstructions("Mix and bake.");
        recipeDto.setIsVegetarian(true);

        IngredientDto ingredientDto = new IngredientDto();
        ingredientDto.setName("Sugar");
        recipeDto.getIngredientList().add(ingredientDto);

        Set<ConstraintViolation<RecipeDto>> violations = validator.validate(recipeDto);
        assertThat(violations).isEmpty();
    }

    @Test
    public void testInvalidRecipeDto() {
        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setName(null);
        recipeDto.setType(null);
        recipeDto.setServingCapacity(null);
        recipeDto.setInstructions(null);
        recipeDto.setIsVegetarian(null);

        Set<ConstraintViolation<RecipeDto>> violations = validator.validate(recipeDto);
        assertThat(violations).isNotEmpty();
    }
}
