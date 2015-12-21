package com.pragbits.bitbucketserver.tools;

import java.util.LinkedList;
import java.util.List;

public class SlackPayload {

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channelName) {
        this.channel = channelName;
    }

    private String channel;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    private String text;

    public boolean isMrkdwn() {
        return mrkdwn;
    }

    public void setMrkdwn(boolean mrkdwn) {
        this.mrkdwn = mrkdwn;
    }

    private boolean mrkdwn;

    public boolean isLinkNames() {
        return link_names;
    }

    public void setLinkNames(boolean link_names) {
        this.link_names = link_names;
    }

    private boolean link_names;

    private List<SlackAttachment> attachments = new LinkedList<SlackAttachment>();

    public void addAttachment(SlackAttachment slackAttachment) {
        this.attachments.add(slackAttachment);
    }

    public void removeAttachment(int index) {
        this.attachments.remove(index);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private String username;

    public String getIconUrl() {
        return icon_url;
    }

    public void setIconUrl(String icon_url) {
        this.icon_url = icon_url;
    }

    private String icon_url;

    public String getIconEmoji() {
        return icon_emoji;
    }

    public void setIconEmoji(String icon_emoji) {
        this.icon_emoji = icon_emoji;
    }

    private String icon_emoji;

}
