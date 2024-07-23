package com.crio.starter.controller;

import com.crio.starter.data.MemeEntity;
import com.crio.starter.exchange.MemeDto;
import com.crio.starter.repository.MemeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GetMemeTest100Latest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MemeRepository memeRepository;

    private String baseUrl;

    @BeforeEach
    public void setUp() {
        baseUrl = "http://localhost:" + port + "/memes/";
        memeRepository.deleteAll(); // Clean up before each test
    }

    @Test
    public void testPostAndGetLatestMemes() {
        // Post more than 100 memes
        IntStream.range(0, 105).forEach(i -> {
            MemeDto memeDto = new MemeDto("Meme " + i, "http://example.com/" + i, "Caption " + i);
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<MemeDto> request = new HttpEntity<>(memeDto, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(baseUrl, request, String.class);
            assertEquals(201, response.getStatusCodeValue());
            assertNotNull(response.getBody());
        });

        // Fetch the latest 100 memes
        ResponseEntity<MemeEntity[]> response = restTemplate.exchange(
                baseUrl,
                HttpMethod.GET,
                null,
                MemeEntity[].class
        );

        assertEquals(200, response.getStatusCodeValue());
        MemeEntity[] memes = response.getBody();
        assertNotNull(memes);
        assertEquals(100, memes.length);
        assertEquals("Meme 104", memes[0].getName()); // Latest meme should be first
        assertEquals("Meme 5", memes[99].getName()); // 100th latest meme should be last
    }
}
