package kz.mircella.blogserver.web.videodto;

import lombok.Value;

@Value
public class VideoDetails {
    private String title;
    private String description;
    private String videoImageUrl;
    private String videoUrl;
    private String createdAt;
    private String authorId;
}
