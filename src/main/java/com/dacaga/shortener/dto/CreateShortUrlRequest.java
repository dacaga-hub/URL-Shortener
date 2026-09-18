package com.dacaga.shortener.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public record CreateShortUrlRequest(
    @NotBlank @URL String url) {

}
