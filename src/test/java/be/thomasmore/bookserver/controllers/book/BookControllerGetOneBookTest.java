package be.thomasmore.bookserver.controllers.book;

import be.thomasmore.bookserver.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql("/sql/books/create_2_books.sql")
@Sql(scripts = "/sql/books/clean_books.sql", executionPhase = AFTER_TEST_METHOD)
public class BookControllerGetOneBookTest extends AbstractIntegrationTest {

    @Test
    public void getOneBook() throws Exception {
        mockMvc.perform(getMockRequestGet("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Automation"))
                .andExpect(jsonPath("$.description").value("Test Automation Description"))
                .andExpect(jsonPath("$.authors").exists())
                .andExpect(jsonPath("$.authors").isEmpty());
    }

    @Test
    public void getOneBookNotFound() throws Exception {
        final MvcResult mvcResult =
                mockMvc.perform(getMockRequestGet("/api/books/9999"))
                        .andExpect(status().isNotFound())
                        .andReturn();
        assertThat(mvcResult.getResponse().getErrorMessage()).isEqualTo("Book with id 9999 does not exist.");

    }

    @Test
    public void getOneBookWithIdZero() throws Exception {
        final MvcResult mvcResult =
                mockMvc.perform(getMockRequestGet("/api/books/0"))
                        .andExpect(status().isNotFound())
                        .andReturn();
        assertThat(mvcResult.getResponse().getErrorMessage()).isEqualTo("Book with id 0 does not exist.");
    }

    @Test
    public void getOneBookWithNegativeId() throws Exception {
        final MvcResult mvcResult =
                mockMvc.perform(getMockRequestGet("/api/books/-1"))
                        .andExpect(status().isNotFound())
                        .andReturn();
        assertThat(mvcResult.getResponse().getErrorMessage()).isEqualTo("Book with id -1 does not exist.");
    }

    @Test
    public void getOneBookWithMaxIntId() throws Exception {
        final MvcResult mvcResult =
                mockMvc.perform(getMockRequestGet("/api/books/2147483647"))
                        .andExpect(status().isNotFound())
                        .andReturn();
        assertThat(mvcResult.getResponse().getErrorMessage()).isEqualTo("Book with id 2147483647 does not exist.");
    }

}
