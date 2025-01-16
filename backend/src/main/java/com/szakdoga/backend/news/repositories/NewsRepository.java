package com.szakdoga.backend.news.repositories;

import com.szakdoga.backend.news.models.NewsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<NewsEntity, Long> {
}
