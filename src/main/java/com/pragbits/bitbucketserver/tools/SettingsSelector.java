package com.pragbits.bitbucketserver.tools;

import com.atlassian.bitbucket.repository.Repository;
import com.pragbits.bitbucketserver.ImmutableSlackSettings;
import com.pragbits.bitbucketserver.SlackGlobalSettingsService;
import com.pragbits.bitbucketserver.SlackSettings;
import com.pragbits.bitbucketserver.SlackSettingsService;

public class SettingsSelector {

    private SlackGlobalSettingsService slackGlobalSettingsService;
    private SlackSettings slackSettings;
    private SlackSettings resolvedSlackSettings;

    public SettingsSelector(SlackSettingsService slackSettingsService, SlackGlobalSettingsService slackGlobalSettingsService, Repository repository) {
        this.slackGlobalSettingsService = slackGlobalSettingsService;
        this.slackSettings = slackSettingsService.getSlackSettings(repository);
        this.setResolvedSlackSettings();
    }

    public SlackSettings getResolvedSlackSettings() {
        return this.resolvedSlackSettings;
    }

    private void setResolvedSlackSettings() {
        resolvedSlackSettings = new ImmutableSlackSettings(
                slackSettings.isSlackNotificationsOverrideEnabled(),
                slackSettings.isSlackNotificationsOverrideEnabled() ? slackSettings.isSlackNotificationsEnabled() : slackGlobalSettingsService.getSlackNotificationsEnabled(),
                slackSettings.isSlackNotificationsOverrideEnabled() ? slackSettings.isSlackNotificationsOpenedEnabled() : slackGlobalSettingsService.getSlackNotificationsOpenedEnabled(),
                slackSettings.isSlackNotificationsOverrideEnabled() ? slackSettings.isSlackNotificationsReopenedEnabled() : slackGlobalSettingsService.getSlackNotificationsReopenedEnabled(),
                slackSettings.isSlackNotificationsOverrideEnabled() ? slackSettings.isSlackNotificationsUpdatedEnabled() : slackGlobalSettingsService.getSlackNotificationsUpdatedEnabled(),
                slackSettings.isSlackNotificationsOverrideEnabled() ? slackSettings.isSlackNotificationsApprovedEnabled() : slackGlobalSettingsService.getSlackNotificationsApprovedEnabled(),
                slackSettings.isSlackNotificationsOverrideEnabled() ? slackSettings.isSlackNotificationsUnapprovedEnabled() : slackGlobalSettingsService.getSlackNotificationsUnapprovedEnabled(),
                slackSettings.isSlackNotificationsOverrideEnabled() ? slackSettings.isSlackNotificationsDeclinedEnabled() : slackGlobalSettingsService.getSlackNotificationsDeclinedEnabled(),
                slackSettings.isSlackNotificationsOverrideEnabled() ? slackSettings.isSlackNotificationsMergedEnabled() : slackGlobalSettingsService.getSlackNotificationsMergedEnabled(),
                slackSettings.isSlackNotificationsOverrideEnabled() ? slackSettings.isSlackNotificationsCommentedEnabled() : slackGlobalSettingsService.getSlackNotificationsCommentedEnabled(),
                slackSettings.isSlackNotificationsOverrideEnabled() ? slackSettings.isSlackNotificationsEnabledForPush() : slackGlobalSettingsService.getSlackNotificationsEnabledForPush(),
                slackSettings.isSlackNotificationsOverrideEnabled() ? slackSettings.isSlackNotificationsEnabledForPersonal() : slackGlobalSettingsService.getSlackNotificationsEnabledForPersonal(),
                slackSettings.isSlackNotificationsOverrideEnabled() ? slackSettings.getNotificationLevel() : slackGlobalSettingsService.getNotificationLevel(),
                slackSettings.isSlackNotificationsOverrideEnabled() ? slackSettings.getNotificationPrLevel() : slackGlobalSettingsService.getNotificationPrLevel(),
                slackSettings.isSlackNotificationsOverrideEnabled() ? slackSettings.getSlackChannelName() : slackGlobalSettingsService.getChannelName(),
                slackSettings.isSlackNotificationsOverrideEnabled() ? slackSettings.getSlackWebHookUrl() : slackGlobalSettingsService.getWebHookUrl(),
                slackSettings.isSlackNotificationsOverrideEnabled() ? slackSettings.getSlackUsername() : slackGlobalSettingsService.getUsername(),
                slackSettings.isSlackNotificationsOverrideEnabled() ? slackSettings.getSlackIconUrl() : slackGlobalSettingsService.getIconUrl(),
                slackSettings.isSlackNotificationsOverrideEnabled() ? slackSettings.getSlackIconEmoji() : slackGlobalSettingsService.getIconEmoji()
        );
    }

}
