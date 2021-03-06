package com.chockwa.beauty.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UploadResponse {
    private String name;
    private String url;
    private String originUrl;
    private String thumbUrl;
    private String originThumbUrl;
}
