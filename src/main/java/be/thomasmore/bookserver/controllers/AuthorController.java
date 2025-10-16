package be.thomasmore.bookserver.controllers;

import be.thomasmore.bookserver.model.dto.AuthorDTO;
import be.thomasmore.bookserver.model.dto.AuthorDetailedDTO;
import be.thomasmore.bookserver.services.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/authors")
@Slf4j
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @Operation(summary = "find all the authors that are stored in the database ")
    @GetMapping("")
    public Iterable<AuthorDTO> findAll() {
        log.info("##### findAll authors");
        return authorService.findAll();
    }

    @Operation(summary = "get 1 author from the database.",
            description = "Author with id is fetched from database. " +
                    "</br>" +
                    "The books Collection contains only id, title and authors-array. </br>" +
                    "Use GET api/books/{id} to fetch more info about the books. ")
    @GetMapping("{id}")
    public AuthorDetailedDTO findOne(@PathVariable int id) {
        log.info(String.format("##### findOne author %d", id));
        return authorService.findOne(id);
    }
}
