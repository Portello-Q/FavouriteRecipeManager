package nl.quintor.web.rest.transformer;

import nl.quintor.model.Ingredient;
import nl.quintor.web.rest.dto.IngredientDto;

public class IngredientTransformer {

    public static Ingredient toEntity(IngredientDto dto) {
        Ingredient ingredient = new Ingredient();
        //ingredient.setId(dto.getId());
        ingredient.setName(dto.getName());
        return ingredient;
    }

    public static IngredientDto toDto(Ingredient entity) {
        IngredientDto dto = new IngredientDto();
        //dto.setId(entity.getId());
        dto.setName(entity.getName());
        return dto;
    }
}
