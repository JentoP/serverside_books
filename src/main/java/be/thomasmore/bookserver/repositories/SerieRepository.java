package be.thomasmore.bookserver.repositories;

import be.thomasmore.bookserver.model.Serie;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends CrudRepository<Serie, Integer> {
    List<Serie> findAll();

    Optional<Serie> findByNameIgnoreCase(String name);

    Optional<Serie> findByIdAndNameIgnoreCase(int id, String name);
}
