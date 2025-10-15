package be.thomasmore.bookserver.controllers;

import be.thomasmore.bookserver.model.converters.AuthorDTOConverter;
import be.thomasmore.bookserver.model.dto.AuthorDTO;
import be.thomasmore.bookserver.repositories.AuthorRepository;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @Operation(summary = "list of authors in the database.", description = "returns a list of authors sorted by name.")
    @GetMapping("")
    public List<AuthorDTO> findAll() {
        return authorRepository.findAll().stream()
                .map(authorDTOConverter::convertToDto)
                .collect(Collectors.toList());
    }
}
