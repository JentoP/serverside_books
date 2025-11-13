package be.thomasmore.bookserver.controllers.book;

import be.thomasmore.bookserver.AbstractIntegrationTest;
import be.thomasmore.bookserver.model.Book;
import be.thomasmore.bookserver.repositories.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;

import jakarta.transaction.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit tests for PUT /api/books/{id}/authors endpoint
 * Tests updating the authors associated with a specific book
 */
@Sql(scripts = {"/sql/books/create_2_books.sql", "/sql/authors/create_2_authors.sql"})
@Sql(scripts = {"/sql/books/clean_books.sql", "/sql/authors/clean_authors.sql"}, executionPhase = AFTER_TEST_METHOD)
public class BookControllerEditAuthorsForBookTest extends AbstractIntegrationTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    @WithMockUser
    @Transactional
    public void editAuthorsForBookWithOneAuthorIdReturnsUpdatedBook() throws Exception {
        // Update book 2 with author 1
        List<Integer> authorIds = List.of(1);

        mockMvc.perform(getMockRequestPut("/api/books/2/authors", authorIds))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.title").value("REST API Automation Testing from Scratch"))
                .andExpect(jsonPath("$.authors").isArray())
                .andExpect(jsonPath("$.authors.length()").value(1))
                .andExpect(jsonPath("$.authors[0].id").value(1))
                .andExpect(jsonPath("$.authors[0].name").value("Thomas Mann"));

        // Verify in database
        Book updatedBook = bookRepository.findById(2).orElseThrow();
        assertThat(updatedBook.getAuthors()).hasSize(1);
        assertThat(updatedBook.getAuthors().get(0).getId()).isEqualTo(1);
    }

    @Test
    @WithMockUser
    @Transactional
    public void editAuthorsForBookWithMultipleAuthorIdsReturnsUpdatedBook() throws Exception {
        // Update book 2 with both authors
        List<Integer> authorIds = List.of(1, 2);

        mockMvc.perform(getMockRequestPut("/api/books/2/authors", authorIds))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.authors").isArray())
                .andExpect(jsonPath("$.authors.length()").value(2))
                .andExpect(jsonPath("$.authors[0].id").value(1))
                .andExpect(jsonPath("$.authors[0].name").value("Thomas Mann"))
                .andExpect(jsonPath("$.authors[1].id").value(2))
                .andExpect(jsonPath("$.authors[1].name").value("Isaac Asimov"));

        // Verify in database
        Book updatedBook = bookRepository.findById(2).orElseThrow();
        assertThat(updatedBook.getAuthors()).hasSize(2);
    }

    @Test
    @WithMockUser
    @Transactional
    public void editAuthorsForBookWithEmptyListRemovesAllAuthors() throws Exception {
        // First add an author to book 2
        List<Integer> authorIds = List.of(1);
        mockMvc.perform(getMockRequestPut("/api/books/2/authors", authorIds))
                .andExpect(status().isOk());

        // Now remove all authors by sending empty list
        List<Integer> emptyAuthorIds = List.of();
        mockMvc.perform(getMockRequestPut("/api/books/2/authors", emptyAuthorIds))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.authors").isArray())
                .andExpect(jsonPath("$.authors.length()").value(0));

        // Verify in database
        Book updatedBook = bookRepository.findById(2).orElseThrow();
        assertThat(updatedBook.getAuthors()).isEmpty();
    }

    @Test
    @WithMockUser
    public void editAuthorsForNonExistentBookReturnsNotFound() throws Exception {
        // Try to update authors for book that doesn't exist
        List<Integer> authorIds = List.of(1);

        mockMvc.perform(getMockRequestPut("/api/books/999/authors", authorIds))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    @Transactional
    public void editAuthorsForBookWithNullListReturnsEmptyAuthors() throws Exception {
        // First add an author to book 2
        List<Integer> authorIds = List.of(1);
        mockMvc.perform(getMockRequestPut("/api/books/2/authors", authorIds))
                .andExpect(status().isOk());

        // Now send null (which should be treated as empty list)
        mockMvc.perform(getMockRequestPut("/api/books/2/authors", null))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.authors").isArray())
                .andExpect(jsonPath("$.authors.length()").value(0));
    }

    @Test
    @WithMockUser
    @Transactional
    public void editAuthorsForBookReplacesPreviousAuthors() throws Exception {
        // First add author 1 to book 2
        List<Integer> firstAuthorIds = List.of(1);
        mockMvc.perform(getMockRequestPut("/api/books/2/authors", firstAuthorIds))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authors.length()").value(1))
                .andExpect(jsonPath("$.authors[0].id").value(1));

        // Now replace with author 2
        List<Integer> secondAuthorIds = List.of(2);
        mockMvc.perform(getMockRequestPut("/api/books/2/authors", secondAuthorIds))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authors.length()").value(1))
                .andExpect(jsonPath("$.authors[0].id").value(2))
                .andExpect(jsonPath("$.authors[0].name").value("Isaac Asimov"));

        // Verify in database
        Book updatedBook = bookRepository.findById(2).orElseThrow();
        assertThat(updatedBook.getAuthors()).hasSize(1);
        assertThat(updatedBook.getAuthors().get(0).getId()).isEqualTo(2);
    }

    @Test
    @WithMockUser
    @Transactional
    public void editAuthorsForBookReturnsDetailedBookDTO() throws Exception {
        // Verify that the response contains all book details (id, title, description, authors)
        List<Integer> authorIds = List.of(1);

        mockMvc.perform(getMockRequestPut("/api/books/2/authors", authorIds))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.description").exists())
                .andExpect(jsonPath("$.authors").exists());
    }

    @Test
    @WithMockUser
    @Transactional
    public void editAuthorsForBookWithNonExistentAuthorIdReturnsInternalServerError() throws Exception {
        // Try to update book 2 with a non-existent author ID (999)
        List<Integer> authorIds = List.of(999);

        mockMvc.perform(getMockRequestPut("/api/books/2/authors", authorIds))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").value("One or more author IDs do not exist: [999]"));
    }

    @Test
    @WithMockUser
    @Transactional
    public void editAuthorsForBookWithMultipleNonExistentAuthorIdsReturnsInternalServerError() throws Exception {
        // Try to update book 2 with multiple non-existent author IDs
        List<Integer> authorIds = List.of(999, 1000, 1001);

        mockMvc.perform(getMockRequestPut("/api/books/2/authors", authorIds))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").value("One or more author IDs do not exist: [999, 1000, 1001]"));
    }

    @Test
    @WithMockUser
    @Transactional
    public void editAuthorsForBookWithMixedValidAndInvalidAuthorIdsReturnsInternalServerError() throws Exception {
        // Try to update book 2 with a mix of valid and invalid author IDs
        List<Integer> authorIds = List.of(1, 999, 2, 1000);

        mockMvc.perform(getMockRequestPut("/api/books/2/authors", authorIds))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").value("One or more author IDs do not exist: [999, 1000]"));
    }

}
