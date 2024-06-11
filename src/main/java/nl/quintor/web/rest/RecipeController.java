package nl.quintor.web.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import nl.quintor.model.Recipe;
import nl.quintor.service.RecipeService;
import nl.quintor.web.rest.dto.RecipeDto;
import nl.quintor.web.rest.transformer.RecipeTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/recipe")
@Slf4j
public class RecipeController {

    private final RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a new recipe")
    @ApiResponse(responseCode = "201", description = "Recipe created successfully")
    public ResponseEntity<RecipeDto> createRecipe(@RequestBody @Valid RecipeDto recipeDto) {
        log.trace("Processing add new recipe request");
        Recipe recipe = RecipeTransformer.toEntity(recipeDto);
        Recipe savedRecipe = recipeService.saveRecipe(recipe);
        RecipeDto savedRecipeDto = RecipeTransformer.toDto(savedRecipe);
        return ResponseEntity.status(201).body(savedRecipeDto);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get all recipes")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Recipes retrieved successfully"),
            @ApiResponse(responseCode = "204", description = "No recipes found")
    })
    public ResponseEntity<List<RecipeDto>> getAllRecipes() {
        log.trace("Processing get all recipes request");
        List<Recipe> recipes = recipeService.getAllRecipes();
        if (recipes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<RecipeDto> recipeDtos = recipes.stream()
                .map(RecipeTransformer::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(recipeDtos);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get a recipe by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Recipe retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Recipe not found")
    })
    public ResponseEntity<RecipeDto> getRecipeById(@PathVariable Long id) {
        log.trace("Processing get recipe by ID request");
        Recipe recipe = recipeService.getRecipeById(id);
        if (recipe == null) {
            return ResponseEntity.notFound().build();
        }
        RecipeDto recipeDto = RecipeTransformer.toDto(recipe);
        return ResponseEntity.ok(recipeDto);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update an existing recipe")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Recipe updated successfully"),
            @ApiResponse(responseCode = "404", description = "Recipe not found")
    })
    public ResponseEntity<RecipeDto> updateRecipe(@PathVariable Long id, @RequestBody @Valid RecipeDto recipeDto) {
        log.trace("Processing update recipe request");
        Recipe existingRecipe = recipeService.getRecipeById(id);
        System.out.println("recipe value: " + existingRecipe);
        if (existingRecipe == null) {
            return ResponseEntity.notFound().build();
        }
        Recipe updatedRecipe = RecipeTransformer.toEntity(recipeDto);
        updatedRecipe.setId(id); // Ensure the ID is set to update the correct entity

        Recipe savedRecipe = recipeService.saveRecipe(updatedRecipe);
        RecipeDto savedRecipeDto = RecipeTransformer.toDto(savedRecipe);
        return ResponseEntity.ok(savedRecipeDto);
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Delete a recipe by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Recipe deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Recipe not found")
    })
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id) {
        log.trace("Processing delete recipe request");
        Recipe recipe = recipeService.getRecipeById(id);
        if (recipe == null) {
            return ResponseEntity.notFound().build();
        }
        recipeService.deleteRecipe(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Search for recipes", description = "Search for recipes based on various criteria. Returns recipes that match all provided criteria.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Recipes retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No recipes found matching the criteria")
    })
    public ResponseEntity<Set<RecipeDto>> searchRecipes(
            @RequestParam(required = false)
            @Parameter(description = "Filter recipes based on whether they are vegetarian. If null, this criterion is ignored.") Boolean isVegetarian,
            @RequestParam(required = false)
            @Parameter(description = "Filter recipes by exact match for serving capacity. If null, this criterion is ignored.") Integer servingCapacity,
            @RequestParam(required = false)
            @Parameter(description = "Filter recipes that include certain ingredients. If null, this criterion is ignored.") Set<String> includeIngredients,
            @RequestParam(required = false)
            @Parameter(description = "Filter recipes that exclude certain ingredients. If null, this criterion is ignored.") Set<String> excludeIngredients,
            @RequestParam(required = false)
            @Parameter(description = "Filter recipes containing specific text in their instructions. If null, this criterion is ignored.") String instructions,
            @RequestParam(required = false)
            @Parameter(description = "Filter recipes containing a specific ingredient name. If null, this criterion is ignored.") String ingredientName) {
        log.trace("Processing search recipes request");
        Set<Recipe> recipes = recipeService.searchRecipes(isVegetarian, servingCapacity, includeIngredients, excludeIngredients, instructions, ingredientName);
        if (recipes.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Set<RecipeDto> recipeDtos = recipes.stream()
                .map(RecipeTransformer::toDto)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(recipeDtos);
    }
}
