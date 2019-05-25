package kz.mircella.blogserver.domain.model.blogpost;

import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;

@Value
public class BlogPostDto {
    private String title;
    private LocalDateTime createdAt;
    private String authorId;
    private List<String> imageIds;
}
