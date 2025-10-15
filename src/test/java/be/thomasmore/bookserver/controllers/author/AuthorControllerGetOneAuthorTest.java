package be.thomasmore.bookserver.controllers.author;

import be.thomasmore.bookserver.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql("/sql/authors/create_2_authors.sql")
@Sql(scripts = "/sql/authors/clean_authors.sql", executionPhase = AFTER_TEST_METHOD)
public class AuthorControllerGetOneAuthorTest extends AbstractIntegrationTest {

    @Test
    public void getExistingAuthor() throws Exception {
        // Test getting an existing author
        mockMvc.perform(getMockRequestGet("/api/authors/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").isString())
                .andExpect(jsonPath("$.books").isArray());
    }

    @Test
    public void getNonExistingAuthor_ShouldReturnNotFound() throws Exception {
        // Test getting a non-existing author
        int nonExistingId = 999;
        mockMvc.perform(getMockRequestGet("/api/authors/" + nonExistingId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Author not found"));
    }

    @Test
    public void getAuthorWithInvalidId_ShouldReturnBadRequest() throws Exception {
        // Test with invalid ID format (non-numeric)
        mockMvc.perform(getMockRequestGet("/api/authors/not-a-number"))
                .andExpect(status().isBadRequest());
    }
}
