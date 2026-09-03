package com.dacaga.shortener.domain;

import jakarta.persistence.*;

@Entity
public class ShortUrl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @Column(length = 2048)
    private String url;

    @Column(unique = true, length = 10)
    private String urlShort;

    //Constructor
    public ShortUrl(){}

    // GET's
    public Long getId(){
        return id;
    }
    public String getUrl(){
        return url;
    }
    public String getUrlShort(){
        return urlShort;
    }
    //SET's
    public void setId(Long id){
        this.id = id;
    }
    public void setUrl(String url){
        this.url = url;
    }
    public void setUrlShort(String urlShort){
        this.urlShort = urlShort;
    }
}
