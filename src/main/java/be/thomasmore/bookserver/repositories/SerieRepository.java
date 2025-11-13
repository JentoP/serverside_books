package be.thomasmore.bookserver.repositories;

import be.thomasmore.bookserver.model.Serie;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends CrudRepository<Serie, Integer> {
    List<Serie> findAll();

    Optional<Serie> findByNameIgnoreCase(String name);

//    Optional<Serie> findByIdAndTitleIgnoreCase(int id, String title);
}
