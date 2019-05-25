package kz.mircella.blogserver.web;

import kz.mircella.blogserver.domain.model.blogpost.BlogPostDetails;
import kz.mircella.blogserver.domain.model.blogpost.BlogPostDto;
import kz.mircella.blogserver.domain.service.BlogPostService;
import kz.mircella.blogserver.web.blogdto.BlogPostCreateDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.InputStream;
import java.util.Map;

@Slf4j
@Component
public class BlogPostHandler {

    private final BlogPostService blogPostService;

    public BlogPostHandler(BlogPostService blogPostService) {
        this.blogPostService = blogPostService;
    }

    Mono<ServerResponse> saveNewBlogPost(ServerRequest request) {
        String userId = request.pathVariable("userId");
        Mono<String> responseBody = request
                .bodyToMono(BlogPostCreateDto.class)
                .flatMap(blogPostCreateDto -> blogPostService.saveNewBlogPost(userId, blogPostCreateDto));
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(responseBody, String.class);
    }

    Mono<ServerResponse> getAllBlogPosts() {
        Flux<BlogPostDto> blogPostDtoFlux = blogPostService.getAllBlogPosts();
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(blogPostDtoFlux, BlogPostDto.class);
    }


    Mono<ServerResponse> getBlogPostByTitle(ServerRequest serverRequest) {
        String title = serverRequest.pathVariable("title");
        Mono<BlogPostDetails> blogPostDetails = blogPostService.getBlogPostByTitle(title);
        return ServerResponse.ok().contentType(MediaType.APPLICATION_STREAM_JSON).body(blogPostDetails, BlogPostDetails.class);
    }

    Mono<ServerResponse> uploadBlogPostImages(ServerRequest serverRequest) {
        String title = serverRequest.pathVariable("title");
        return serverRequest.body(BodyExtractors.toMultipartData()).flatMap(parts -> {
            Map<String, Part> partMap = parts.toSingleValueMap();
            FilePart filePart = (FilePart) partMap.get("file");
            log.info("Filename {}", filePart.filename());
            Flux<DataBuffer> dataBufferFlux = filePart.content();
            Mono<String> blogPostImageId = dataBufferFlux.collect(InputStreamCollector::new, (t, b) -> t.collectInputStream(b.asInputStream())).flatMap(inputStreamCollector -> {
                InputStream inputStream = inputStreamCollector.getInputStream();
                return blogPostService.saveBlogPostImage(title, MediaType.IMAGE_JPEG_VALUE, inputStream);
            });
            return ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_STREAM_JSON)
                    .body(blogPostImageId, String.class);
        });
    }

    Mono<ServerResponse> getBlogPostImageById(ServerRequest serverRequest) {
        String imageId = serverRequest.pathVariable("imageId");
        Mono<InputStream> blogImage = blogPostService.getBlogImageByTitleAndId(imageId);
        Mono<InputStreamResource> blogImagesStream = blogImage.map(InputStreamResource::new);
        return ServerResponse.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(blogImagesStream, InputStreamResource.class);
    }

    public Mono<ServerResponse> clear() {
        blogPostService.clearAll();
        return ServerResponse.accepted().build();
    }
}
