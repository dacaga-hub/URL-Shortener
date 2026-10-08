package com.dacaga.shortener.service;

import com.dacaga.shortener.domain.ShortUrl;
import com.dacaga.shortener.repository.ShortUrlRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ShortUrlService {

    private final ShortUrlRepository repository;
    private final Base62Encoder encoder;

    // Constructor: Spring inyecta ambos colaboradores aquí
    public ShortUrlService(ShortUrlRepository repository, Base62Encoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    @Transactional
    public ShortUrl create(String url) {
        ShortUrl shortUrl = new ShortUrl();
        shortUrl.setUrl(url);
        ShortUrl saved = repository.save(shortUrl);
        saved.setUrlShort(encoder.encode(saved.getId()));
        return saved;
    }
}