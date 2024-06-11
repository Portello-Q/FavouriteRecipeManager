package nl.quintor.repository;

import nl.quintor.config.TestApplicationContext;
import nl.quintor.model.Ingredient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestApplicationContext.class)
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class IngredientRepositoryTest {

    @Autowired
    private IngredientRepository repository;

    @BeforeEach
    public void beforeEach() {
        Ingredient ingredientOne = new Ingredient();
        ingredientOne.setName("Salt");

        Ingredient ingredientTwo = new Ingredient();
        ingredientTwo.setName("Pepper");

        repository.save(ingredientOne);
        repository.save(ingredientTwo);
    }

    @Test
    public void testFindByName() {
        Optional<Ingredient> ingredient = repository.findByName("Salt");
        assertThat(ingredient.isPresent(), is(true));
        assertThat(ingredient.get().getName(), is("Salt"));
    }

    @Test
    public void testFindByName_NotFound() {
        Optional<Ingredient> ingredient = repository.findByName("Sugar");
        assertThat(ingredient.isPresent(), is(false));
    }

    @Test
    public void testSaveIngredient() {
        Ingredient ingredient = new Ingredient();
        ingredient.setName("Sugar");

        Ingredient savedIngredient = repository.save(ingredient);
        assertThat(savedIngredient, is(notNullValue()));
        assertThat(savedIngredient.getName(), is("Sugar"));
    }

    @Test
    public void testDeleteIngredient() {
        Ingredient ingredient = new Ingredient();
        ingredient.setName("Sugar");

        ingredient = repository.save(ingredient);
        Long ingredientId = ingredient.getId();

        repository.deleteById(ingredientId);
        Optional<Ingredient> deletedIngredient = repository.findById(ingredientId);

        assertThat(deletedIngredient.isPresent(), is(false));
    }
}
