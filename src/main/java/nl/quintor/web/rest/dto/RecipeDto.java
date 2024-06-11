package nl.quintor.web.rest.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RecipeDto {

    @NotNull
    private String name;

    @NotNull
    private String type;

    @NotNull
    private Integer servingCapacity;

    @NotNull
    private List<IngredientDto> ingredientList = new ArrayList<>();

    @NotNull
    private String instructions;

    @NotNull
    private Boolean isVegetarian;
}
