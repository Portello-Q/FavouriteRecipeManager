package nl.quintor.repository;

import nl.quintor.model.Recipe;
import nl.quintor.model.Ingredient;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class RecipeRepositoryCustomImpl implements RecipeRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Set<Recipe> searchRecipes(Boolean isVegetarian, Integer servingCapacity, Set<String> includeIngredients,
                                     Set<String> excludeIngredients, String instructions, String ingredientName) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Recipe> cq = cb.createQuery(Recipe.class);
        Root<Recipe> recipeRoot = cq.from(Recipe.class);
        Join<Recipe, Ingredient> ingredientsJoin = recipeRoot.join("ingredientList", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();
        addIsVegetarianPredicate(cb, recipeRoot, predicates, isVegetarian);
        addServingCapacityPredicate(cb, recipeRoot, predicates, servingCapacity);
        addIncludeIngredientsPredicate(cb, ingredientsJoin, predicates, includeIngredients);
        addExcludeIngredientsPredicate(cb, ingredientsJoin, predicates, excludeIngredients);
        addInstructionsPredicate(cb, recipeRoot, predicates, instructions);
        addIngredientNamePredicate(cb, ingredientsJoin, predicates, ingredientName);

        cq.distinct(true).where(cb.and(predicates.toArray(new Predicate[0])));

        return new HashSet<>(entityManager.createQuery(cq).getResultList());
    }

    private void addIsVegetarianPredicate(CriteriaBuilder cb, Root<Recipe> recipeRoot, List<Predicate> predicates, Boolean isVegetarian) {
        if (isVegetarian != null) {
            predicates.add(cb.equal(recipeRoot.get("isVegetarian"), isVegetarian));
        }
    }

    private void addServingCapacityPredicate(CriteriaBuilder cb, Root<Recipe> recipeRoot, List<Predicate> predicates, Integer servingCapacity) {
        if (servingCapacity != null) {
            predicates.add(cb.equal(recipeRoot.get("servingCapacity"), servingCapacity));
        }
    }

    private void addIncludeIngredientsPredicate(CriteriaBuilder cb, Join<Recipe, Ingredient> ingredientsJoin, List<Predicate> predicates, Set<String> includeIngredients) {
        if (includeIngredients != null && !includeIngredients.isEmpty()) {
            predicates.add(ingredientsJoin.get("name").in(includeIngredients));
        }
    }

    private void addExcludeIngredientsPredicate(CriteriaBuilder cb, Join<Recipe, Ingredient> ingredientsJoin, List<Predicate> predicates, Set<String> excludeIngredients) {
        if (excludeIngredients != null && !excludeIngredients.isEmpty()) {
            predicates.add(cb.not(ingredientsJoin.get("name").in(excludeIngredients)));
        }
    }

    private void addInstructionsPredicate(CriteriaBuilder cb, Root<Recipe> recipeRoot, List<Predicate> predicates, String instructions) {
        if (instructions != null && !instructions.isEmpty()) {
            predicates.add(cb.like(cb.lower(recipeRoot.get("instructions")), "%" + instructions.toLowerCase() + "%"));
        }
    }

    private void addIngredientNamePredicate(CriteriaBuilder cb, Join<Recipe, Ingredient> ingredientsJoin, List<Predicate> predicates, String ingredientName) {
        if (ingredientName != null && !ingredientName.isEmpty()) {
            predicates.add(cb.like(cb.lower(ingredientsJoin.get("name")), "%" + ingredientName.toLowerCase() + "%"));
        }
    }
}
