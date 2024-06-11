package nl.quintor.repository;

import nl.quintor.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long>, RecipeRepositoryCustom {

    @Query("SELECT r FROM Recipe r LEFT JOIN FETCH r.ingredientList")
    List<Recipe> findAllWithIngredients();
}
