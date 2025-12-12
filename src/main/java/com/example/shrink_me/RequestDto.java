package com.example.shrink_me;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class RequestDto {

    @Pattern(regexp = "^(http|https)://.*$", message = "Invalid URL format, must start with http:// or https://")
    @NotBlank(message = "URL cannot be blank")
    private String url;

    public RequestDto() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}