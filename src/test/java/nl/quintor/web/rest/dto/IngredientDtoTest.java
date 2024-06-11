package nl.quintor.web.rest.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class IngredientDtoTest {

    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    public void testValidIngredientDto() {
        IngredientDto ingredientDto = new IngredientDto();
        ingredientDto.setName("Salt");

        Set<ConstraintViolation<IngredientDto>> violations = validator.validate(ingredientDto);
        assertThat(violations).isEmpty();
    }

    @Test
    public void testInvalidIngredientDto() {
        IngredientDto ingredientDto = new IngredientDto();
        ingredientDto.setName(null);

        Set<ConstraintViolation<IngredientDto>> violations = validator.validate(ingredientDto);
        assertThat(violations).isNotEmpty();
    }
}
