package nl.quintor.web.rest.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IngredientDto {
    @NotNull
    private String name;
}
