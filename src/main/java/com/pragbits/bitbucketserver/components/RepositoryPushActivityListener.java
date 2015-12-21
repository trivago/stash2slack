package com.pragbits.bitbucketserver.components;

import com.atlassian.bitbucket.commit.*;
import com.atlassian.bitbucket.util.PageRequestImpl;
import com.atlassian.event.api.EventListener;
import com.atlassian.bitbucket.event.repository.RepositoryPushEvent;
import com.atlassian.bitbucket.nav.NavBuilder;
import com.atlassian.bitbucket.repository.RefChange;
import com.atlassian.bitbucket.repository.RefChangeType;
import com.atlassian.bitbucket.repository.Repository;
import com.atlassian.bitbucket.util.Page;
import com.atlassian.bitbucket.util.PageRequest;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.pragbits.bitbucketserver.ColorCode;
import com.pragbits.bitbucketserver.SlackGlobalSettingsService;
import com.pragbits.bitbucketserver.SlackSettings;
import com.pragbits.bitbucketserver.SlackSettingsService;
import com.pragbits.bitbucketserver.tools.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class RepositoryPushActivityListener {
    private static final Logger log = LoggerFactory.getLogger(RepositoryPushActivityListener.class);

    private final SlackGlobalSettingsService slackGlobalSettingsService;
    private final SlackSettingsService slackSettingsService;
    private final CommitService commitService;
    private final NavBuilder navBuilder;
    private final SlackNotifier slackNotifier;
    private final Gson gson = new Gson();

    public RepositoryPushActivityListener(SlackGlobalSettingsService slackGlobalSettingsService,
                                          SlackSettingsService slackSettingsService,
                                          CommitService commitService,
                                          NavBuilder navBuilder,
                                          SlackNotifier slackNotifier) {
        this.slackGlobalSettingsService = slackGlobalSettingsService;
        this.slackSettingsService = slackSettingsService;
        this.commitService = commitService;
        this.navBuilder = navBuilder;
        this.slackNotifier = slackNotifier;
    }

    @EventListener
    public void NotifySlackChannel(RepositoryPushEvent event) {
        // find out if notification is enabled for this repo
        Repository repository = event.getRepository();
        SlackSettings slackSettings = slackSettingsService.getSlackSettings(repository);
        String globalHookUrl = slackGlobalSettingsService.getWebHookUrl();

        SettingsSelector settingsSelector = new SettingsSelector(slackSettingsService,  slackGlobalSettingsService, repository);
        SlackSettings resolvedSlackSettings = settingsSelector.getResolvedSlackSettings();

        if (resolvedSlackSettings.isSlackNotificationsEnabledForPush()) {
            String localHookUrl = slackSettings.getSlackWebHookUrl();
            WebHookSelector hookSelector = new WebHookSelector(globalHookUrl, localHookUrl);
            ChannelSelector channelSelector = new ChannelSelector(slackGlobalSettingsService.getChannelName(), slackSettings.getSlackChannelName());

            if (!hookSelector.isHookValid()) {
                log.error("There is no valid configured Web hook url! Reason: " + hookSelector.getProblem());
                return;
            }

            if (repository.isFork() && !resolvedSlackSettings.isSlackNotificationsEnabledForPersonal()) {
                // simply return silently when we don't want forks to get notifications unless they're explicitly enabled
                return;
            }

            String repoName = repository.getSlug();
            String projectName = repository.getProject().getKey();

            String repoPath = projectName + "/" + event.getRepository().getName();

            for (RefChange refChange : event.getRefChanges()) {
                String text;
                String ref = refChange.getRef().getId();
                NavBuilder.Repo repoUrlBuilder = navBuilder
                        .project(projectName)
                        .repo(repoName);
                String url = repoUrlBuilder
                        .commits()
                        .until(refChange.getRef().getId())
                        .buildAbsolute();

                List<Commit> myCommits = new LinkedList<Commit>();

                boolean isNewRef = refChange.getFromHash().equalsIgnoreCase("0000000000000000000000000000000000000000");
                boolean isDeleted = refChange.getToHash().equalsIgnoreCase("0000000000000000000000000000000000000000")
                    && refChange.getType() == RefChangeType.DELETE;
                if (isDeleted) {
                    // issue#4: if type is "DELETE" and toHash is all zero then this is a branch delete
                    if (ref.indexOf("refs/tags") >= 0) {
                        text = String.format("Tag `%s` deleted from repository <%s|`%s`>.",
                                ref.replace("refs/tags/", ""),
                                repoUrlBuilder.buildAbsolute(),
                                repoPath);
                    } else {
                        text = String.format("Branch `%s` deleted from repository <%s|`%s`>.",
                                ref.replace("refs/heads/", ""),
                                repoUrlBuilder.buildAbsolute(),
                                repoPath);
                    }
                } else if (isNewRef) {
                    // issue#3 if fromHash is all zero (meaning the beginning of everything, probably), then this push is probably
                    // a new branch or tag, and we want only to display the latest commit, not the entire history

                    if (ref.indexOf("refs/tags") >= 0) {
                        text = String.format("Tag <%s|`%s`> pushed on <%s|`%s`>. See <%s|commit list>.",
                                url,
                                ref.replace("refs/tags/", ""),
                                repoUrlBuilder.buildAbsolute(),
                                repoPath,
                                url
                                );
                    } else {
                        text = String.format("Branch <%s|`%s`> pushed on <%s|`%s`>. See <%s|commit list>.",
                                url,
                                ref.replace("refs/heads/", ""),
                                repoUrlBuilder.buildAbsolute(),
                                repoPath,
                                url);
                    }
                } else {
                    PageRequest pRequest = new PageRequestImpl(0, PageRequest.MAX_PAGE_LIMIT);
                    CommitsBetweenRequest commitsBetween = new CommitsBetweenRequest.Builder(repository).exclude(refChange.getFromHash()).include(refChange.getToHash()).build();
                    Page<Commit> commitList = commitService.getCommitsBetween(commitsBetween, pRequest);
                    myCommits.addAll(Lists.newArrayList(commitList.getValues()));

                    int commitCount = myCommits.size();
                    String commitStr = commitCount == 1 ? "commit" : "commits";

                    String branch = ref.replace("refs/heads/", "");
                    text = String.format("Push on <%s|`%s`> branch <%s|`%s`> by `%s <%s>` (%d %s). See <%s|commit list>.",
                            repoUrlBuilder.buildAbsolute(),
                            repoPath,
                            url,
                            branch,
                            event.getUser() != null ? event.getUser().getDisplayName() : "unknown user",
                            event.getUser() != null ? event.getUser().getEmailAddress() : "unknown email",
                            commitCount, commitStr,
                            url);
                }

                // Figure out what type of change this is:

                SlackPayload payload = new SlackPayload();

                payload.setText(text);
                payload.setMrkdwn(true);

                switch (resolvedSlackSettings.getNotificationLevel()) {
                    case COMPACT:
                        compactCommitLog(event, refChange, payload, repoUrlBuilder, myCommits);
                        break;
                    case VERBOSE:
                        verboseCommitLog(event, refChange, payload, repoUrlBuilder, text, myCommits);
                        break;
                    case MINIMAL:
                    default:
                        break;
                }

                // slackSettings.getSlackChannelName might be:
                // - empty
                // - comma separated list of channel names, eg: #mych1, #mych2, #mych3

                if (channelSelector.getSelectedChannel().isEmpty()) {
                    slackNotifier.SendSlackNotification(hookSelector.getSelectedHook(), gson.toJson(payload));
                } else {
                    // send message to multiple channels
                    List<String> channels = Arrays.asList(channelSelector.getSelectedChannel().split("\\s*,\\s*"));
                    for (String channel: channels) {
                        payload.setChannel(channel.trim());
                        slackNotifier.SendSlackNotification(hookSelector.getSelectedHook(), gson.toJson(payload));
                    }
                }
            }
        }
    }

    private void compactCommitLog(RepositoryPushEvent event, RefChange refChange, SlackPayload payload, NavBuilder.Repo urlBuilder, List<Commit> myCommits) {
        if (myCommits.size() == 0) {
            // If there are no commits, no reason to add anything
        }
        SlackAttachment commits = new SlackAttachment();
        commits.setColor(ColorCode.GRAY.getCode());
        // Since the branch is now in the main commit line, title is not needed
        //commits.setTitle(String.format("[%s:%s]", event.getRepository().getName(), refChange.getRefId().replace("refs/heads", "")));
        StringBuilder attachmentFallback = new StringBuilder();
        StringBuilder commitListBlock = new StringBuilder();
        for (Commit c : myCommits) {
            String commitUrl = urlBuilder.commit(c.getId()).buildAbsolute();
            String firstCommitMessageLine = c.getMessage().split("\n")[0];

            // Note that we changed this to put everything in one attachment because otherwise it
            // doesn't get collapsed in slack (the see more... doesn't appear)
            commitListBlock.append(String.format("<%s|`%s`>: %s - _%s_\n",
                    commitUrl, c.getDisplayId(), firstCommitMessageLine, c.getAuthor().getName()));

            attachmentFallback.append(String.format("%s: %s\n", c.getDisplayId(), firstCommitMessageLine));
        }
        commits.setText(commitListBlock.toString());
        commits.setFallback(attachmentFallback.toString());

        payload.addAttachment(commits);
    }

    private void verboseCommitLog(RepositoryPushEvent event, RefChange refChange, SlackPayload payload, NavBuilder.Repo urlBuilder, String text, List<Commit> myCommits) {
        for (Commit c : myCommits) {
            SlackAttachment attachment = new SlackAttachment();
            attachment.setFallback(text);
            attachment.setColor(ColorCode.GRAY.getCode());
            SlackAttachmentField field = new SlackAttachmentField();

            attachment.setTitle(String.format("[%s:%s] - %s", event.getRepository().getName(), refChange.getRefId().replace("refs/heads", ""), c.getId()));
            attachment.setTitle_link(urlBuilder.commit(c.getId()).buildAbsolute());

            field.setTitle("comment");
            field.setValue(c.getMessage());
            field.setShort(false);
            attachment.addField(field);
            payload.addAttachment(attachment);
        }
    }
}
