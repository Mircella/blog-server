package kz.mircella.blogserver.repository;

import kz.mircella.blogserver.domain.model.forum.topicpost.TopicPost;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface TopicPostRepository extends ReactiveMongoRepository<TopicPost, String> {
}
