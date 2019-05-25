package kz.mircella.blogserver.repository;

import kz.mircella.blogserver.domain.model.forum.forumsection.ForumSection;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface ForumSectionRepository extends ReactiveMongoRepository<ForumSection, String> {
    Mono<ForumSection> findByTitle(String title);

}
