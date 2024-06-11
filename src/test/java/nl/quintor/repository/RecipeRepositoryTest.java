package nl.quintor.repository;

import nl.quintor.config.TestApplicationContext;
import nl.quintor.model.Ingredient;
import nl.quintor.model.Recipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestApplicationContext.class)
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class RecipeRepositoryTest {

    @Autowired
    private RecipeRepository repository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @BeforeEach
    public void beforeEach() {
        Ingredient ingredientOne = new Ingredient();
        ingredientOne.setName("Salt");

        Ingredient ingredientTwo = new Ingredient();
        ingredientTwo.setName("Pepper");

        ingredientOne = ingredientRepository.save(ingredientOne);
        ingredientTwo = ingredientRepository.save(ingredientTwo);

        Set<Ingredient> ingredients = new HashSet<>();
        ingredients.add(ingredientOne);
        ingredients.add(ingredientTwo);

        Recipe recipe1 = new Recipe();
        recipe1.setName("Recipe 1");
        recipe1.setType("Main Course");
        recipe1.setServingCapacity(4);
        recipe1.setInstructions("Mix and cook.");
        recipe1.setIsVegetarian(false);
        recipe1.setIngredientList(ingredients);
        repository.save(recipe1);

        Recipe recipe2 = new Recipe();
        recipe2.setName("Recipe 2");
        recipe2.setType("Dessert");
        recipe2.setServingCapacity(2);
        recipe2.setInstructions("Mix and bake.");
        recipe2.setIsVegetarian(true);
        recipe2.setIngredientList(ingredients);
        repository.save(recipe2);

        Recipe recipe3 = new Recipe();
        recipe3.setName("Recipe 3");
        recipe3.setType("Appetizer");
        recipe3.setServingCapacity(6);
        recipe3.setInstructions("Mix and chill.");
        recipe3.setIsVegetarian(true);
        recipe3.setIngredientList(ingredients);
        repository.save(recipe3);
    }

    @Test
    public void testGetRecipeById() {
        Recipe recipe = repository.findById(1L).orElse(null);
        assertThat(recipe, is(notNullValue()));
        assertThat(recipe.getId(), is(1L));
        assertThat(recipe.getName(), is("Recipe 1"));
        assertThat(recipe.getType(), is("Main Course"));
        assertThat(recipe.getServingCapacity(), is(4));
        assertThat(recipe.getInstructions(), is("Mix and cook."));
        assertThat(recipe.getIsVegetarian(), is(false));
        assertThat(recipe.getIngredientList().size(), is(2));
    }

    @Test
    public void testCreateRecipe() {
        Ingredient ingredientOne = new Ingredient();
        ingredientOne.setName("Sugar");

        ingredientOne = ingredientRepository.save(ingredientOne);

        Set<Ingredient> ingredients = new HashSet<>();
        ingredients.add(ingredientOne);

        Recipe recipe = new Recipe();
        recipe.setName("Recipe 4");
        recipe.setType("Snack");
        recipe.setServingCapacity(3);
        recipe.setInstructions("Mix and fry.");
        recipe.setIsVegetarian(true);
        recipe.setIngredientList(ingredients);

        Recipe newRecipe = repository.save(recipe);
        Recipe findRecipe = repository.findById(newRecipe.getId()).orElse(null);
        assertThat(newRecipe, is(notNullValue()));
        assertThat(newRecipe, is(findRecipe));

        Iterable<Recipe> recipes = repository.findAll();

        assertThat(recipes, is(notNullValue()));
        int size = 0;
        for (Object obj : recipes) {
            size++;
        }
        assertThat(size, is(4));
    }

    @Test
    public void testDeleteRecipe() {
        repository.deleteById(1L);
        Iterable<Recipe> recipes = repository.findAll();

        assertThat(recipes, is(notNullValue()));
        int size = 0;
        for (Object obj : recipes) {
            size++;
        }
        assertThat(size, is(2));
    }

    @Test
    public void testFindAllWithIngredients() {
        List<Recipe> recipes = repository.findAllWithIngredients();
        assertThat(recipes, is(notNullValue()));
        assertThat(recipes.size(), is(3));
        assertThat(recipes.get(0).getIngredientList(), is(notNullValue()));
        assertThat(recipes.get(0).getIngredientList().size(), is(2));
    }
}
