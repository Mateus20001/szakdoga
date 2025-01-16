package com.szakdoga.backend.news.services;

import com.szakdoga.backend.news.models.NewsEntity;
import com.szakdoga.backend.news.repositories.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NewsService {
    private final NewsRepository newsRepository;

    @Autowired
    public NewsService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    // Get all news articles
    public List<NewsEntity> getAllNews() {
        return newsRepository.findAll();
    }

    // Get a specific news article by ID
    public Optional<NewsEntity> getNewsById(long id) {
        return newsRepository.findById(id);
    }

    // Save a new news article or update an existing one
    public NewsEntity saveNews(NewsEntity newsEntity) {
        return newsRepository.save(newsEntity);
    }

    // Delete a news article by ID
    public void deleteNews(long id) {
        newsRepository.deleteById(id);
    }
}
