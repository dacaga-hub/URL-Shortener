package com.dacaga.shortener.controller;

import com.dacaga.shortener.domain.ShortUrl;
import com.dacaga.shortener.dto.CreateShortUrlRequest;
import com.dacaga.shortener.dto.ShortUrlResponse;
import com.dacaga.shortener.service.ShortUrlService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/urls")
public class ShortUrlController {

    private final ShortUrlService service;

    public ShortUrlController(ShortUrlService service) {
        this.service = service;
    }

    @PostMapping
    public ShortUrlResponse create(@Valid @RequestBody CreateShortUrlRequest request){
        String url = request.url();
        ShortUrl shortUrl= service.create(url);
        return new ShortUrlResponse(shortUrl.getUrlShort());
    }
}
