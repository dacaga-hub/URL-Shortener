package com.dacaga.shortener;

import com.dacaga.shortener.repository.ShortUrlRepository;
import com.dacaga.shortener.service.Base62Encoder;
import com.dacaga.shortener.service.ShortUrlService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
class ShortUrlServiceTransactionIntegrationTest {

    @Autowired
    private ShortUrlService service;

    @Autowired
    private ShortUrlRepository repository;

    @MockitoBean
    private Base62Encoder encoder;

    @Test
    void rollsBackInsertWhenEncodingFails() {
        when(encoder.encode(anyLong())).thenThrow(new IllegalStateException("boom"));
        long before = repository.count();

        assertThrows(IllegalStateException.class,
                () -> service.create("https://example.com/tx"));

        assertEquals(before, repository.count());
    }
}