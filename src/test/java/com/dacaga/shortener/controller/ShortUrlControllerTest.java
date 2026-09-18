package com.dacaga.shortener.controller;

import com.dacaga.shortener.domain.ShortUrl;
import com.dacaga.shortener.service.ShortUrlService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ShortUrlController.class)
class ShortUrlControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ShortUrlService service;

    @Test
    void createReturnShortCode() throws Exception{
        //Preparar: amañar el service
        ShortUrl fake = new ShortUrl();
        fake.setUrl("https://google.com");
        fake.setUrlShort("abc");
        when(service.create(any())).thenReturn(fake);

        //Actuar + Comprobar
        mockMvc.perform(post("/api/urls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"url\": \"https://google.com\"}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.urlShort").value("abc"))
                    ;
    }

    @Test
    void createRejectsBlankUrl() throws Exception{

        mockMvc.perform(post("/api/urls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"url\": \"\"}"))
                    .andExpect(status().isBadRequest());
    }

    @Test
    void createRejectsMalformedUrl() throws Exception{

        mockMvc.perform(post("/api/urls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"url\": \"patata\"}"))
                    .andExpect(status().isBadRequest());
    }
}
