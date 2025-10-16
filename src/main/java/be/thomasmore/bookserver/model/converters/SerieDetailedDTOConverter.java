package be.thomasmore.bookserver.model.converters;

import be.thomasmore.bookserver.model.Serie;
import be.thomasmore.bookserver.model.dto.SerieDetailedDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SerieDetailedDTOConverter {

    @Autowired
    private ModelMapper modelMapper;

    /**
     * @param serie the entity from the db
     * @return SerieDetailedDTO object to send to the client.
     */
    public SerieDetailedDTO convertToDto(Serie serie) {
        return modelMapper.map(serie, SerieDetailedDTO.class);
    }

}
