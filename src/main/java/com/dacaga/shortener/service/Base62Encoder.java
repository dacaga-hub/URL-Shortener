package com.dacaga.shortener.service;


public class Base62Encoder{

    private static final String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public String encode(Long id){

        StringBuilder sb = new StringBuilder();

        if(id == 0){ return "0";}
        if(id < 0)throw new IllegalArgumentException();

        do {
            int r = (int)(id % 62);
            sb.append(ALPHABET.charAt(r));
            id /= 62;
        } while(id > 0);

        sb.reverse();

        return sb.toString();
    }
}