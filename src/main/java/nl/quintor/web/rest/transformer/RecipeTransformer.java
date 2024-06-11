package nl.quintor.web.rest.transformer;

import nl.quintor.model.Recipe;
import nl.quintor.web.rest.dto.RecipeDto;

import java.util.stream.Collectors;

public class RecipeTransformer {

    public static Recipe toEntity(RecipeDto dto) {
        Recipe recipe = new Recipe();
        recipe.setName(dto.getName());
        recipe.setType(dto.getType());
        recipe.setServingCapacity(dto.getServingCapacity());
        recipe.setInstructions(dto.getInstructions());
        recipe.setIngredientList(dto.getIngredientList().stream()
                .map(IngredientTransformer::toEntity)
                .collect(Collectors.toSet()));
        recipe.setIsVegetarian(dto.getIsVegetarian());
        return recipe;
    }

    public static RecipeDto toDto(Recipe entity) {
        RecipeDto dto = new RecipeDto();
        dto.setName(entity.getName());
        dto.setType(entity.getType());
        dto.setServingCapacity(entity.getServingCapacity());
        dto.setInstructions(entity.getInstructions());
        dto.setIngredientList(entity.getIngredientList().stream()
                .map(IngredientTransformer::toDto)
                .collect(Collectors.toList()));
        dto.setIsVegetarian(entity.getIsVegetarian());
        return dto;
    }
}
