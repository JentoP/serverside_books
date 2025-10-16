package be.thomasmore.bookserver.controllers.serie;

import be.thomasmore.bookserver.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql("/sql/series/create_2_series.sql")
@Sql(scripts = "/sql/series/clean_series.sql", executionPhase = AFTER_TEST_METHOD)
public class SerieControllerGetOneSerieTest extends AbstractIntegrationTest {

    @Test
    public void getOneSerie() throws Exception {
        mockMvc.perform(getMockRequestGet("/api/series/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Programming in C"));
    }

    @Test
    public void getOneSerieNotFound() throws Exception {
        final MvcResult mvcResult =
                mockMvc.perform(getMockRequestGet("/api/series/9999"))
                        .andExpect(status().isInternalServerError())
                        .andReturn();
        assertThat(mvcResult.getResponse().getErrorMessage()).isEqualTo("Serie with id 9999 does not exist.");

    }

}
