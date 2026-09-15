package com.dacaga.shortener.service;

import com.dacaga.shortener.domain.ShortUrl;
import com.dacaga.shortener.repository.ShortUrlRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShortUrlServiceTest {

    @Mock
    private ShortUrlRepository repository;

    @Mock
    private Base62Encoder encoder;

    @InjectMocks
    private ShortUrlService service;

    @Test
    void createSavesUrlAndGeneratesShortCode(){
        // Preparar para amañar los mocks
        ShortUrl savedEntity = new ShortUrl();
        savedEntity.setId(1L);
        savedEntity.setUrl("https://google.com");
        when(repository.save(any(ShortUrl.class))).thenReturn(savedEntity);
        when(encoder.encode(1L)).thenReturn("abc");

        // Actuar
        ShortUrl result = service.create("https://google.com");

        // Comprobar
        assertEquals(savedEntity.getUrl(), result.getUrl());
        assertEquals("abc", result.getUrlShort());
        verify(encoder).encode(1L);
        verify(repository, times(2)).save(any());
    }
}
