package be.thomasmore.bookserver.controllers;

import be.thomasmore.bookserver.model.Serie;
import be.thomasmore.bookserver.repositories.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/series")
public class SerieController {
    @Autowired
    private SerieRepository serieRepository;

    @GetMapping("")
    public Iterable<Serie> getAllSeries() {
        return serieRepository.findAll();
    }

    @GetMapping("/{id}")
    public Serie getSerie(@PathVariable int id) {
        return serieRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No serie with id " + id + " found"));
    }
}
