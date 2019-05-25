package kz.mircella.blogserver.repository;

import kz.mircella.blogserver.domain.model.video.Video;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface VideoRepository extends ReactiveMongoRepository<Video, String> {
    Mono<Video> findByTitle(String title);
}
