package be.thomasmore.bookserver.model.converters;

import be.thomasmore.bookserver.model.Author;
import be.thomasmore.bookserver.model.dto.AuthorDetailedDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthorDetailedDTOConverter {

    @Autowired
    private ModelMapper modelMapper;

    /**
     * @param author the entity from the db
     * @return AuthorDetailedDTO object to send to the client.
     */
    public AuthorDetailedDTO convertToDto(Author author) {
        return modelMapper.map(author, AuthorDetailedDTO.class);
    }
}
