package nl.quintor.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.quintor.model.Ingredient;
import nl.quintor.model.Recipe;
import nl.quintor.repository.IngredientRepository;
import nl.quintor.repository.RecipeRepository;
import nl.quintor.web.rest.dto.RecipeDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.HashSet;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class RecipeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        recipeRepository.deleteAll();
        ingredientRepository.deleteAll();
    }

    @Test
    public void testCreateRecipe() throws Exception {
        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setName("Test Recipe");
        recipeDto.setType("Dessert");
        recipeDto.setServingCapacity(4);
        recipeDto.setInstructions("Mix and bake.");
        recipeDto.setIsVegetarian(true);
        recipeDto.setIngredientList(Collections.emptyList());

        mockMvc.perform(post("/recipe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipeDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test Recipe"));
    }

    @Test
    public void testGetAllRecipes() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setName("Test Recipe");
        recipe.setType("Dessert");
        recipe.setServingCapacity(4);
        recipe.setInstructions("Mix and bake.");
        recipe.setIsVegetarian(true);
        recipe.setIngredientList(new HashSet<>());

        recipeRepository.save(recipe);

        mockMvc.perform(get("/recipe")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Recipe"));
    }

    @Test
    public void testSearchRecipes() throws Exception {
        Ingredient salt = new Ingredient();
        salt.setName("Salt");

        Recipe recipe = new Recipe();
        recipe.setName("Test Recipe");
        recipe.setType("Dessert");
        recipe.setServingCapacity(4);
        recipe.setInstructions("Mix and bake.");
        recipe.setIsVegetarian(true);
        recipe.setIngredientList(new HashSet<>(Collections.singletonList(salt)));

        recipeRepository.save(recipe);

        mockMvc.perform(get("/recipe/search")
                        .param("isVegetarian", "true")
                        .param("ingredientName", "Salt")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Recipe"));
    }

    @Test
    public void testUpdateRecipe() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setName("Test Recipe");
        recipe.setType("Dessert");
        recipe.setServingCapacity(4);
        recipe.setInstructions("Mix and bake.");
        recipe.setIsVegetarian(true);
        recipe.setIngredientList(new HashSet<>());

        recipe = recipeRepository.save(recipe);

        RecipeDto updatedRecipeDto = new RecipeDto();
        updatedRecipeDto.setName("Updated Recipe");
        updatedRecipeDto.setType("Dessert");
        updatedRecipeDto.setServingCapacity(4);
        updatedRecipeDto.setInstructions("Mix and bake.");
        updatedRecipeDto.setIsVegetarian(true);
        updatedRecipeDto.setIngredientList(Collections.emptyList());

        mockMvc.perform(put("/recipe/" + recipe.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedRecipeDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Recipe"));
    }

    @Test
    public void testDeleteRecipe() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setName("Test Recipe");
        recipe.setType("Dessert");
        recipe.setServingCapacity(4);
        recipe.setInstructions("Mix and bake.");
        recipe.setIsVegetarian(true);
        recipe.setIngredientList(new HashSet<>());

        recipe = recipeRepository.save(recipe);

        mockMvc.perform(delete("/recipe/" + recipe.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/recipe/" + recipe.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
