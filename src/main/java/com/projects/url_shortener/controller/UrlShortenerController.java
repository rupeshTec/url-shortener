package com.projects.url_shortener.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projects.url_shortener.service.UrlShortenerService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UrlShortenerController {
    @Autowired
    private UrlShortenerService service;

    @PostMapping("/shorten")
    public ResponseEntity<String> shortenUrl(@RequestBody Map<String, String> reqMap) {
        String shortUrl = service.generateShortUrl(reqMap.get("longUrl"));
        return ResponseEntity.ok("http://short.ly/" + shortUrl);
    }

    @PostMapping("/redirect")
    public ResponseEntity<Void> redirectToLongUrl(@RequestBody Map<String, String> reqMap, HttpServletResponse response) throws IOException {
        String longUrl = service.getLongUrl(reqMap.get("shortUrl"));
        response.sendRedirect(longUrl);
        return ResponseEntity.status(HttpStatus.FOUND).build();
    }
}
