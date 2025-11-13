package be.thomasmore.bookserver.controllers;

import be.thomasmore.bookserver.model.dto.AuthorDTO;
import be.thomasmore.bookserver.model.dto.AuthorDetailedDTO;
import be.thomasmore.bookserver.model.dto.BookDetailedDTO;
import be.thomasmore.bookserver.services.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "create a new author in the database.",
            description = "Returns new author (containing id from database). </br>" +
                    "The name of the new author must be unique (case insensitive). </br>" +
                    "The name of the author should never be empty or blank. </br>")
    @PostMapping("")
    public AuthorDetailedDTO create(@Valid @RequestBody AuthorDetailedDTO authorDTO) {
        log.info("##### create author");
        return authorService.create(authorDTO);
    }

    @Operation(summary = "edit existing author in the database.",
            description = "The books are <b>not</b> updated in the new author.</br>" +
                    "Use PUT api/books/{id}/authors to update those. </br>" +
                    "</br>" +
                    "Returns updated author. ")
    @PutMapping("{id}")
    public AuthorDetailedDTO edit(@PathVariable int id, @Valid @RequestBody AuthorDetailedDTO authorDto) {
        log.info(String.format("##### edit author %d", id));
        return authorService.edit(id, authorDto);
    }

    @Operation(summary = "delete existing author from the database.",
            description = "If the author is still associated with books, the author cannot be deleted. </br>" +
                    "In that case a Bad Request (400) error is returned with a message indicating how many books need to be deleted first. </br>" +
                    "Please delete or reassign all books associated with this author before attempting to delete the author. </br>" +
                    "If the author does not exist, a Not Found (404) error is returned.  </br>" )
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info(String.format("##### delete author %d", id));
        authorService.delete(id);
    }

}
