package com.dacaga.shortener;

import com.dacaga.shortener.domain.ShortUrl;
import com.dacaga.shortener.repository.ShortUrlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
class ShortUrlFlowIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ShortUrlRepository repository;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void createsShortUrlAndRedirectsToOriginal() throws Exception {
        String original = "https://example.com/it-" + UUID.randomUUID();

        mockMvc.perform(post("/api/urls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"url\":\"" + original + "\"}"))
                .andExpect(status().isCreated());

        ShortUrl saved = repository.findAll().stream()
                .filter(s -> original.equals(s.getUrl()))
                .findFirst()
                .orElseThrow();
        assertThat(saved.getUrlShort()).isNotBlank();

        mockMvc.perform(get("/" + saved.getUrlShort()))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", original));
    }

    @Test
    void unknownCodeReturns404() throws Exception {
        mockMvc.perform(get("/noexiste"))
                .andExpect(status().isNotFound());
    }
}