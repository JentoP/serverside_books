package be.thomasmore.bookserver.repositories;

import be.thomasmore.bookserver.model.Serie;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SerieRepository extends CrudRepository<Serie, Integer> {
    Optional<Serie> findByName(@Param("name") String name);
}
