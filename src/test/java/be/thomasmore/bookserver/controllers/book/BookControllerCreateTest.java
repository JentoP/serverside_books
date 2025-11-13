package be.thomasmore.bookserver.controllers.book;

import be.thomasmore.bookserver.AbstractIntegrationTest;
import be.thomasmore.bookserver.model.Book;
import be.thomasmore.bookserver.model.dto.BookDetailedDTO;
import be.thomasmore.bookserver.repositories.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(scripts = "/sql/books/clean_books.sql", executionPhase = AFTER_TEST_METHOD)
public class BookControllerCreateTest extends AbstractIntegrationTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    @WithMockUser
    public void createBook() throws Exception {
        final String BOOK_TITLE = "It is simple to create a book";
        final String BOOK_DESCRIPTION = "simple description";
        BookDetailedDTO newBookDto = BookDetailedDTO.builder()
                .title(BOOK_TITLE)
                .description(BOOK_DESCRIPTION)
                .build();

        mockMvc.perform(getMockRequestPost("/api/books/", newBookDto))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value(BOOK_TITLE))
                .andExpect(jsonPath("$.description").value(BOOK_DESCRIPTION))
                .andExpect(jsonPath("$.authors").isEmpty());

        Book loadedBook = bookRepository.findByTitle(BOOK_TITLE).orElseThrow();
        assertThat(loadedBook.getTitle()).isEqualTo(BOOK_TITLE);
    }

    @Test
    @ExceptionHandler
    @WithMockUser
    public void createBook_titleCannotBeEmpty() {
        BookDetailedDTO newBookDto = BookDetailedDTO.builder()
                .title(null)
                .build();

        assertThatThrownBy(() -> mockMvc.perform(getMockRequestPost("/api/books/", newBookDto)));
        assertThat(bookRepository.count()).isEqualTo(0);
    }

    @Test
    @ExceptionHandler
    @WithMockUser
    public void createBook_titleCannotBeBlank() {
        BookDetailedDTO newBookDto = BookDetailedDTO.builder()
                .title("")
                .build();

        assertThatThrownBy(() -> mockMvc.perform(getMockRequestPost("/api/books/", newBookDto)));
        assertThat(bookRepository.count()).isEqualTo(0);
    }

    @Test
    @ExceptionHandler
    @WithMockUser
    public void createBook_titleHasToBeUnique() throws Exception {
        BookDetailedDTO newBookDto = BookDetailedDTO.builder()
                .title("Recreate an existing book")
                .build();
        MockHttpServletRequestBuilder mockRequest = getMockRequestPost("/api/books/", newBookDto);

        //first time is ok
        mockMvc.perform(mockRequest).andExpect(status().isOk());

        //send the same POST again with same title - fails
        final MvcResult mvcResult = mockMvc.perform(mockRequest)
                .andExpect(status().isConflict())
                .andReturn();
        assertThat(mvcResult.getResponse().getErrorMessage()).contains("already exists (case-insensitive check)");
        assertThat(bookRepository.count()).isEqualTo(1);
    }

    @Test
    @WithMockUser
    public void createBook_givenIdIsNotTakenIntoAccount() throws Exception {
        final String BOOK_TITLE = "Book with random given id";
        BookDetailedDTO newBookDto = BookDetailedDTO.builder().id(57)
                .title(BOOK_TITLE)
                .build();

        mockMvc.perform(getMockRequestPost("/api/books/", newBookDto))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1)) // not 57!
                .andExpect(jsonPath("$.title").value(BOOK_TITLE))
                .andExpect(jsonPath("$.authors").isEmpty());

        Book loadedBook = bookRepository.findByTitle(BOOK_TITLE).orElseThrow();
        assertThat(loadedBook.getId()).isEqualTo(1); // not 57!
        assertThat(loadedBook.getTitle()).isEqualTo(BOOK_TITLE);
    }

    @Test
    @ExceptionHandler
    @WithMockUser
    public void createBook_titleHasToBeUniqueCaseInsensitive() throws Exception {
        BookDetailedDTO newBookDto = BookDetailedDTO.builder()
                .title("RECREATE AN EXISTING BOOK")
                .build();
        MockHttpServletRequestBuilder mockRequest = getMockRequestPost("/api/books/", newBookDto);

        //first time is ok
        mockMvc.perform(mockRequest).andExpect(status().isOk());

        //send the same POST again with different casing - fails
        final MvcResult mvcResult = mockMvc.perform(mockRequest)
                .andExpect(status().isConflict())
                .andReturn();
        assertThat(mvcResult.getResponse().getErrorMessage()).contains("already exists (case-insensitive check)");
        assertThat(bookRepository.count()).isEqualTo(1);
    }

    @Test
    @ExceptionHandler
    @WithMockUser
    public void createBook_titleHasToBeUniqueCaseInsensitiveWithDifferentCasing() throws Exception {
        // Create a book with title "1Q84"
        BookDetailedDTO firstBook = BookDetailedDTO.builder()
                .title("1Q84")
                .build();
        mockMvc.perform(getMockRequestPost("/api/books/", firstBook)).andExpect(status().isOk());

        // Try to create a book with title "1q84" (different casing) - should fail
        BookDetailedDTO secondBook = BookDetailedDTO.builder()
                .title("1q84")
                .build();
        final MvcResult mvcResult = mockMvc.perform(getMockRequestPost("/api/books/", secondBook))
                .andExpect(status().isConflict())
                .andReturn();
        assertThat(mvcResult.getResponse().getErrorMessage()).contains("already exists (case-insensitive check)");
        assertThat(bookRepository.count()).isEqualTo(1);
    }
}