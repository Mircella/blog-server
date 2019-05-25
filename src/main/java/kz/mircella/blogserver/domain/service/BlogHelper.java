package kz.mircella.blogserver.domain.service;

import kz.mircella.blogserver.domain.model.BlogImage;
import kz.mircella.blogserver.domain.model.blogpost.BlogPost;
import kz.mircella.blogserver.domain.model.blogpost.BlogPostDetails;
import kz.mircella.blogserver.domain.model.blogpost.BlogPostDto;
import kz.mircella.blogserver.domain.model.user.User;
import kz.mircella.blogserver.web.blogdto.BlogPostCreateDto;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

class BlogHelper {

    private final Clock clock;

    BlogHelper() {
        this.clock = Clock.systemDefaultZone();
    }

    BlogPostDto mapToBlogPostDto(BlogPost blogPost) {
        List<String> imageIds = blogPost.getBlogImages().stream().map(BlogImage::getId).collect(Collectors.toList());
        return new BlogPostDto(blogPost.getTitle(), blogPost.getCreatedAt(), blogPost.getAuthorId(), imageIds);
    }

    BlogPostDetails mapToBlogPostDetails(BlogPost blogPost) {
        List<String> imageIds = blogPost.getBlogImages().stream().map(BlogImage::getId).collect(Collectors.toList());
        return new BlogPostDetails(blogPost.getTitle(), blogPost.getContent(), blogPost.getCreatedAt(), blogPost.getAuthorId(), imageIds);
    }

    BlogPost newBlogPost(BlogPostCreateDto blogPostCreateDto, User user) {
        return BlogPost.create(
                blogPostCreateDto.getTitle(),
                blogPostCreateDto.getContent(),
                LocalDateTime.now(clock),
                LocalDateTime.now(clock),
                user);
    }
}
