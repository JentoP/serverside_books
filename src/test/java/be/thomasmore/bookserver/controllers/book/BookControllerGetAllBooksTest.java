package be.thomasmore.bookserver.controllers.book;

import be.thomasmore.bookserver.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql("/sql/books/create_2_books.sql")
@Sql(scripts = "/sql/books/clean_books.sql", executionPhase = AFTER_TEST_METHOD)
public class BookControllerGetAllBooksTest extends AbstractIntegrationTest {

    @Test
    public void getAllBooks() throws Exception {
        mockMvc.perform(getMockRequestGet("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Test Automation"))
                .andExpect(jsonPath("$[0].authors").exists())
                .andExpect(jsonPath("$[0].authors").isEmpty())
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].title").value("REST API Automation Testing from Scratch"))
                .andExpect(jsonPath("$[1].authors").exists())
                .andExpect(jsonPath("$[1].authors").isEmpty());
    }

    @Test
    public void getAllBooksFilter() throws Exception {
        mockMvc.perform(getMockRequestGet("/api/books?titleKeyWord=from Scratch"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value("REST API Automation Testing from Scratch"));
    }

    @Test
    public void getAllBooksFilterCaseInsensitive() throws Exception {
        mockMvc.perform(getMockRequestGet("/api/books?titleKeyWord=FROM SCRATCH"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value("REST API Automation Testing from Scratch"));
    }

    @Test
    public void getAllBooksFilterNoResults() throws Exception {
        mockMvc.perform(getMockRequestGet("/api/books?titleKeyWord=NonExistentBook"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void getAllBooksFilterEmptyString() throws Exception {
        mockMvc.perform(getMockRequestGet("/api/books?titleKeyWord="))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void getAllBooksFilterSpecialCharacters() throws Exception {
        mockMvc.perform(getMockRequestGet("/api/books?titleKeyWord=@#$%"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}
