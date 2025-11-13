package be.thomasmore.bookserver.controllers.book;

import be.thomasmore.bookserver.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit tests for GET /api/books/{id}/authors endpoint
 * Tests retrieving the authors associated with a specific book
 */
@Sql(scripts = {"/sql/books/create_2_books.sql", "/sql/authors/create_2_authors.sql"})
@Sql(scripts = {"/sql/books/clean_books.sql", "/sql/authors/clean_authors.sql"}, executionPhase = AFTER_TEST_METHOD)
public class BookControllerGetAuthorsForBookTest extends AbstractIntegrationTest {

    @Test
    @WithMockUser
    public void getAuthorsForBookWithNoAuthorsReturnsEmptyList() throws Exception {
        // Book with id 2 has no authors assigned
        mockMvc.perform(getMockRequestGet("/api/books/2/authors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @WithMockUser
    public void getAuthorsForNonExistentBookReturnsNotFound() throws Exception {
        // Book with id 999 does not exist
        mockMvc.perform(getMockRequestGet("/api/books/999/authors"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    @Sql(scripts = {"/sql/authors/create_author_with_book.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"/sql/authors/clean_author_with_book.sql"}, executionPhase = AFTER_TEST_METHOD)
    public void getAuthorsForBookWithOneAuthorReturnsAuthorData() throws Exception {
        // Book with id 1 has one author (Thomas Mann)
        mockMvc.perform(getMockRequestGet("/api/books/1/authors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Thomas Mann"));
    }

    @Test
    @WithMockUser
    public void getAuthorsForBookReturnsOnlyIdAndName() throws Exception {
        // Verify that the response contains only id and name fields (no description or country)
        mockMvc.perform(getMockRequestGet("/api/books/2/authors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

}
