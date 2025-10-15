package be.thomasmore.bookserver.model.converters;

import be.thomasmore.bookserver.model.Author;
import be.thomasmore.bookserver.model.dto.AuthorDetailedDTO;
import be.thomasmore.bookserver.model.dto.BookDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuthorDetailedDTOConverter {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private BookDTOConverter bookDTOConverter;

    /**
     * @param author the entity from the db
     * @return AuthorDetailedDTO object with author details and their books
     */
    public AuthorDetailedDTO convertToDto(Author author) {
        if (author == null) {
            return null;
        }

        List<BookDTO> bookDTOs = author.getBooks() != null ?
                author.getBooks().stream()
                        .map(bookDTOConverter::convertToDto)
                        .collect(Collectors.toList()) :
                null;

        return AuthorDetailedDTO.builder()
                .id(author.getId())
                .name(author.getName())
                .books(bookDTOs)
                .build();
    }
}
