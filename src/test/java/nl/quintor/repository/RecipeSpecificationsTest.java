package nl.quintor.repository;

import nl.quintor.model.Ingredient;
import nl.quintor.model.Recipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.*;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class RecipeSpecificationsTest {

    private Root<Recipe> root;
    private CriteriaQuery<?> query;
    private CriteriaBuilder cb;
    private SetJoin<Recipe, Ingredient> ingredientsJoin;

    @BeforeEach
    void setUp() {
        root = mock(Root.class);
        query = mock(CriteriaQuery.class);
        cb = mock(CriteriaBuilder.class);
        ingredientsJoin = mock(SetJoin.class);
    }

    @Test
    void testIsVegetarian_HappyFlow() {
        Specification<Recipe> spec = RecipeSpecifications.isVegetarian(true);
        when(cb.equal(root.get("isVegetarian"), true)).thenReturn(mock(Predicate.class));

        Predicate predicate = spec.toPredicate(root, query, cb);
        assertNotNull(predicate);
    }

    @Test
    void testIsVegetarian_Null() {
        Specification<Recipe> spec = RecipeSpecifications.isVegetarian(null);
        when(cb.conjunction()).thenReturn(mock(Predicate.class));

        Predicate predicate = spec.toPredicate(root, query, cb);
        assertNotNull(predicate);
    }

    @Test
    void testServingCapacity_HappyFlow() {
        Specification<Recipe> spec = RecipeSpecifications.servingCapacity(4);
        when(cb.equal(root.get("servingCapacity"), 4)).thenReturn(mock(Predicate.class));

        Predicate predicate = spec.toPredicate(root, query, cb);
        assertNotNull(predicate);
    }

    @Test
    void testServingCapacity_Null() {
        Specification<Recipe> spec = RecipeSpecifications.servingCapacity(null);
        when(cb.conjunction()).thenReturn(mock(Predicate.class));

        Predicate predicate = spec.toPredicate(root, query, cb);
        assertNotNull(predicate);
    }

    @Test
    void testIncludeIngredients_HappyFlow() {
        Set<String> ingredients = new HashSet<>();
        ingredients.add("Salt");

        Specification<Recipe> spec = RecipeSpecifications.includeIngredients(ingredients);
        doReturn(ingredientsJoin).when(root).joinSet("ingredientList", JoinType.LEFT);
        when(ingredientsJoin.get("name")).thenReturn(mock(Path.class));
        when(ingredientsJoin.get("name").in(ingredients)).thenReturn(mock(Predicate.class));

        Predicate predicate = spec.toPredicate(root, query, cb);
        assertNotNull(predicate);
    }

    @Test
    void testIncludeIngredients_Null() {
        Specification<Recipe> spec = RecipeSpecifications.includeIngredients(null);
        when(cb.conjunction()).thenReturn(mock(Predicate.class));

        Predicate predicate = spec.toPredicate(root, query, cb);
        assertNotNull(predicate);
    }

    @Test
    void testExcludeIngredients_HappyFlow() {
        Set<String> ingredients = new HashSet<>();
        ingredients.add("Sugar");

        Specification<Recipe> spec = RecipeSpecifications.excludeIngredients(ingredients);
        doReturn(ingredientsJoin).when(root).joinSet("ingredientList", JoinType.LEFT);
        when(ingredientsJoin.get("name")).thenReturn(mock(Path.class));
        when(cb.not(ingredientsJoin.get("name").in(ingredients))).thenReturn(mock(Predicate.class));


        Predicate predicate = spec.toPredicate(root, query, cb);
        assertNotNull(predicate);
    }

    @Test
    void testExcludeIngredients_Null() {
        Specification<Recipe> spec = RecipeSpecifications.excludeIngredients(null);
        when(cb.conjunction()).thenReturn(mock(Predicate.class));

        Predicate predicate = spec.toPredicate(root, query, cb);
        assertNotNull(predicate);
    }

    @Test
    void testInstructionsContain_HappyFlow() {
        Specification<Recipe> spec = RecipeSpecifications.instructionsContain("Mix");
        when(cb.like(cb.lower(root.get("instructions")), "%mix%")).thenReturn(mock(Predicate.class));

        Predicate predicate = spec.toPredicate(root, query, cb);
        assertNotNull(predicate);
    }

    @Test
    void testInstructionsContain_Null() {
        Specification<Recipe> spec = RecipeSpecifications.instructionsContain(null);
        when(cb.conjunction()).thenReturn(mock(Predicate.class));

        Predicate predicate = spec.toPredicate(root, query, cb);
        assertNotNull(predicate);
    }

    @Test
    void testIngredientNameContains_HappyFlow() {
        Specification<Recipe> spec = RecipeSpecifications.ingredientNameContains("Salt");
        doReturn(ingredientsJoin).when(root).joinSet("ingredientList", JoinType.LEFT);
        when(cb.like(cb.lower(ingredientsJoin.get("name")), "%salt%")).thenReturn(mock(Predicate.class));

        Predicate predicate = spec.toPredicate(root, query, cb);
        assertNotNull(predicate);
    }

    @Test
    void testIngredientNameContains_Null() {
        Specification<Recipe> spec = RecipeSpecifications.ingredientNameContains(null);
        when(cb.conjunction()).thenReturn(mock(Predicate.class));

        Predicate predicate = spec.toPredicate(root, query, cb);
        assertNotNull(predicate);
    }
}
