package be.thomasmore.bookserver.controllers.book;

import be.thomasmore.bookserver.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql({"/sql/books/clean_books.sql"})
public class BookControllerGetOneBookWithSerieTest extends AbstractIntegrationTest {

    @Test
    public void getOneBookWithSerieInfo() throws Exception {
        // create serie and a book in it
        jdbcTemplate.execute("insert into serie (name) values ('Maddaddam')");
        jdbcTemplate.execute("insert into book (title, description, serie_id, number_in_serie) values ('Oryx and Crake', 'desc', 1, 1)");

        mockMvc.perform(getMockRequestGet("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Oryx and Crake"))
                .andExpect(jsonPath("$.serieId").value(1))
                .andExpect(jsonPath("$.serieName").value("Maddaddam"))
                .andExpect(jsonPath("$.numberInSerie").value(1));
    }
}
