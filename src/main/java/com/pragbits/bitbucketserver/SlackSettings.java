package com.pragbits.bitbucketserver;

public interface SlackSettings {

    boolean isSlackNotificationsOverrideEnabled();
    boolean isSlackNotificationsEnabled();
    boolean isSlackNotificationsOpenedEnabled();
    boolean isSlackNotificationsReopenedEnabled();
    boolean isSlackNotificationsUpdatedEnabled();
    boolean isSlackNotificationsApprovedEnabled();
    boolean isSlackNotificationsUnapprovedEnabled();
    boolean isSlackNotificationsDeclinedEnabled();
    boolean isSlackNotificationsMergedEnabled();
    boolean isSlackNotificationsCommentedEnabled();
    boolean isSlackNotificationsEnabledForPush();
    boolean isSlackNotificationsEnabledForPersonal();
    NotificationLevel getNotificationLevel();
    NotificationLevel getNotificationPrLevel();
    String getSlackChannelName();
    String getSlackWebHookUrl();
    String getSlackUsername();
    String getSlackIconUrl();
    String getSlackIconEmoji();
}
