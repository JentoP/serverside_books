package be.thomasmore.bookserver.controllers;

import be.thomasmore.bookserver.model.dto.SerieDTO;
import be.thomasmore.bookserver.model.dto.SerieDetailedDTO;
import be.thomasmore.bookserver.services.SerieService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/series")
@Slf4j
public class SerieController {

    @Autowired
    private SerieService serieService;

    @Operation(summary = "find all the series that are stored in the database ",
            description = "All series are returned. </br>")
    @GetMapping("")
    public Iterable<SerieDTO> findAll() {
        log.info("##### findAll series");
        return serieService.findAll();
    }


    @Operation(summary = "get 1 serie from the database.",
            description = "Serie with id is fetched from database. ")
    @GetMapping("{id}")
    public SerieDetailedDTO findOne(@PathVariable int id) {
        log.info(String.format("##### findOne serie %d", id));
        return serieService.findOne(id);
    }

    //create
    @Operation(summary = "Create a new serie in the database",
            description = "The name cannot be empty and has to be unique. With case insensitive check. Adds the newly created serie to the database")
    @PostMapping("")
    public SerieDetailedDTO create(@Valid @RequestBody SerieDetailedDTO serieDTO) {
        log.info("##### create serie");
        return serieService.create(serieDTO);
    }

    //    edit
    @Operation(summary = "Edit an existing serie in the database",
            description = "The name cannot be empty and has to be unique. With case insensitive check. Updates the existing serie in the database")
    @PutMapping("{id}")
    public SerieDetailedDTO edit(@PathVariable int id, @RequestBody SerieDetailedDTO serieDTO) {
        log.info(String.format("#### edit serie %d", id));
        return serieService.edit(id, serieDTO);
    }

    //delete
    @Operation(summary = "Delete an existing serie from the database",
            description = "Deletes the serie with the given id from the database")
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info(String.format("##### delete book %d", id));
        serieService.delete(id);
    }
}
