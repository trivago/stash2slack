package com.pragbits.bitbucketserver.components;

import com.atlassian.event.api.EventListener;
import com.atlassian.bitbucket.comment.Comment;
import com.atlassian.bitbucket.event.pull.PullRequestActivityEvent;
import com.atlassian.bitbucket.event.pull.PullRequestCommentActivityEvent;
import com.atlassian.bitbucket.event.pull.PullRequestRescopeActivityEvent;
import com.atlassian.bitbucket.nav.NavBuilder;
import com.atlassian.bitbucket.pull.PullRequestParticipant;
import com.atlassian.bitbucket.repository.Repository;
import com.atlassian.bitbucket.avatar.AvatarService;
import com.atlassian.bitbucket.avatar.AvatarRequest;
import com.google.gson.Gson;
import com.pragbits.bitbucketserver.*;
import com.pragbits.bitbucketserver.tools.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class PullRequestActivityListener {
    private static final Logger log = LoggerFactory.getLogger(PullRequestActivityListener.class);

    private final SlackGlobalSettingsService slackGlobalSettingsService;
    private final SlackSettingsService slackSettingsService;
    private final NavBuilder navBuilder;
    private final SlackNotifier slackNotifier;
    private final AvatarService avatarService;
    private final AvatarRequest avatarRequest = new AvatarRequest(true, 16, true);
    private final Gson gson = new Gson();

    public PullRequestActivityListener(SlackGlobalSettingsService slackGlobalSettingsService,
                                             SlackSettingsService slackSettingsService,
                                             NavBuilder navBuilder,
                                             SlackNotifier slackNotifier,
                                             AvatarService avatarService) {
        this.slackGlobalSettingsService = slackGlobalSettingsService;
        this.slackSettingsService = slackSettingsService;
        this.navBuilder = navBuilder;
        this.slackNotifier = slackNotifier;
        this.avatarService = avatarService;
    }

    @EventListener
    public void NotifySlackChannel(PullRequestActivityEvent event) {
        // find out if notification is enabled for this repo
        Repository repository = event.getPullRequest().getToRef().getRepository();
        SlackSettings slackSettings = slackSettingsService.getSlackSettings(repository);
        String globalHookUrl = slackGlobalSettingsService.getWebHookUrl();


        SettingsSelector settingsSelector = new SettingsSelector(slackSettingsService,  slackGlobalSettingsService, repository);
        SlackSettings resolvedSlackSettings = settingsSelector.getResolvedSlackSettings();

        if (resolvedSlackSettings.isSlackNotificationsEnabled()) {

            String localHookUrl = resolvedSlackSettings.getSlackWebHookUrl();
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
            long pullRequestId = event.getPullRequest().getId();
            String userName = event.getUser() != null ? event.getUser().getDisplayName() : "unknown user";
            String activity = event.getActivity().getAction().name();
            String avatar = event.getUser() != null ? avatarService.getUrlForPerson(event.getUser(), avatarRequest) : "";

            NotificationLevel resolvedLevel = resolvedSlackSettings.getNotificationPrLevel();

            // Ignore RESCOPED PR events
            if (activity.equalsIgnoreCase("RESCOPED") && event instanceof PullRequestRescopeActivityEvent) {
                return;
            }

            if (activity.equalsIgnoreCase("OPENED") && !resolvedSlackSettings.isSlackNotificationsOpenedEnabled()) {
                return;
            }

            if (activity.equalsIgnoreCase("REOPENED") && !resolvedSlackSettings.isSlackNotificationsReopenedEnabled()) {
                return;
            }

            if (activity.equalsIgnoreCase("UPDATED") && !resolvedSlackSettings.isSlackNotificationsUpdatedEnabled()) {
                return;
            }

            if (activity.equalsIgnoreCase("APPROVED") && !resolvedSlackSettings.isSlackNotificationsApprovedEnabled()) {
                return;
            }

            if (activity.equalsIgnoreCase("UNAPPROVED") && !resolvedSlackSettings.isSlackNotificationsUnapprovedEnabled()) {
                return;
            }

            if (activity.equalsIgnoreCase("DECLINED") && !resolvedSlackSettings.isSlackNotificationsDeclinedEnabled()) {
                return;
            }

            if (activity.equalsIgnoreCase("MERGED") && !resolvedSlackSettings.isSlackNotificationsMergedEnabled()) {
                return;
            }

            if (activity.equalsIgnoreCase("COMMENTED") && !resolvedSlackSettings.isSlackNotificationsCommentedEnabled()) {
                return;
            }

            NavBuilder.PullRequest pullRequestUrlBuilder = navBuilder
                    .project(projectName)
                    .repo(repoName)
                    .pullRequest(pullRequestId);

            String url = pullRequestUrlBuilder
                    .overview()
                    .buildAbsolute();

            SlackPayload payload = new SlackPayload();
            payload.setMrkdwn(true);
            payload.setLinkNames(true);

            SlackAttachment attachment = new SlackAttachment();
            attachment.setAuthorName(userName);
            attachment.setAuthorIcon(avatar);

            switch (event.getActivity().getAction()) {
                case OPENED:
                    attachment.setColor(ColorCode.BLUE.getCode());
                    attachment.setFallback(String.format("%s opened pull request \"%s\". <%s|(open)>",
                                                            userName,
                                                            event.getPullRequest().getTitle(),
                                                            url));
                    attachment.setText(String.format("opened pull request <%s|#%d: %s>",
                                                            url,
                                                            event.getPullRequest().getId(),
                                                            event.getPullRequest().getTitle()));


                    if (resolvedLevel == NotificationLevel.COMPACT) {
                        this.addField(attachment, "Description", event.getPullRequest().getDescription());
                    }

                    if (resolvedLevel == NotificationLevel.VERBOSE) {
                        this.addReviewers(attachment, event.getPullRequest().getReviewers());
                    }
                    break;

                case REOPENED:
                    attachment.setColor(ColorCode.BLUE.getCode());
                    attachment.setFallback(String.format("%s reopened pull request \"%s\". <%s|(open)>",
                                                            userName,
                                                            event.getPullRequest().getTitle(),
                                                            url));
                    attachment.setText(String.format("reopened pull request <%s|#%d: %s>",
                                                            url,
                                                            event.getPullRequest().getId(),
                                                            event.getPullRequest().getTitle()));

                    if (resolvedLevel == NotificationLevel.COMPACT) {
                        this.addField(attachment, "Description", event.getPullRequest().getDescription());
                    }
                    if (resolvedLevel == NotificationLevel.VERBOSE) {
                        this.addReviewers(attachment, event.getPullRequest().getReviewers());
                    }
                    break;

                case UPDATED:
                    attachment.setColor(ColorCode.PURPLE.getCode());
                    attachment.setFallback(String.format("%s updated pull request \"%s\". <%s|(open)>",
                                                            userName,
                                                            event.getPullRequest().getTitle(),
                                                            url));
                    attachment.setText(String.format("updated pull request <%s|#%d: %s>",
                                                            url,
                                                            event.getPullRequest().getId(),
                                                            event.getPullRequest().getTitle()));

                    if (resolvedLevel == NotificationLevel.COMPACT) {
                        this.addField(attachment, "Description", event.getPullRequest().getDescription());
                    }
                    if (resolvedLevel == NotificationLevel.VERBOSE) {
                        this.addReviewers(attachment, event.getPullRequest().getReviewers());
                    }
                    break;

                case APPROVED:
                    attachment.setColor(ColorCode.GREEN.getCode());
                    attachment.setFallback(String.format("%s approved pull request \"%s\". <%s|(open)>",
                                                            userName,
                                                            event.getPullRequest().getTitle(),
                                                            url));
                    attachment.setText(String.format("approved pull request <%s|#%d: %s>",
                                                            url,
                                                            event.getPullRequest().getId(),
                                                            event.getPullRequest().getTitle()));
                    break;

                case UNAPPROVED:
                    attachment.setColor(ColorCode.RED.getCode());
                    attachment.setFallback(String.format("%s unapproved pull request \"%s\". <%s|(open)>",
                                                            userName,
                                                            event.getPullRequest().getTitle(),
                                                            url));
                    attachment.setText(String.format("unapproved pull request <%s|#%d: %s>",
                                                            url,
                                                            event.getPullRequest().getId(),
                                                            event.getPullRequest().getTitle()));
                    break;

                case DECLINED:
                    attachment.setColor(ColorCode.DARK_RED.getCode());
                    attachment.setFallback(String.format("%s declined pull request \"%s\". <%s|(open)>",
                                                            userName,
                                                            event.getPullRequest().getTitle(),
                                                            url));
                    attachment.setText(String.format("declined pull request <%s|#%d: %s>",
                                                            url,
                                                            event.getPullRequest().getId(),
                                                            event.getPullRequest().getTitle()));
                    break;

                case MERGED:
                    attachment.setColor(ColorCode.DARK_GREEN.getCode());
                    attachment.setFallback(String.format("%s merged pull request \"%s\". <%s|(open)>",
                                                            userName,
                                                            event.getPullRequest().getTitle(),
                                                            url));
                    attachment.setText(String.format("merged pull request <%s|#%d: %s>",
                                                            url,
                                                            event.getPullRequest().getId(),
                                                            event.getPullRequest().getTitle()));
                    break;

                case RESCOPED:
                    attachment.setColor(ColorCode.PURPLE.getCode());
                    attachment.setFallback(String.format("%s rescoped on pull request \"%s\". <%s|(open)>",
                                                            userName,
                                                            event.getPullRequest().getTitle(),
                                                            url));
                    attachment.setText(String.format("rescoped on pull request <%s|#%d: %s>",
                                                            url,
                                                            event.getPullRequest().getId(),
                                                            event.getPullRequest().getTitle()));
                    break;

                case COMMENTED:
                    Comment comment = ((PullRequestCommentActivityEvent) event).getActivity().getComment();
                    String commentUrl = pullRequestUrlBuilder
                            .comment(comment.getId())
                            .buildAbsolute();

                    attachment.setColor(ColorCode.PALE_BLUE.getCode());
                    attachment.setFallback(String.format("%s commented on pull request \"%s\". <%s|(open)>",
                                                            userName,
                                                            event.getPullRequest().getTitle(),
                                                            commentUrl));
                    if (resolvedLevel == NotificationLevel.MINIMAL) {
                        attachment.setText(String.format("commented on pull request <%s|#%d: %s>",
                                commentUrl,
                                event.getPullRequest().getId(),
                                event.getPullRequest().getTitle()));
                    }
                    if (resolvedLevel == NotificationLevel.COMPACT || resolvedLevel == NotificationLevel.VERBOSE) {
                        attachment.setText(String.format("commented on pull request <%s|#%d: %s>\n%s",
                                commentUrl,
                                event.getPullRequest().getId(),
                                event.getPullRequest().getTitle(),
                                ((PullRequestCommentActivityEvent) event).getActivity().getComment().getText()));
                    }
                    break;
            }

            if (resolvedLevel == NotificationLevel.VERBOSE) {
                SlackAttachmentField projectField = new SlackAttachmentField();
                projectField.setTitle("Source");
                projectField.setValue(String.format("_%s — %s_\n`%s`",
                        event.getPullRequest().getFromRef().getRepository().getProject().getName(),
                        event.getPullRequest().getFromRef().getRepository().getName(),
                        event.getPullRequest().getFromRef().getDisplayId()));
                projectField.setShort(true);
                attachment.addField(projectField);

                SlackAttachmentField repoField = new SlackAttachmentField();
                repoField.setTitle("Destination");
                repoField.setValue(String.format("_%s — %s_\n`%s`",
                        event.getPullRequest().getToRef().getRepository().getProject().getName(),
                        event.getPullRequest().getToRef().getRepository().getName(),
                        event.getPullRequest().getToRef().getDisplayId()));
                repoField.setShort(true);
                attachment.addField(repoField);
            }

            payload.addAttachment(attachment);

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

    private void addField(SlackAttachment attachment, String title, String message) {
        SlackAttachmentField field = new SlackAttachmentField();
        field.setTitle(title);
        field.setValue(message);
        field.setShort(false);
        attachment.addField(field);
    }

    private void addReviewers(SlackAttachment attachment, Set<PullRequestParticipant> reviewers) {
        if (reviewers.isEmpty()) {
            return;
        }
        String names = "";
        for(PullRequestParticipant p : reviewers) {
            names += String.format("@%s ", p.getUser().getSlug());
        }
        this.addField(attachment, "Reviewers", names);
    }
}
