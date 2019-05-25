package kz.mircella.blogserver.web.videodto;

import lombok.Value;

@Value
public class VideoCreateDto {
    private String title;
    private String description;
    private String videoImageUrl;
    private String videoUrl;
}
