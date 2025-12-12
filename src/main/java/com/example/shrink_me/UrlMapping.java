package com.example.shrink_me;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "url_mapping")
public class UrlMapping {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "long_url")
    private String longUrl;

    @Column(name = "short_key")
    private String shortKey;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "clicks")
    private int clicks;

    public UrlMapping() {
    }

    public UrlMapping(String longUrl, String shortKey) {
        this.longUrl = longUrl;
        this.shortKey = shortKey;
    }

    public UrlMapping(String longUrl, String shortKey, int ttlMinutes) {
        this.longUrl = longUrl;
        this.shortKey = shortKey;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    public String getShortKey() {
        return shortKey;
    }

    public void setShortKey(String shortKey) {
        this.shortKey = shortKey;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getClicks() {
        return clicks;
    }

    public void setClicks(int clicks) {
        this.clicks = clicks;
    }

    @Override
    public String toString() {
        return "UrlMapping{" +
                "id=" + id +
                ", longUrl='" + longUrl + '\'' +
                ", shortKey='" + shortKey + '\'' +
                ", createdAt=" + createdAt +
                ", clicks=" + clicks +
                '}';
    }
}