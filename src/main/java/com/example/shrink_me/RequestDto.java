package com.example.shrink_me;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

public class RequestDto {

    @Pattern(regexp = "^(http|https)://.*$", message = "Invalid URL format")
    private String url;

    @Min(value = 1, message = "ttlMinutes must be at least 1")
    @Max(value = 525600, message = "ttlMinutes is too large")
    private Integer ttlMinutes;

    public RequestDto() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getTtlMinutes() {
        return ttlMinutes;
    }

    public void setTtlMinutes(Integer ttlMinutes) {
        this.ttlMinutes = ttlMinutes;
    }
}