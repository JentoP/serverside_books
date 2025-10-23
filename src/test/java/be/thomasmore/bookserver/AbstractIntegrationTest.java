package be.thomasmore.bookserver;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ContextConfiguration
public abstract class AbstractIntegrationTest {
    @Autowired
    protected ObjectMapper mapper;

    @Autowired
    protected MockMvc mockMvc;

    protected MockHttpServletRequestBuilder getMockRequestGet(String url) {
        return MockMvcRequestBuilders
                .get(url)
                .contentType(MediaType.APPLICATION_JSON);
    }

    protected <T> MockHttpServletRequestBuilder getMockRequestPost(String url, T dto) throws JsonProcessingException {
        return MockMvcRequestBuilders.post(url)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(dto));
    }
    protected <T> MockHttpServletRequestBuilder getMockRequestPut(String url, T dto) throws JsonProcessingException {
        return MockMvcRequestBuilders.put(url)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(dto));
    }

    protected MockHttpServletRequestBuilder getMockRequestDelete(String url, Integer id)  {
        return MockMvcRequestBuilders.delete(url, id)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON);
    }

}

