package kz.mircella.blogserver.domain.service;

import kz.mircella.blogserver.domain.model.forum.forumsection.ForumSection;
import kz.mircella.blogserver.domain.model.forum.forumtopic.ForumTopic;
import kz.mircella.blogserver.domain.model.forum.forumtopic.ForumTopicDetails;
import kz.mircella.blogserver.domain.model.forum.forumtopic.ForumTopicDto;
import kz.mircella.blogserver.domain.model.forum.topicpost.TopicPost;
import kz.mircella.blogserver.domain.model.forum.topicpost.TopicPostDto;
import kz.mircella.blogserver.domain.model.user.User;
import kz.mircella.blogserver.web.forumdto.ForumSectionCreateDto;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;

class ForumHelper {
    private final DateTimeFormatter formatter;
    private final Clock clock;

    ForumHelper() {
        this.formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.SHORT);
        this.clock = Clock.systemDefaultZone();
    }

    ForumTopicDetails mapToForumTopicDetails(ForumTopic forumTopic) {
        return new ForumTopicDetails(
                forumTopic.getTitle(),
                getLastChangeDateTimeDescription(forumTopic),
                getLastChangeAuthorLoginDescription(forumTopic));
    }

    ForumTopicDto mapToForumTopicDto(ForumTopic forumTopic) {
        return new ForumTopicDto(
                forumTopic.getTitle(),
                getCreationAtDescription(forumTopic),
                getCreatorDescription(forumTopic),
                forumTopic.getForumSectionTitle(),
                forumTopic.getTopicPosts().stream().map(this::mapToTopicPostDto).collect(Collectors.toList()));
    }

    ForumSection newForumSection(ForumSectionCreateDto forumSectionCreateDto, User user) {
        return ForumSection.create(
                forumSectionCreateDto.getTitle(),
                user,
                now(clock));
    }

    ForumTopic newForumTopic(String topicTitle, ForumSection foundForumSection, User foundUser) {
        return ForumTopic.create(
                topicTitle,
                now(clock),
                foundUser,
                foundForumSection);
    }

    TopicPost newTopicPost(String body, ForumTopic foundForumTopic, User foundUser) {
        return TopicPost.create(body, LocalDateTime.now(clock), foundUser, foundForumTopic);
    }

    private String getLastChangeDateTimeDescription(ForumTopic forumTopic) {
        return forumTopic.getLastTopicPostDateTime()
                .map(it -> String.format("Last posted at %s", formatter.format(it)))
                .orElseGet(() -> String.format("Created at %s", formatter.format(forumTopic.getCreatedAt())));
    }

    private String getCreationAtDescription(ForumTopic forumTopic) {
        return String.format("Was created at %s", formatter.format(forumTopic.getCreatedAt()));
    }

    private String getCreatorDescription(ForumTopic forumTopic) {
        return String.format("Was created by %s", forumTopic.getCreatorLogin());
    }

    private String getLastChangeAuthorLoginDescription(ForumTopic forumTopic) {
        return forumTopic.getLastTopicPostAuthorLogin()
                .map(it -> String.format("Last posted by %s", it))
                .orElseGet(() -> String.format("Created by %s", forumTopic.getCreatorLogin()));
    }

    private TopicPostDto mapToTopicPostDto(TopicPost topicPost) {
        return new TopicPostDto(topicPost.getBody(), getCreatedAtDescription(topicPost), getAuthorDescription(topicPost));
    }

    private String getCreatedAtDescription(TopicPost topicPost) {
        return String.format("Was created at %s", formatter.format(topicPost.getCreatedAt()));
    }

    private String getAuthorDescription(TopicPost topicPost) {
        return String.format("Was created by %s", topicPost.getAuthorLogin());
    }
}
