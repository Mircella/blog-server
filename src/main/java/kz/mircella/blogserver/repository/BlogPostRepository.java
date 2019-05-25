package kz.mircella.blogserver.repository;

import kz.mircella.blogserver.domain.model.blogpost.BlogPost;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface BlogPostRepository extends ReactiveMongoRepository<BlogPost, String> {
    Mono<BlogPost> findByTitle(String title);
}
