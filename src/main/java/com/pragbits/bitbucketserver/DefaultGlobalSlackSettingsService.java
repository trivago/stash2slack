package com.pragbits.bitbucketserver;


import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.google.common.base.Strings;

public class DefaultGlobalSlackSettingsService implements SlackGlobalSettingsService {
    private static final String KEY_GLOBAL_SETTING_HOOK_URL = "stash2slack.globalsettings.hookurl";
    private static final String KEY_GLOBAL_SETTING_CHANNEL_NAME = "stash2slack.globalsettings.channelname";
    private static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_ENABLED = "stash2slack.globalsettings.slacknotificationsenabled";
    private static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_OPENED_ENABLED = "stash2slack.globalsettings.slacknotificationsopenedenabled";
    private static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_REOPENED_ENABLED = "stash2slack.globalsettings.slacknotificationsreopenedenabled";
    private static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_UPDATED_ENABLED = "stash2slack.globalsettings.slacknotificationsupdatedenabled";
    private static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_APPROVED_ENABLED = "stash2slack.globalsettings.slacknotificationsapprovedenabled";
    private static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_UNAPPROVED_ENABLED = "stash2slack.globalsettings.slacknotificationsunapprovedenabled";
    private static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_DECLINED_ENABLED = "stash2slack.globalsettings.slacknotificationsdeclinedenabled";
    private static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_MERGED_ENABLED = "stash2slack.globalsettings.slacknotificationsmergedenabled";
    private static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_COMMENTED_ENABLED = "stash2slack.globalsettings.slacknotificationscommentedenabled";
    private static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_LEVEL = "stash2slack.globalsettings.slacknotificationslevel";
    private static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_PR_LEVEL = "stash2slack.globalsettings.slacknotificationsprlevel";
    private static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_PUSH_ENABLED = "stash2slack.globalsettings.slacknotificationspushenabled";
    private static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_PERSONAL_ENABLED = "stash2slack.globalsettings.slacknotificationspersonalenabled";
    private static final String KEY_GLOBAL_SETTING_USER_NAME = "stash2slack.globalsettings.username";
    private static final String KEY_GLOBAL_SETTING_ICON_URL = "stash2slack.globalsettings.iconurl";
    private static final String KEY_GLOBAL_SETTING_ICON_EMOJI = "stash2slack.globalsettings.iconemojil";

    private final PluginSettings pluginSettings;

    public DefaultGlobalSlackSettingsService(PluginSettingsFactory pluginSettingsFactory) {
        this.pluginSettings = pluginSettingsFactory.createGlobalSettings();
    }

    @Override
    public String getWebHookUrl() {
        return getString(KEY_GLOBAL_SETTING_HOOK_URL);
    }

    @Override
    public void setWebHookUrl(String value) {
        if (!Strings.isNullOrEmpty(value)) {
            pluginSettings.put(KEY_GLOBAL_SETTING_HOOK_URL, value);
        } else {
            pluginSettings.put(KEY_GLOBAL_SETTING_HOOK_URL, null);
        }
    }

    @Override
    public String getChannelName() {
        return getString(KEY_GLOBAL_SETTING_CHANNEL_NAME);
    }

    @Override
    public void setChannelName(String value) {
        if (!Strings.isNullOrEmpty(value)) {
            pluginSettings.put(KEY_GLOBAL_SETTING_CHANNEL_NAME, value);
        } else {
            pluginSettings.put(KEY_GLOBAL_SETTING_CHANNEL_NAME, null);
        }
    }

    @Override
    public boolean getSlackNotificationsEnabled() {
        return getBoolean(KEY_GLOBAL_SETTING_NOTIFICATIONS_ENABLED);
    }

    @Override
    public void setSlackNotificationsEnabled(boolean value) {
        setBoolean(KEY_GLOBAL_SETTING_NOTIFICATIONS_ENABLED, value);
    }

    @Override
    public boolean getSlackNotificationsOpenedEnabled() {
        return getBoolean(KEY_GLOBAL_SETTING_NOTIFICATIONS_OPENED_ENABLED);
    }

    @Override
    public void setSlackNotificationsOpenedEnabled(boolean value) {
        setBoolean(KEY_GLOBAL_SETTING_NOTIFICATIONS_OPENED_ENABLED, value);
    }

    @Override
    public boolean getSlackNotificationsReopenedEnabled() {
        return getBoolean(KEY_GLOBAL_SETTING_NOTIFICATIONS_REOPENED_ENABLED);
    }

    @Override
    public void setSlackNotificationsReopenedEnabled(boolean value) {
        setBoolean(KEY_GLOBAL_SETTING_NOTIFICATIONS_REOPENED_ENABLED, value);
    }

    @Override
    public boolean getSlackNotificationsUpdatedEnabled() {
        return getBoolean(KEY_GLOBAL_SETTING_NOTIFICATIONS_UPDATED_ENABLED);
    }

    @Override
    public void setSlackNotificationsUpdatedEnabled(boolean value) {
        setBoolean(KEY_GLOBAL_SETTING_NOTIFICATIONS_UPDATED_ENABLED, value);
    }

    @Override
    public boolean getSlackNotificationsApprovedEnabled() {
        return getBoolean(KEY_GLOBAL_SETTING_NOTIFICATIONS_APPROVED_ENABLED);
    }

    @Override
    public void setSlackNotificationsApprovedEnabled(boolean value) {
        setBoolean(KEY_GLOBAL_SETTING_NOTIFICATIONS_APPROVED_ENABLED, value);
    }

    @Override
    public boolean getSlackNotificationsUnapprovedEnabled() {
        return getBoolean(KEY_GLOBAL_SETTING_NOTIFICATIONS_UNAPPROVED_ENABLED);
    }

    @Override
    public void setSlackNotificationsUnapprovedEnabled(boolean value) {
        setBoolean(KEY_GLOBAL_SETTING_NOTIFICATIONS_UNAPPROVED_ENABLED, value);
    }

    @Override
    public boolean getSlackNotificationsDeclinedEnabled() {
        return getBoolean(KEY_GLOBAL_SETTING_NOTIFICATIONS_DECLINED_ENABLED);
    }

    @Override
    public void setSlackNotificationsDeclinedEnabled(boolean value) {
        setBoolean(KEY_GLOBAL_SETTING_NOTIFICATIONS_DECLINED_ENABLED, value);
    }

    @Override
    public boolean getSlackNotificationsMergedEnabled() {
        return getBoolean(KEY_GLOBAL_SETTING_NOTIFICATIONS_MERGED_ENABLED);
    }

    @Override
    public void setSlackNotificationsMergedEnabled(boolean value) {
        setBoolean(KEY_GLOBAL_SETTING_NOTIFICATIONS_MERGED_ENABLED, value);
    }

    @Override
    public boolean getSlackNotificationsCommentedEnabled() {
        return getBoolean(KEY_GLOBAL_SETTING_NOTIFICATIONS_COMMENTED_ENABLED);
    }

    @Override
    public void setSlackNotificationsCommentedEnabled(boolean value) {
        setBoolean(KEY_GLOBAL_SETTING_NOTIFICATIONS_COMMENTED_ENABLED, value);
    }

    @Override
    public boolean getSlackNotificationsEnabledForPush() {
        return getBoolean(KEY_GLOBAL_SETTING_NOTIFICATIONS_PUSH_ENABLED);
    }

    @Override
    public void setSlackNotificationsEnabledForPush(boolean value) {
        setBoolean(KEY_GLOBAL_SETTING_NOTIFICATIONS_PUSH_ENABLED, value);
    }

    @Override
    public boolean getSlackNotificationsEnabledForPersonal() {
        return getBoolean(KEY_GLOBAL_SETTING_NOTIFICATIONS_PERSONAL_ENABLED);
    }

    @Override
    public void setSlackNotificationsEnabledForPersonal(boolean value) {
        setBoolean(KEY_GLOBAL_SETTING_NOTIFICATIONS_PERSONAL_ENABLED, value);
    }

    @Override
    public NotificationLevel getNotificationLevel() {
        String value = getString(KEY_GLOBAL_SETTING_NOTIFICATIONS_LEVEL);
        if (value.isEmpty()) {
            return NotificationLevel.VERBOSE;
        } else {
            return NotificationLevel.valueOf(value);
        }
    }

    @Override
    public void setNotificationLevel(String value) {
        if (!Strings.isNullOrEmpty(value)) {
            pluginSettings.put(KEY_GLOBAL_SETTING_NOTIFICATIONS_LEVEL, value);
        } else {
            pluginSettings.put(KEY_GLOBAL_SETTING_NOTIFICATIONS_LEVEL, null);
        }
    }

    @Override
    public NotificationLevel getNotificationPrLevel() {
        String value = getString(KEY_GLOBAL_SETTING_NOTIFICATIONS_PR_LEVEL);
        if (value.isEmpty()) {
            return NotificationLevel.VERBOSE;
        } else {
            return NotificationLevel.valueOf(value);
        }
    }

    @Override
    public void setNotificationPrLevel(String value) {
        if (!Strings.isNullOrEmpty(value)) {
            pluginSettings.put(KEY_GLOBAL_SETTING_NOTIFICATIONS_PR_LEVEL, value);
        } else {
            pluginSettings.put(KEY_GLOBAL_SETTING_NOTIFICATIONS_PR_LEVEL, null);
        }
    }

    @Override
    public String getUsername() {
        String userName = getString(KEY_GLOBAL_SETTING_USER_NAME);
        if (null == userName) {
            return "Stash";
        }
        return userName.toString();
    }

    @Override
    public void setUsername(String value) {
        if (!Strings.isNullOrEmpty(value)) {
            pluginSettings.put(KEY_GLOBAL_SETTING_USER_NAME, value);
        } else {
            pluginSettings.put(KEY_GLOBAL_SETTING_USER_NAME, null);
        }
    }

    @Override
    public String getIconUrl() {
        return getString(KEY_GLOBAL_SETTING_ICON_URL);
    }

    @Override
    public void setIconUrl(String value) {
        if (!Strings.isNullOrEmpty(value)) {
            pluginSettings.put(KEY_GLOBAL_SETTING_ICON_URL, value);
        } else {
            pluginSettings.put(KEY_GLOBAL_SETTING_ICON_URL, null);
        }
    }

    @Override
    public String getIconEmoji() {
        return getString(KEY_GLOBAL_SETTING_ICON_EMOJI);
    }

    @Override
    public void setIconEmoji(String value) {
        if (!Strings.isNullOrEmpty(value)) {
            pluginSettings.put(KEY_GLOBAL_SETTING_ICON_EMOJI, value);
        } else {
            pluginSettings.put(KEY_GLOBAL_SETTING_ICON_EMOJI, null);
        }
    }

    private String getString(String key) {
        Object value = pluginSettings.get(key);
        return null == value ? "" : value.toString();
    }

    private boolean getBoolean(String key) {
        return Boolean.parseBoolean((String)pluginSettings.get(key));
    }

    private void setBoolean(String key, Boolean value) {
        pluginSettings.put(key, value.toString());
    }
}