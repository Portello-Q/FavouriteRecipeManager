package nl.quintor.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.quintor.model.Recipe;
import nl.quintor.service.RecipeService;
import nl.quintor.web.rest.dto.RecipeDto;
import nl.quintor.web.rest.transformer.RecipeTransformer;
import org.hamcrest.CoreMatchers;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebAppConfiguration
@WebMvcTest(RecipeController.class)
public class RecipeControllerTest {

    @MockBean
    private RecipeService recipeService;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Captor
    private ArgumentCaptor<Recipe> recipeArgumentCaptor;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @AfterEach
    public void resetMocks() {
        reset(recipeService);
    }

    @Test
    public void createRecipeTest() throws Exception {
        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setName("Test Recipe");
        recipeDto.setType("Dessert");
        recipeDto.setServingCapacity(4);
        recipeDto.setInstructions("Mix and bake.");
        recipeDto.setIsVegetarian(true);

        Recipe recipe = RecipeTransformer.toEntity(recipeDto);
        Recipe savedRecipe = new Recipe();
        savedRecipe.setId(1L);
        savedRecipe.setName("Test Recipe");
        savedRecipe.setType("Dessert");
        savedRecipe.setServingCapacity(4);
        savedRecipe.setInstructions("Mix and bake.");
        savedRecipe.setIsVegetarian(true);

        when(recipeService.saveRecipe(any())).thenReturn(savedRecipe);

        final ObjectMapper objectMapper = new Jackson2ObjectMapperBuilder().build();
        MockHttpServletResponse response = mockMvc.perform(
                        post("/recipe")
                                .content(objectMapper.writeValueAsString(recipeDto))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus(), CoreMatchers.is(HttpStatus.CREATED.value()));
        assertThat(response.getContentAsString(), CoreMatchers.containsString("Test Recipe"));
    }

    @Test
    public void getAllRecipesTest() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipe.setName("Test Recipe");
        recipe.setType("Dessert");
        recipe.setServingCapacity(4);
        recipe.setInstructions("Mix and bake.");
        recipe.setIsVegetarian(true);

        given(recipeService.getAllRecipes()).willReturn(List.of(recipe));

        MockHttpServletResponse response = mockMvc.perform(
                        get("/recipe")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus(), CoreMatchers.is(HttpStatus.OK.value()));
        assertThat(response.getContentAsString(), CoreMatchers.containsString("Test Recipe"));
    }

    @Test
    public void getAllRecipesNoContentTest() throws Exception {
        given(recipeService.getAllRecipes()).willReturn(List.of());

        MockHttpServletResponse response = mockMvc.perform(
                        get("/recipe")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus(), CoreMatchers.is(HttpStatus.NO_CONTENT.value()));
    }

    @Test
    public void getRecipeByIdTest() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipe.setName("Test Recipe");
        recipe.setType("Dessert");
        recipe.setServingCapacity(4);
        recipe.setInstructions("Mix and bake.");
        recipe.setIsVegetarian(true);

        given(recipeService.getRecipeById(1L)).willReturn(recipe);

        MockHttpServletResponse response = mockMvc.perform(
                        get("/recipe/1")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus(), CoreMatchers.is(HttpStatus.OK.value()));
        assertThat(response.getContentAsString(), CoreMatchers.containsString("Test Recipe"));
    }

    @Test
    public void getRecipeByIdNotFoundTest() throws Exception {
        given(recipeService.getRecipeById(1L)).willReturn(null);

        MockHttpServletResponse response = mockMvc.perform(
                        get("/recipe/1")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus(), CoreMatchers.is(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    public void updateRecipeTest() throws Exception {
        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setName("Updated Recipe");
        recipeDto.setType("Main Course");
        recipeDto.setServingCapacity(2);
        recipeDto.setInstructions("Mix ingredients and cook.");
        recipeDto.setIsVegetarian(false);

        Recipe existingRecipe = new Recipe();
        existingRecipe.setId(1L);
        existingRecipe.setName("Test Recipe");

        Recipe updatedRecipe = RecipeTransformer.toEntity(recipeDto);
        updatedRecipe.setId(1L);

        given(recipeService.getRecipeById(1L)).willReturn(existingRecipe);
        given(recipeService.saveRecipe(any())).willReturn(updatedRecipe);

        final ObjectMapper objectMapper = new Jackson2ObjectMapperBuilder().build();
        MockHttpServletResponse response = mockMvc.perform(
                        put("/recipe/1")
                                .content(objectMapper.writeValueAsString(recipeDto))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus(), CoreMatchers.is(HttpStatus.OK.value()));
        assertThat(response.getContentAsString(), CoreMatchers.containsString("Updated Recipe"));
    }

    @Test
    public void updateRecipeNotFoundTest() throws Exception {
        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setName("Updated Recipe");
        recipeDto.setType("Main Course");
        recipeDto.setServingCapacity(2);
        recipeDto.setInstructions("Mix ingredients and cook.");
        recipeDto.setIsVegetarian(false);

        given(recipeService.getRecipeById(1L)).willReturn(null);

        final ObjectMapper objectMapper = new Jackson2ObjectMapperBuilder().build();
        MockHttpServletResponse response = mockMvc.perform(
                        put("/recipe/1")
                                .content(objectMapper.writeValueAsString(recipeDto))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus(), CoreMatchers.is(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    public void deleteRecipeTest() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setId(1L);

        given(recipeService.getRecipeById(1L)).willReturn(recipe);

        MockHttpServletResponse response = mockMvc.perform(
                        delete("/recipe/1")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus(), CoreMatchers.is(HttpStatus.NO_CONTENT.value()));
        verify(recipeService, times(1)).deleteRecipe(1L);
    }

    @Test
    public void deleteRecipeNotFoundTest() throws Exception {
        given(recipeService.getRecipeById(1L)).willReturn(null);

        MockHttpServletResponse response = mockMvc.perform(
                        delete("/recipe/1")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus(), CoreMatchers.is(HttpStatus.NOT_FOUND.value()));
    }
}
