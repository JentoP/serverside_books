package be.thomasmore.bookserver.services;

import be.thomasmore.bookserver.model.Serie;
import be.thomasmore.bookserver.model.Book;
import be.thomasmore.bookserver.model.converters.SerieDTOConverter;
import be.thomasmore.bookserver.model.converters.SerieDetailedDTOConverter;
import be.thomasmore.bookserver.model.dto.SerieDTO;
import be.thomasmore.bookserver.model.dto.SerieDetailedDTO;
import be.thomasmore.bookserver.model.dto.SerieDetailedDTOBookItem;
import be.thomasmore.bookserver.repositories.SerieRepository;
import be.thomasmore.bookserver.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SerieService {
    @Autowired
    private SerieRepository serieRepository;

    @Autowired
    private SerieDTOConverter serieDTOConverter;
    @Autowired
    private SerieDetailedDTOConverter serieDetailedDTOConverter;
    @Autowired
    private BookRepository bookRepository;

    public List<SerieDTO> findAll() {
        final List<Serie> series = serieRepository.findAll();
        return series.stream()
                .map(a -> serieDTOConverter.convertToDto(a))
                .collect(Collectors.toList());
    }

    public SerieDetailedDTO findOne(int id) {
        final Optional<Serie> serie = serieRepository.findById(id);
        if (serie.isEmpty())
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    String.format("Serie with id %d does not exist.", id));
        SerieDetailedDTO dto = serieDetailedDTOConverter.convertToDto(serie.get());
        // add books list with title and numberInSerie
        final java.util.List<Book> books = bookRepository.findBySerie_IdOrderByNumberInSerieAscIdAsc(id);
        final java.util.List<SerieDetailedDTOBookItem> items = books.stream()
                .map(b -> new SerieDetailedDTOBookItem(b.getId(), b.getTitle(), b.getNumberInSerie()))
                .collect(java.util.stream.Collectors.toList());
        dto.setBooks(items);
        return dto;
    }

    public SerieDetailedDTO create(SerieDetailedDTO serieDto) {
        if (serieRepository.findByNameIgnoreCase(serieDto.getName()).isPresent())
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, String.format("Serie with name already exists.", serieDto.getName()));
        final Serie entityToSave = serieDetailedDTOConverter.convertToEntity(serieDto);
        final Serie serieSaved = serieRepository.save(entityToSave);
        return serieDetailedDTOConverter.convertToDto(serieSaved);
    }

    public SerieDetailedDTO edit(int id, SerieDetailedDTO serieDTO) {
        if (serieDTO.getId() != id)
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, String.format("id in serie (%d) does not match id in url (%d)", serieDTO.getId(), id));
        Optional<Serie> serieFromDb = serieRepository.findById(id);
        if (serieFromDb.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Book with id %d not found"));
        Optional<Serie> allSeriesWithNewName = serieRepository.findByIdAndNameIgnoreCase(id, serieDTO.getName());
        if (allSeriesWithNewName.isPresent())
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, String.format("Another serie already exist with title %s.", serieDTO.getName()));
        Serie serieSaved = serieRepository.save(serieDetailedDTOConverter.convertToEntity(serieDTO, serieFromDb.get()));
        return serieDetailedDTOConverter.convertToDto(serieSaved);
    }

    public void delete(int id) {
        Optional<Serie> serieFromDb = serieRepository.findById((id));
        if (serieFromDb.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Serie with id %d not found", id));
        serieRepository.deleteById(id);
    }
}
