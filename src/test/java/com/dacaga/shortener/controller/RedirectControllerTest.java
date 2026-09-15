package com.dacaga.shortener.controller;

import com.dacaga.shortener.domain.ShortUrl;
import com.dacaga.shortener.repository.ShortUrlRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

@WebMvcTest(RedirectController.class)
class RedirectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ShortUrlRepository repository;

    @Test
    void returnOptionalExists() throws Exception{
        //Preparar
        ShortUrl fake = new ShortUrl();
        fake.setUrl("https://google.com");
        fake.setUrlShort("abc");
        when(repository.findByUrlShort(any())).thenReturn(Optional.of(fake));

        // Actuar + Comprobar
        mockMvc.perform(get("/" + fake.getUrlShort()))
                            .andExpect(status().isFound())
                            .andExpect(header().string("Location", fake.getUrl()))
                            ;
    }

    @Test
    void returnOptionalEmpty() throws Exception{
        //Preparar
        when(repository.findByUrlShort(any())).thenReturn(Optional.empty());

        mockMvc.perform(get("/noexiste"))
                            .andExpect(status().isNotFound())
                            ;
    }
}
