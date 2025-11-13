package be.thomasmore.bookserver.controllers.serie;

import be.thomasmore.bookserver.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql("/sql/series/create_2_series.sql")
@Sql(scripts = "/sql/series/clean_series.sql", executionPhase = AFTER_TEST_METHOD)
public class SerieControllerDeleteSerieTest extends AbstractIntegrationTest {


    @Test
    @WithMockUser
    public void deleteOneSerie() throws Exception {
        mockMvc.perform(getMockRequestDelete("/api/series/{id}", 1))
                .andExpect(status().isNoContent());
    }
}
