package kz.mircella.blogserver.domain.service;

import kz.mircella.blogserver.domain.model.BlogImage;
import kz.mircella.blogserver.domain.model.blogpost.BlogPost;
import kz.mircella.blogserver.domain.model.blogpost.BlogPostDetails;
import kz.mircella.blogserver.domain.model.blogpost.BlogPostDto;
import kz.mircella.blogserver.repository.BlogImageContentStore;
import kz.mircella.blogserver.repository.BlogImageRepository;
import kz.mircella.blogserver.repository.BlogPostRepository;
import kz.mircella.blogserver.web.blogdto.BlogPostCreateDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.InputStream;
import java.util.function.Function;

@Service
@Slf4j
public class BlogPostService {
    private final UserProfileService userProfileService;
    private final BlogPostRepository blogPostRepository;
    private final BlogImageRepository blogImageRepository;
    private final BlogImageContentStore blogImageContentStore;
    private final BlogHelper blogHelper;

    public BlogPostService(BlogPostRepository blogPostRepository,
                           UserProfileService userProfileService,
                           BlogImageRepository blogImageRepository,
                           BlogImageContentStore blogImageContentStore) {
        this.blogPostRepository = blogPostRepository;
        this.userProfileService = userProfileService;
        this.blogImageRepository = blogImageRepository;
        this.blogImageContentStore = blogImageContentStore;
        this.blogHelper = new BlogHelper();

    }

    public Flux<BlogPostDto> getAllBlogPosts() {
        return blogPostRepository.findAll().map(blogHelper::mapToBlogPostDto);
    }

    public Mono<String> saveNewBlogPost(String authorId, BlogPostCreateDto blogPostCreateDto) {
        return userProfileService.getUser(authorId)
                .flatMap(user -> {
                    BlogPost blogPost = blogHelper.newBlogPost(blogPostCreateDto, user);
                    return blogPostRepository.save(blogPost).flatMap(savedBlogPost -> {
                        user.getBlogPosts().add(savedBlogPost);
                        return userProfileService.updateUser(user).map(updatedUser -> blogPost.getId());
                    });
                }).map(Function.identity());
    }

    public Mono<BlogPostDetails> getBlogPostByTitle(String title) {
        return blogPostRepository.findByTitle(title).map(blogHelper::mapToBlogPostDetails);
    }

    public Mono<String> saveBlogPostImage(String title, String imageContentType, InputStream imageContent) {
        return blogPostRepository.findByTitle(title)
                .flatMap(foundBlogPost -> {
                    BlogImage blogImage = new BlogImage(String.format("Image of %s", title), imageContentType, foundBlogPost);
                    blogImageContentStore.setContent(blogImage, imageContent);
                    return blogImageRepository.save(blogImage).flatMap(savedImage -> {
                        log.info("Image for BlogPost \"{}\" was saved", savedImage.getBlogPost().getTitle());
                        foundBlogPost.getBlogImages().add(savedImage);
                        return blogPostRepository.save(foundBlogPost).map(updatedBlogPost -> savedImage.getId());
                    });
                }).map(Function.identity());
    }

    public Mono<InputStream> getBlogImageByTitleAndId(String imageId) {
        return blogImageRepository.findById(imageId).map(blogImageContentStore::getContent);
    }

    public void clearAll() {
        System.out.println("Started cleaning");
        blogImageRepository.findAll().subscribe(
                blogImageContentStore::unsetContent,
                er -> log.info("Error {0}", er),
                () -> log.info("Completed"));
    }
}
