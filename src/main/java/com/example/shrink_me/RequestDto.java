package com.example.shrink_me;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RequestDto {


    @Pattern(regexp = "^(http|https)://.*$", message = "Invalid URL format")
    private String url;
}
