package kz.mircella.blogserver.repository;

import kz.mircella.blogserver.domain.model.forum.forumtopic.ForumTopic;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface ForumTopicRepository extends ReactiveMongoRepository<ForumTopic, String> {
    Mono<ForumTopic> findByTitle(String title);
}
