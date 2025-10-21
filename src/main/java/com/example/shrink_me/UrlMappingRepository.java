package com.example.shrink_me;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long> {

    Optional<UrlMapping> findByLongUrl(String longUrl);

    Optional<UrlMapping> findByShortKey(String shortKey);

    @Modifying
    @Transactional
    @Query("DELETE FROM UrlMapping u where u.expiryDate < :now")
    void deleteExpiredUrls(@Param("now") LocalDateTime now);
}
