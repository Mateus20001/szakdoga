package com.szakdoga.backend.news.controllers;


import com.szakdoga.backend.news.models.NewsEntity;
import com.szakdoga.backend.news.services.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    private final NewsService newsService;

    @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    // Get all news articles
    @GetMapping
    public ResponseEntity<List<NewsEntity>> getAllNews() {
        List<NewsEntity> news = newsService.getAllNews();
        return new ResponseEntity<>(news, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<NewsEntity> getNewsById(@PathVariable long id) {
        Optional<NewsEntity> news = newsService.getNewsById(id);
        return news.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<NewsEntity> createNews(@RequestParam("file") MultipartFile file,
                                                 @RequestParam("title") String title,
                                                 @RequestParam("shortDesc") String shortDesc,
                                                 @RequestParam("longDesc") String longDesc) throws IOException {

        NewsEntity newsEntity = new NewsEntity();
        newsEntity.setTitle(title);
        newsEntity.setShortDesc(shortDesc);
        newsEntity.setLongDesc(longDesc);

        // Convert the uploaded file to byte array and set it to the entity
        byte[] imageBytes = file.getBytes();
        newsEntity.setImage(imageBytes);

        NewsEntity createdNews = newsService.saveNews(newsEntity);
        return new ResponseEntity<>(createdNews, HttpStatus.CREATED);
    }

    // Update an existing news article with image
    @PutMapping("/{id}")
    public ResponseEntity<NewsEntity> updateNews(@PathVariable long id,
                                                 @RequestParam("file") MultipartFile file,
                                                 @RequestParam("title") String title,
                                                 @RequestParam("shortDesc") String shortDesc,
                                                 @RequestParam("longDesc") String longDesc) throws IOException {
        if (newsService.getNewsById(id).isPresent()) {
            NewsEntity newsEntity = new NewsEntity();
            newsEntity.setId(id);
            newsEntity.setTitle(title);
            newsEntity.setShortDesc(shortDesc);
            newsEntity.setLongDesc(longDesc);

            // Convert the uploaded file to byte array and set it to the entity
            byte[] imageBytes = file.getBytes();
            newsEntity.setImage(imageBytes);

            NewsEntity updatedNews = newsService.saveNews(newsEntity);
            return new ResponseEntity<>(updatedNews, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a news article by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNews(@PathVariable long id) {
        if (newsService.getNewsById(id).isPresent()) {
            newsService.deleteNews(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable long id) {
        Optional<NewsEntity> news = newsService.getNewsById(id);
        if (news.isPresent() && news.get().getImage() != null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)  // Or the image's actual MIME type
                    .body(news.get().getImage());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
