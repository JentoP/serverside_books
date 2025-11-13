package be.thomasmore.bookserver.controllers.serie;

import be.thomasmore.bookserver.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql({"/sql/books/clean_books.sql", "/sql/series/create_2_series.sql"})
@Sql(scripts = "/sql/series/clean_series.sql", executionPhase = AFTER_TEST_METHOD)
@Sql(scripts = "/sql/books/clean_books.sql", executionPhase = AFTER_TEST_METHOD)
public class SerieControllerGetOneSerieWithBooksTest extends AbstractIntegrationTest {

    @Test
    public void getOneSerieIncludesBooksList() throws Exception {
        // create books that belong to serie id 1 with number_in_serie
        jdbcTemplate.execute("insert into book (title, description, serie_id, number_in_serie) values ('Oryx and Crake', 'desc', 1, 1)");
        jdbcTemplate.execute("insert into book (title, description, serie_id, number_in_serie) values ('The year of the flood', 'desc', 1, 2)");

        mockMvc.perform(getMockRequestGet("/api/series/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Programming in C"))
                .andExpect(jsonPath("$.books").isArray())
                .andExpect(jsonPath("$.books[0].title").value("Oryx and Crake"))
                .andExpect(jsonPath("$.books[0].numberInSerie").value(1))
                .andExpect(jsonPath("$.books[1].title").value("The year of the flood"))
                .andExpect(jsonPath("$.books[1].numberInSerie").value(2));
    }
}
