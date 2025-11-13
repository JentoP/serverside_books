package be.thomasmore.bookserver.controllers.book;

import be.thomasmore.bookserver.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql({"/sql/books/clean_books.sql"})
public class BookControllerGetOneBookWithoutSerieTest extends AbstractIntegrationTest {

    @Test
    public void getOneBookWithoutSerie_hasNullsForSerieFields() throws Exception {
        jdbcTemplate.execute("insert into book (title, description) values ('Standalone Book', 'desc')");

        mockMvc.perform(getMockRequestGet("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Standalone Book"))
                .andExpect(jsonPath("$.serieId").doesNotExist())
                .andExpect(jsonPath("$.serieName").doesNotExist())
                .andExpect(jsonPath("$.numberInSerie").doesNotExist());
    }
}
