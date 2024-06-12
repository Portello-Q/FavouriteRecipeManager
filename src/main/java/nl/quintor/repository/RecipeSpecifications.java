package nl.quintor.repository;

import nl.quintor.model.Recipe;
import nl.quintor.model.Ingredient;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.SetJoin;

import java.util.Set;

public class RecipeSpecifications {

    public static Specification<Recipe> isVegetarian(Boolean isVegetarian) {
        return (root, query, cb) -> {
            if (isVegetarian == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("isVegetarian"), isVegetarian);
        };
    }

    public static Specification<Recipe> servingCapacity(Integer servingCapacity) {
        return (root, query, cb) -> {
            if (servingCapacity == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("servingCapacity"), servingCapacity);
        };
    }

    public static Specification<Recipe> includeIngredients(Set<String> includeIngredients) {
        return (root, query, cb) -> {
            if (includeIngredients == null || includeIngredients.isEmpty()) {
                return cb.conjunction();
            }
            SetJoin<Recipe, Ingredient> ingredientsJoin = root.joinSet("ingredientList", JoinType.LEFT);
            return ingredientsJoin.get("name").in(includeIngredients);
        };
    }

    public static Specification<Recipe> excludeIngredients(Set<String> excludeIngredients) {
        return (root, query, cb) -> {
            if (excludeIngredients == null || excludeIngredients.isEmpty()) {
                return cb.conjunction();
            }
            SetJoin<Recipe, Ingredient> ingredientsJoin = root.joinSet("ingredientList", JoinType.LEFT);
            return cb.not(ingredientsJoin.get("name").in(excludeIngredients));
        };
    }

    public static Specification<Recipe> instructionsContain(String instructions) {
        return (root, query, cb) -> {
            if (instructions == null || instructions.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("instructions")), "%" + instructions.toLowerCase() + "%");
        };
    }

    public static Specification<Recipe> ingredientNameContains(String ingredientName) {
        return (root, query, cb) -> {
            if (ingredientName == null || ingredientName.isEmpty()) {
                return cb.conjunction();
            }
            SetJoin<Recipe, Ingredient> ingredientsJoin = root.joinSet("ingredientList", JoinType.LEFT);
            return cb.like(cb.lower(ingredientsJoin.get("name")), "%" + ingredientName.toLowerCase() + "%");
        };
    }
}
