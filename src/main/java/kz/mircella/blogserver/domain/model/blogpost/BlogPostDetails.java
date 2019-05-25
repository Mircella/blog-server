package kz.mircella.blogserver.domain.model.blogpost;

import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;

@Value
public class BlogPostDetails {
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private String authorId;
    private List<String> imageIds;
}
