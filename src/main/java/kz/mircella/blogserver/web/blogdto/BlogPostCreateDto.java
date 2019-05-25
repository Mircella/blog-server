package kz.mircella.blogserver.web.blogdto;

import lombok.Value;

@Value
public class BlogPostCreateDto {
    private String title;
    private String content;
}
