package kz.mircella.blogserver.web.videodto;

import lombok.Value;

@Value
public class VideoItemDto {
    private String videoTitle;
    private String createdAt;
}
