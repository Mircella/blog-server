package kz.mircella.blogserver.domain.service;

import kz.mircella.blogserver.domain.model.forum.forumsection.ForumSection;
import kz.mircella.blogserver.domain.model.forum.forumsection.ForumSectionDto;
import kz.mircella.blogserver.domain.model.forum.forumtopic.ForumTopic;
import kz.mircella.blogserver.domain.model.forum.forumtopic.ForumTopicDetails;
import kz.mircella.blogserver.domain.model.forum.forumtopic.ForumTopicDto;
import kz.mircella.blogserver.domain.model.forum.topicpost.TopicPost;
import kz.mircella.blogserver.repository.ForumSectionRepository;
import kz.mircella.blogserver.repository.ForumTopicRepository;
import kz.mircella.blogserver.repository.TopicPostRepository;
import kz.mircella.blogserver.web.forumdto.ForumSectionCreateDto;
import kz.mircella.blogserver.web.forumdto.ForumTopicCreateDto;
import kz.mircella.blogserver.web.forumdto.TopicPostCreateDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ForumService {
    private final UserProfileService userProfileService;
    private final ForumTopicRepository forumTopicRepository;
    private final ForumSectionRepository forumSectionRepository;
    private final TopicPostRepository topicPostRepository;
    private final ForumHelper forumHelper;

    public ForumService(UserProfileService userProfileService,
                        ForumTopicRepository forumTopicRepository,
                        ForumSectionRepository forumSectionRepository,
                        TopicPostRepository topicPostRepository) {
        this.userProfileService = userProfileService;
        this.forumTopicRepository = forumTopicRepository;
        this.forumSectionRepository = forumSectionRepository;
        this.topicPostRepository = topicPostRepository;
        this.forumHelper = new ForumHelper();
    }

    public Flux<ForumSectionDto> getAllForumSections() {
        return forumSectionRepository.findAll()
                .map(forumSection -> new ForumSectionDto(
                        forumSection.getTitle(),
                        forumSection.getCreatedAt(),
                        forumSection.getAuthorId(),
                        getForumTopicDetails(forumSection.getForumTopics())));
    }

    private List<ForumTopicDetails> getForumTopicDetails(List<ForumTopic> forumTopics) {
        return forumTopics.stream()
                .map(forumHelper::mapToForumTopicDetails)
                .collect(Collectors.toList());
    }


    public Mono<String> saveNewForumSection(String authorId, ForumSectionCreateDto forumSectionCreateDto) {
        return userProfileService
                .getUser(authorId)
                .flatMap(user -> forumSectionRepository.save(forumHelper.newForumSection(forumSectionCreateDto, user)))
                .map(ForumSection::getId);
    }

    public Mono<String> saveNewForumTopic(ForumTopicCreateDto forumTopicCreateDto) {
        String authorId = forumTopicCreateDto.getAuthorId();
        String topicTitle = forumTopicCreateDto.getTopicTitle();
        String sectionTitle = forumTopicCreateDto.getForumSectionTitle();
        return getForumSectionByTitle(sectionTitle)
                .flatMap(foundForumSection -> userProfileService.getUser(authorId)
                        .flatMap(foundUser -> {
                            ForumTopic newForumTopic = forumHelper.newForumTopic(topicTitle, foundForumSection, foundUser);
                            return forumTopicRepository.save(newForumTopic)
                                    .flatMap(savedForumTopic -> {
                                        foundForumSection.getForumTopics().add(savedForumTopic);
                                        return forumSectionRepository.save(foundForumSection).map(updatedForumSection -> savedForumTopic.getId());
                                    });
                        })).map(Function.identity());
    }


    public Mono<ForumTopicDto> getForumTopicDto(String forumTopicTitle) {
        return getForumTopicByTitle(forumTopicTitle).map(forumHelper::mapToForumTopicDto);
    }

    public Mono<String> saveNewTopicPost(TopicPostCreateDto topicPostCreateDto) {
        String topicTitle = topicPostCreateDto.getForumTopicTitle();
        String authorId = topicPostCreateDto.getAuthorId();
        String body = topicPostCreateDto.getBody();
        return getForumTopicByTitle(topicTitle)
                .flatMap(foundForumTopic -> userProfileService.getUser(authorId)
                        .flatMap(foundUser -> {
                            TopicPost topicPost = forumHelper.newTopicPost(body, foundForumTopic, foundUser);
                            return topicPostRepository.save(topicPost)
                                    .flatMap(savedTopicPost -> {
                                        foundForumTopic.getTopicPosts().add(savedTopicPost);
                                        return forumTopicRepository.save(foundForumTopic).map(updatedForumTopic -> savedTopicPost.getId());
                                    });
                        })).map(Function.identity());
    }

    private Mono<ForumSection> getForumSectionByTitle(String forumSectionTitle) {
        return forumSectionRepository.findByTitle(forumSectionTitle);
    }

    private Mono<ForumTopic> getForumTopicByTitle(String topicTitle) {
        return forumTopicRepository.findByTitle(topicTitle);
    }
}
