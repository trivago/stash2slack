package com.pragbits.bitbucketserver;

public interface SlackGlobalSettingsService {

    // hook and channel name
    String getWebHookUrl();
    void setWebHookUrl(String value);

    String getChannelName();
    void setChannelName(String value);

    // pull requests are enabled and pr events
    boolean getSlackNotificationsEnabled();
    void setSlackNotificationsEnabled(boolean value);

    boolean getSlackNotificationsOpenedEnabled();
    void setSlackNotificationsOpenedEnabled(boolean value);

    boolean getSlackNotificationsReopenedEnabled();
    void setSlackNotificationsReopenedEnabled(boolean value);

    boolean getSlackNotificationsUpdatedEnabled();
    void setSlackNotificationsUpdatedEnabled(boolean value);

    boolean getSlackNotificationsApprovedEnabled();
    void setSlackNotificationsApprovedEnabled(boolean value);

    boolean getSlackNotificationsUnapprovedEnabled();
    void setSlackNotificationsUnapprovedEnabled(boolean value);

    boolean getSlackNotificationsDeclinedEnabled();
    void setSlackNotificationsDeclinedEnabled(boolean value);

    boolean getSlackNotificationsMergedEnabled();
    void setSlackNotificationsMergedEnabled(boolean value);

    boolean getSlackNotificationsCommentedEnabled();
    void setSlackNotificationsCommentedEnabled(boolean value);

    // push notifications are enabled and push options
    boolean getSlackNotificationsEnabledForPush();
    void setSlackNotificationsEnabledForPush(boolean value);

    NotificationLevel getNotificationLevel();
    void setNotificationLevel(String value);

    NotificationLevel getNotificationPrLevel();
    void setNotificationPrLevel(String value);

    boolean getSlackNotificationsEnabledForPersonal();
    void setSlackNotificationsEnabledForPersonal(boolean value);

    String getUsername();
    void setUsername(String value);

    String getIconUrl();
    void setIconUrl(String value);

    String getIconEmoji();
    void setIconEmoji(String value);

}
