package kz.mircella.blogserver.domain.model;

import kz.mircella.blogserver.domain.model.blogpost.BlogPost;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.content.commons.annotations.ContentId;
import org.springframework.content.commons.annotations.ContentLength;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@Document
@NoArgsConstructor
@AllArgsConstructor
@TypeAlias("blogimage")
public class BlogImage {
    @Id
    private String id;
    private String title;
    private String mimeType;
    @ContentId
    private String contentId;
    @ContentLength
    private long contentLength;

    @DBRef
    private BlogPost blogPost;

    public BlogImage(String title, String imageMimeType, BlogPost blogPost) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.mimeType = imageMimeType;
        this.blogPost = blogPost;
    }
}
