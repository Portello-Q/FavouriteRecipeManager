package nl.quintor.service;

import nl.quintor.model.Recipe;
import nl.quintor.repository.RecipeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecipeServiceTest {

    @Mock
    private RecipeRepository recipeRepositoryMock;

    @InjectMocks
    private RecipeService recipeService;

    @Test
    public void testSaveRecipe() {
        Recipe recipe = new Recipe();
        recipe.setName("Test Recipe");

        when(recipeRepositoryMock.save(recipe)).thenReturn(recipe);

        Recipe savedRecipe = recipeService.saveRecipe(recipe);

        assertThat(savedRecipe).isNotNull();
        assertThat(savedRecipe.getName()).isEqualTo("Test Recipe");
        verify(recipeRepositoryMock).save(recipe);
    }

    @Test
    public void testGetAllRecipes() {
        List<Recipe> expectedRecipes = new ArrayList<>();
        expectedRecipes.add(new Recipe());
        when(recipeRepositoryMock.findAllWithIngredients()).thenReturn(expectedRecipes);

        List<Recipe> allRecipes = recipeService.getAllRecipes();

        verify(recipeRepositoryMock).findAllWithIngredients();
        assertThat(allRecipes).hasSize(expectedRecipes.size()).isEqualTo(expectedRecipes);
    }

    @Test
    public void testGetRecipeById_Found() {
        Long recipeId = 1L;
        Recipe recipe = new Recipe();
        when(recipeRepositoryMock.findById(recipeId)).thenReturn(Optional.of(recipe));

        Recipe foundRecipe = recipeService.getRecipeById(recipeId);

        verify(recipeRepositoryMock).findById(recipeId);
        assertThat(foundRecipe).isNotNull();
        assertThat(foundRecipe).isSameAs(recipe);
    }

    @Test
    public void testGetRecipeById_NotFound() {
        Long recipeId = 1L;
        when(recipeRepositoryMock.findById(recipeId)).thenReturn(Optional.empty());

        Recipe foundRecipe = recipeService.getRecipeById(recipeId);

        verify(recipeRepositoryMock).findById(recipeId);
        assertThat(foundRecipe).isNull();
    }

    @Test
    public void testDeleteRecipe() {
        Long recipeId = 1L;
        doNothing().when(recipeRepositoryMock).deleteById(recipeId);

        recipeService.deleteRecipe(recipeId);

        verify(recipeRepositoryMock).deleteById(recipeId);
    }

    @Test
    public void testSaveRecipe_Failure() {
        Recipe recipe = new Recipe();
        recipe.setName("Test Recipe");

        when(recipeRepositoryMock.save(recipe)).thenThrow(new RuntimeException("Save failed"));

        Exception exception = assertThrows(RuntimeException.class, () -> recipeService.saveRecipe(recipe));

        assertThat(exception.getMessage()).contains("Save failed");
        verify(recipeRepositoryMock).save(recipe);
    }

    @Test
    public void testDeleteNonExistingRecipe() {
        Long recipeId = 1L;
        doThrow(new RuntimeException("Delete failed")).when(recipeRepositoryMock).deleteById(recipeId);

        Exception exception = assertThrows(RuntimeException.class, () -> recipeService.deleteRecipe(recipeId));

        assertThat(exception.getMessage()).contains("Delete failed");
        verify(recipeRepositoryMock).deleteById(recipeId);
    }
}
