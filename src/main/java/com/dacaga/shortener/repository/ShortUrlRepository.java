package com.dacaga.shortener.repository;

import com.dacaga.shortener.domain.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;



public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long>{

    //Firma que Spring interpreta
    Optional<ShortUrl> findByUrlShort(String urlShort);
}
