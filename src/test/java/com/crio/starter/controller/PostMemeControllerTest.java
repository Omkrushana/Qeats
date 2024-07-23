package com.crio.starter.controller;

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
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostMemeControllerTest {

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
    public void testPostMeme_Success() {
        // Valid meme data
        MemeDto memeDto = new MemeDto("John Doe", "http://example.com/meme.jpg", "This is a meme");

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<MemeDto> request = new HttpEntity<>(memeDto, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl, request, String.class);

        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());

        // Check if the meme was saved correctly
        long count = memeRepository.count();
        assertEquals(1, count);
    }

    @Test
    public void testPostMeme_EmptyFields() {
        // Invalid meme data with empty fields
        MemeDto memeDto = new MemeDto("", "", "");

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<MemeDto> request = new HttpEntity<>(memeDto, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl, request, String.class);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("One or more fields are empty", response.getBody());

        // Check if no meme was saved
        long count = memeRepository.count();
        assertEquals(0, count);
    }

    @Test
    public void testPostMeme_PartialEmptyFields() {
        // Invalid meme data with some empty fields
        MemeDto memeDto = new MemeDto("John Doe", "", "This is a meme");

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<MemeDto> request = new HttpEntity<>(memeDto, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl, request, String.class);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("One or more fields are empty", response.getBody());

        // Check if no meme was saved
        long count = memeRepository.count();
        assertEquals(0, count);
    }


    @Test
    public void testPostMeme_DuplicateName() {
        MemeDto memeDto1 = new MemeDto("Duplicate Name", "http://example.com/meme1.jpg", "This is meme 1");
        MemeDto memeDto2 = new MemeDto("Duplicate Name", "http://example.com/meme2.jpg", "This is meme 2");

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<MemeDto> request1 = new HttpEntity<>(memeDto1, headers);
        HttpEntity<MemeDto> request2 = new HttpEntity<>(memeDto2, headers);

        ResponseEntity<String> response1 = restTemplate.postForEntity(baseUrl, request1, String.class);
        ResponseEntity<String> response2 = restTemplate.postForEntity(baseUrl, request2, String.class);

        assertEquals(201, response1.getStatusCodeValue());
        assertNotNull(response1.getBody());

        assertEquals(409, response2.getStatusCodeValue());
        assertEquals("Meme with the same name already exists", response2.getBody());

        long count = memeRepository.count();
        assertEquals(1, count);
    }
}
