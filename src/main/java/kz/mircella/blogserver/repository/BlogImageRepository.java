package kz.mircella.blogserver.repository;

import kz.mircella.blogserver.domain.model.BlogImage;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface BlogImageRepository extends ReactiveMongoRepository<BlogImage, String> {
}
