package com.devrace.domain.commit.component;

import com.devrace.global.exception.CustomException;
import com.devrace.global.exception.ErrorCode;
import org.kohsuke.github.GHEvent;
import org.kohsuke.github.GHEventInfo;
import org.kohsuke.github.GHEventPayload;
import org.kohsuke.github.GitHubBuilder;
import org.kohsuke.github.GitHub;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

@Component
public class GithubApiClient {

    private final GitHub github;

    public GithubApiClient(@Value("${github.oauth.token}") String oauthToken) {
        try {
            this.github = new GitHubBuilder().withOAuthToken(oauthToken).build();

            github.checkApiUrlValidity();

        } catch (Exception e) {
            throw new CustomException(ErrorCode.FAIL_TO_CONNECT_GITHUB);
        }
    }

    public long getCommitCountInperiod (String githubUsername, ZonedDateTime since, ZonedDateTime until) throws Exception {
        return github.getUser(githubUsername)
                .listEvents()
                .toList()
                .stream()
                .filter(this::isPushEvent)
                .filter(event -> isWithinPeriod(event, since, until))
                .mapToLong(this::getCommitCountFromEvent)
                .sum();
    }

    private boolean isPushEvent(GHEventInfo event) {
        return event.getType() == GHEvent.PUSH;
    }

    private boolean isWithinPeriod(GHEventInfo event, ZonedDateTime since, ZonedDateTime until) {
        ZonedDateTime eventTime = ZonedDateTime.ofInstant(event.getCreatedAt().toInstant(), ZoneOffset.UTC);
        return (eventTime.isAfter(since) && eventTime.isBefore(until));
    }

    private long getCommitCountFromEvent(GHEventInfo event) {
        try {
            if (isPushEvent(event)) {
                GHEventPayload.Push pushPayload = event.getPayload(GHEventPayload.Push.class);
                List<GHEventPayload.Push.PushCommit> commits = pushPayload.getCommits();
                return commits.size();
            }
        } catch (Exception e) {
            throw new CustomException(ErrorCode.CAN_NOT_EXTRACTION_COMMIT_COUNT);
        }
        return 0;
    }
}
