package be.thomasmore.bookserver.services;

import be.thomasmore.bookserver.model.Author;
import be.thomasmore.bookserver.model.Book;
import be.thomasmore.bookserver.model.converters.AuthorDTOConverter;
import be.thomasmore.bookserver.model.converters.BookDTOConverter;
import be.thomasmore.bookserver.model.converters.BookDetailedDTOConverter;
import be.thomasmore.bookserver.model.dto.AuthorDTO;
import be.thomasmore.bookserver.model.dto.BookDTO;
import be.thomasmore.bookserver.model.dto.BookDetailedDTO;
import be.thomasmore.bookserver.repositories.AuthorRepository;
import be.thomasmore.bookserver.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookDTOConverter bookDTOConverter;
    @Autowired
    private BookDetailedDTOConverter bookDetailedDTOConverter;
    @Autowired
    private AuthorDTOConverter authorDTOConverter;

    public List<BookDTO> findAll(String titleKeyWord) {
        final List<Book> books = titleKeyWord == null ?
                bookRepository.findAll() :
                bookRepository.findByTitleContainingIgnoreCase(titleKeyWord);

        return books.stream()
                .map(b -> bookDTOConverter.convertToDto(b))
                .collect(Collectors.toList());
    }

    public BookDetailedDTO findOne(int id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("Book with id %d does not exist.", id)));

        return bookDetailedDTOConverter.convertToDto(book);
    }

    public List<AuthorDTO> authorsForBook(int bookId) {
        Optional<Book> bookFromDb = bookRepository.findById(bookId);
        if (bookFromDb.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Book with id %d not found.", bookId));

        return bookFromDb.get().getAuthors().stream()
                .map(a -> authorDTOConverter.convertToDto(a))
                .collect(Collectors.toList());
    }

    public BookDetailedDTO create(BookDetailedDTO bookDto) {
        // Normalize title to lowercase for case-insensitive comparison
        String normalizedTitle = bookDto.getTitle().toLowerCase().trim();

        // Check if a book with the same title (case-insensitive) already exists
        List<Book> allBooks = bookRepository.findAll();
        boolean titleExists = allBooks.stream()
                .anyMatch(book -> book.getTitle().toLowerCase().trim().equals(normalizedTitle));

        if (titleExists)
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    String.format("A book with title '%s' already exists (case-insensitive check).", bookDto.getTitle()));

        bookDto.setAuthors(null); //we do not want to update the relation
        final Book entityToSave = bookDetailedDTOConverter.convertToEntity(bookDto);
        final Book bookSaved = bookRepository.save(entityToSave);
        return bookDetailedDTOConverter.convertToDto(bookSaved);
    }

    public BookDetailedDTO edit(int id, BookDetailedDTO bookDto) {
        if (bookDto.getId() != id)
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    String.format("id in book (%d) does not match id in url (%d).", bookDto.getId(), id));

        Optional<Book> bookFromDb = bookRepository.findById(id);
        if (bookFromDb.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Book with id %d not found.", id));

        // Normalize title to lowercase for case-insensitive comparison
        String normalizedNewTitle = bookDto.getTitle().toLowerCase().trim();
        String currentTitle = bookFromDb.get().getTitle().toLowerCase().trim();

        // Check if the title is being changed
        if (!normalizedNewTitle.equals(currentTitle)) {
            // Check if another book with the same title (case-insensitive) already exists
            List<Book> allBooks = bookRepository.findAll();
            boolean titleExists = allBooks.stream()
                    .filter(book -> book.getId() != id) // Exclude current book
                    .anyMatch(book -> book.getTitle().toLowerCase().trim().equals(normalizedNewTitle));

            if (titleExists)
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        String.format("Another book already exists with title '%s' (case-insensitive check).", bookDto.getTitle()));
        }

        //overwrite fields present in bookDto - relations are not touched
        Book bookSaved = bookRepository.save(bookDetailedDTOConverter.convertToEntity(bookDto, bookFromDb.get()));
        return bookDetailedDTOConverter.convertToDto(bookSaved);
    }

    public BookDetailedDTO editAuthorsForBook(int id, List<Integer> authorIds) {
        Optional<Book> bookFromDb = bookRepository.findById(id);
        if (bookFromDb.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Book with id %d not found.", id));

        Book book = bookFromDb.get();

        // Handle null or empty list
        if (authorIds == null || authorIds.isEmpty()) {
            book.setAuthors(new ArrayList<>());
            Book bookSaved = bookRepository.save(book);
            return bookDetailedDTOConverter.convertToDto(bookSaved);
        }

        // Validate that all author IDs exist
        List<Integer> nonExistentAuthorIds = new ArrayList<>();
        for (Integer authorId : authorIds) {
            if (!authorRepository.existsById(authorId)) {
                nonExistentAuthorIds.add(authorId);
            }
        }

        // If any author IDs don't exist, throw an error with the list of non-existent IDs
        if (!nonExistentAuthorIds.isEmpty()) {
            String errorMessage = String.format("One or more author IDs do not exist: %s", nonExistentAuthorIds);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage);
        }

        // All author IDs are valid, create Author objects and set them
        List<Author> authorIdObjects = authorIds.stream()
                .map(Author::new)
                .collect(Collectors.toList());
        book.setAuthors(authorIdObjects);
        Book bookSaved = bookRepository.save(book);
        return bookDetailedDTOConverter.convertToDto(bookSaved);
    }

    public void delete(int id) {
        Optional<Book> bookFromDb = bookRepository.findById(id);
        if (bookFromDb.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Book with id %d not found.", id));

        bookRepository.deleteById(id);
    }
}
