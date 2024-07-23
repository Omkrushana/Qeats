package com.crio.starter.controller;

import com.crio.starter.data.MemeEntity;
import com.crio.starter.exchange.MemeDto;
import com.crio.starter.service.MemeService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/memes")
public class MemeController {
    @Autowired
    private MemeService memeService;

    @PostMapping("/")
    public ResponseEntity<?> postMeme(@Valid @RequestBody MemeDto memeDto) {
        System.out.println(memeDto);
        if (memeDto.getName() == null || memeDto.getName().isEmpty() || memeDto.getUrl() == null || memeDto.getUrl().isEmpty() || memeDto.getCaption() == null || memeDto.getCaption().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("One or more fields are empty");
          } 

        try {
            MemeEntity id = memeService.saveMeme(memeDto);
            Map<String, String> response = new HashMap<>();
            response.put("id", id.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred " + e);
        }
    }

    // @GetMapping("/")
    // public ResponseEntity<List<MemeEntity>> getMemes() {
    // List<MemeEntity> memes = memeService.getLatestMemes();
    // return ResponseEntity.ok(memes);
    // }

    @GetMapping("/{id}")
    public MemeDto getMemeById(@PathVariable String id) {
        return memeService.getMemeById(id);
    }

    @GetMapping("/")
    public ResponseEntity<List<MemeEntity>> getLatestMemes() {
        List<MemeEntity> memes = memeService.getLatestMemes();
        return ResponseEntity.ok(memes);
    }
}
