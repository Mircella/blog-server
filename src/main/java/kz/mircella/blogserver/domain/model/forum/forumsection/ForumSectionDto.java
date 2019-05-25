package kz.mircella.blogserver.domain.model.forum.forumsection;

import kz.mircella.blogserver.domain.model.forum.forumtopic.ForumTopicDetails;
import lombok.Value;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Value
public class ForumSectionDto {
    private String title;
    private LocalDateTime createdAt;
    private String authorId;

    @Size(max = 3)
    private List<ForumTopicDetails> forumTopics;
}
