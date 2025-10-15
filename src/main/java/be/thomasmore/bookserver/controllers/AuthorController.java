package be.thomasmore.bookserver.controllers;

import be.thomasmore.bookserver.model.converters.AuthorDTOConverter;
import be.thomasmore.bookserver.model.converters.AuthorDetailedDTOConverter;
import be.thomasmore.bookserver.model.dto.AuthorDTO;
import be.thomasmore.bookserver.model.dto.AuthorDetailedDTO;
import be.thomasmore.bookserver.repositories.AuthorRepository;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/authors")
@Slf4j
public class AuthorController {
    
    @Autowired
    private AuthorRepository authorRepository;
    
    @Autowired
    private AuthorDTOConverter authorDTOConverter;

    @Autowired
    private AuthorDetailedDTOConverter authorDetailedDTOConverter;

    @Operation(summary = "list of authors in the database.", 
              description = "returns a list of authors sorted by name.")
    @GetMapping("")
    public List<AuthorDTO> findAll() {
        return authorRepository.findAll().stream()
                .map(authorDTOConverter::convertToDto)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Get a single author by ID", 
              description = "Returns detailed information about a specific author including their books.")
    @GetMapping("/{id}")
    public AuthorDetailedDTO findOne(@PathVariable int id) {
        return authorRepository.findById(id)
                .map(authorDetailedDTOConverter::convertToDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found"));
    }
}
