package com.example.shrink_me;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long> {

    Optional<UrlMapping> findByLongUrl(String longUrl);

    Optional<UrlMapping> findByShortKey(String shortKey);

}
