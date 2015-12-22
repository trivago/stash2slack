package com.pragbits.bitbucketserver;

public class ImmutableSlackSettings implements SlackSettings {

    private final boolean slackNotificationsOverrideEnabled;
    private final boolean slackNotificationsEnabled;
    private final boolean slackNotificationsOpenedEnabled;
    private final boolean slackNotificationsReopenedEnabled;
    private final boolean slackNotificationsUpdatedEnabled;
    private final boolean slackNotificationsApprovedEnabled;
    private final boolean slackNotificationsUnapprovedEnabled;
    private final boolean slackNotificationsDeclinedEnabled;
    private final boolean slackNotificationsMergedEnabled;
    private final boolean slackNotificationsCommentedEnabled;
    private final boolean slackNotificationsEnabledForPush;
    private final boolean slackNotificationsEnabledForPersonal;
    private final NotificationLevel notificationLevel;
    private final NotificationLevel notificationPrLevel;
    private final String slackChannelName;
    private final String slackWebHookUrl;
    private final String slackUsername;
    private final String slackIconUrl;
    private final String slackIconEmoji;


    public ImmutableSlackSettings(boolean slackNotificationsOverrideEnabled,
                                  boolean slackNotificationsEnabled,
                                  boolean slackNotificationsOpenedEnabled,
                                  boolean slackNotificationsReopenedEnabled,
                                  boolean slackNotificationsUpdatedEnabled,
                                  boolean slackNotificationsApprovedEnabled,
                                  boolean slackNotificationsUnapprovedEnabled,
                                  boolean slackNotificationsDeclinedEnabled,
                                  boolean slackNotificationsMergedEnabled,
                                  boolean slackNotificationsCommentedEnabled,
                                  boolean slackNotificationsEnabledForPush,
                                  boolean slackNotificationsEnabledForPersonal,
                                  NotificationLevel notificationLevel,
                                  NotificationLevel notificationPrLevel,
                                  String slackChannelName,
                                  String slackWebHookUrl,
                                  String slackUsername,
                                  String slackIconUrl,
                                  String slackIconEmoji) {
        this.slackNotificationsOverrideEnabled = slackNotificationsOverrideEnabled;
        this.slackNotificationsEnabled = slackNotificationsEnabled;
        this.slackNotificationsOpenedEnabled = slackNotificationsOpenedEnabled;
        this.slackNotificationsReopenedEnabled = slackNotificationsReopenedEnabled;
        this.slackNotificationsUpdatedEnabled = slackNotificationsUpdatedEnabled;
        this.slackNotificationsApprovedEnabled = slackNotificationsApprovedEnabled;
        this.slackNotificationsUnapprovedEnabled = slackNotificationsUnapprovedEnabled;
        this.slackNotificationsDeclinedEnabled = slackNotificationsDeclinedEnabled;
        this.slackNotificationsMergedEnabled = slackNotificationsMergedEnabled;
        this.slackNotificationsCommentedEnabled = slackNotificationsCommentedEnabled;
        this.slackNotificationsEnabledForPush = slackNotificationsEnabledForPush;
        this.slackNotificationsEnabledForPersonal = slackNotificationsEnabledForPersonal;
        this.notificationLevel = notificationLevel;
        this.notificationPrLevel = notificationPrLevel;
        this.slackChannelName = slackChannelName;
        this.slackWebHookUrl = slackWebHookUrl;
        this.slackUsername = slackUsername;
        this.slackIconUrl = slackIconUrl;
        this.slackIconEmoji = slackIconEmoji;
    }

    public boolean isSlackNotificationsOverrideEnabled() {
        return slackNotificationsOverrideEnabled;
    }

    public boolean isSlackNotificationsEnabled() {
        return slackNotificationsEnabled;
    }

    public boolean isSlackNotificationsOpenedEnabled() {
        return slackNotificationsOpenedEnabled;
    }

    public boolean isSlackNotificationsReopenedEnabled() {
        return slackNotificationsReopenedEnabled;
    }

    public boolean isSlackNotificationsUpdatedEnabled() {
        return slackNotificationsUpdatedEnabled;
    }

    public boolean isSlackNotificationsApprovedEnabled() {
        return slackNotificationsApprovedEnabled;
    }

    public boolean isSlackNotificationsUnapprovedEnabled() {
        return slackNotificationsUnapprovedEnabled;
    }

    public boolean isSlackNotificationsDeclinedEnabled() {
        return slackNotificationsDeclinedEnabled;
    }

    public boolean isSlackNotificationsMergedEnabled() {
        return slackNotificationsMergedEnabled;
    }

    public boolean isSlackNotificationsCommentedEnabled() {
        return slackNotificationsCommentedEnabled;
    }

    public boolean isSlackNotificationsEnabledForPush() {
        return slackNotificationsEnabledForPush;
    }

    public boolean isSlackNotificationsEnabledForPersonal() { return slackNotificationsEnabledForPersonal; }

    public NotificationLevel getNotificationLevel() {
        return notificationLevel;
    }

    public NotificationLevel getNotificationPrLevel() {
        return notificationPrLevel;
    }

    public String getSlackChannelName() {
        return slackChannelName;
    }

    public String getSlackWebHookUrl() {
        return slackWebHookUrl;
    }

    public String getSlackUsername() {
        return slackUsername;
    }

    public String getSlackIconUrl() {
        return slackIconUrl;
    }

    public String getSlackIconEmoji() {
        return slackIconEmoji;
    }

    @Override
    public String toString() {
        return "ImmutableSlackSettings {" + "slackNotificationsOverrideEnabled=" + slackNotificationsOverrideEnabled +
                ", slackNotificationsEnabled=" + slackNotificationsEnabled +
                ", slackNotificationsOpenedEnabled=" + slackNotificationsOpenedEnabled +
                ", slackNotificationsReopenedEnabled=" + slackNotificationsReopenedEnabled +
                ", slackNotificationsUpdatedEnabled=" + slackNotificationsUpdatedEnabled +
                ", slackNotificationsApprovedEnabled=" + slackNotificationsApprovedEnabled +
                ", slackNotificationsUnapprovedEnabled=" + slackNotificationsUnapprovedEnabled +
                ", slackNotificationsDeclinedEnabled=" + slackNotificationsDeclinedEnabled +
                ", slackNotificationsMergedEnabled=" + slackNotificationsMergedEnabled +
                ", slackNotificationsCommentedEnabled=" + slackNotificationsCommentedEnabled +
                ", slackNotificationsEnabledForPush=" + slackNotificationsEnabledForPush +
                ", slackNotificationsEnabledForPersonal=" + slackNotificationsEnabledForPersonal +
                ", notificationLevel=" + notificationLevel +
                ", notificationPrLevel=" + notificationPrLevel +
                ", slackChannelName=" + slackChannelName +
                ", slackWebHookUrl=" + slackWebHookUrl +
                ", slackUsername=" + slackUsername +
                ", slackIconUrl=" + slackIconUrl +
                ", slackIconEmoji=" + slackIconEmoji +
                "}";
    }

}
