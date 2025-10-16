package be.thomasmore.bookserver.controllers.author;

import be.thomasmore.bookserver.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("SpringTestingSqlInspection")
@Sql("/sql/authors/create_2_authors.sql")
@Sql(scripts = "/sql/authors/clean_authors.sql", executionPhase = AFTER_TEST_METHOD)
public class AuthorControllerGetOneAuthorTest extends AbstractIntegrationTest {

    @Test
    public void getOneAuthor() throws Exception {
        mockMvc.perform(getMockRequestGet("/api/authors/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Thomas Mann"))
                .andExpect(jsonPath("$.books").exists())
                .andExpect(jsonPath("$.books").isEmpty());
    }

    @Test
    public void getOneAuthorNotFound() throws Exception {
        final MvcResult mvcResult =
                mockMvc.perform(getMockRequestGet("/api/authors/9999"))
                        .andExpect(status().isInternalServerError()) // strange!!! I expected isNotFound().....??????
                        .andReturn();
        assertThat(mvcResult.getResponse().getErrorMessage()).isEqualTo("Author with id 9999 does not exist.");

    }

}
