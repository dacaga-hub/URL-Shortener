package com.dacaga.shortener.controller;

import com.dacaga.shortener.repository.ShortUrlRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.net.URI;


@RestController
public class RedirectController {

    private final ShortUrlRepository repository;

    public RedirectController(ShortUrlRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/{code}")
    public ResponseEntity<Void> redirect(@PathVariable String code) {
        return repository.findByUrlShort(code)
                .map(shortUrl -> ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create(shortUrl.getUrl()))
                    .<Void>build())
                .orElse(ResponseEntity.notFound().build());
        }
}