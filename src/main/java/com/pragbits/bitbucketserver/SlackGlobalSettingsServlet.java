package com.pragbits.bitbucketserver;

import com.atlassian.soy.renderer.SoyException;
import com.atlassian.soy.renderer.SoyTemplateRenderer;
import com.atlassian.bitbucket.AuthorisationException;
import com.atlassian.bitbucket.permission.Permission;
import com.atlassian.bitbucket.permission.PermissionValidationService;
import com.atlassian.webresource.api.assembler.PageBuilderService;
import com.atlassian.bitbucket.i18n.I18nService;
import com.google.common.collect.ImmutableMap;
import com.pragbits.bitbucketserver.soy.SelectFieldOptions;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class SlackGlobalSettingsServlet extends HttpServlet {
    static final String KEY_GLOBAL_SETTING_HOOK_URL = "stash2slack.globalsettings.hookurl";
    static final String KEY_GLOBAL_SETTING_CHANNEL_NAME = "stash2slack.globalsettings.channelname";
    static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_ENABLED = "stash2slack.globalsettings.slacknotificationsenabled";
    static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_OPENED_ENABLED = "stash2slack.globalsettings.slacknotificationsopenedenabled";
    static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_REOPENED_ENABLED = "stash2slack.globalsettings.slacknotificationsreopenedenabled";
    static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_UPDATED_ENABLED = "stash2slack.globalsettings.slacknotificationsupdatedenabled";
    static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_APPROVED_ENABLED = "stash2slack.globalsettings.slacknotificationsapprovedenabled";
    static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_UNAPPROVED_ENABLED = "stash2slack.globalsettings.slacknotificationsunapprovedenabled";
    static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_DECLINED_ENABLED = "stash2slack.globalsettings.slacknotificationsdeclinedenabled";
    static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_MERGED_ENABLED = "stash2slack.globalsettings.slacknotificationsmergedenabled";
    static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_COMMENTED_ENABLED = "stash2slack.globalsettings.slacknotificationscommentedenabled";
    static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_LEVEL = "stash2slack.globalsettings.slacknotificationslevel";
    static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_PR_LEVEL = "stash2slack.globalsettings.slacknotificationsprlevel";
    static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_PUSH_ENABLED = "stash2slack.globalsettings.slacknotificationspushenabled";
    static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_PERSONAL_ENABLED = "stash2slack.globalsettings.slacknotificationspersonalenabled";

    private final PageBuilderService pageBuilderService;
    private final SlackGlobalSettingsService slackGlobalSettingsService;
    private final SoyTemplateRenderer soyTemplateRenderer;
    private final PermissionValidationService validationService;
    private final I18nService i18nService;

    public SlackGlobalSettingsServlet(PageBuilderService pageBuilderService,
                                      SlackGlobalSettingsService slackGlobalSettingsService,
                                      SoyTemplateRenderer soyTemplateRenderer,
                                      PermissionValidationService validationService,
                                      I18nService i18nService) {
        this.pageBuilderService = pageBuilderService;
        this.slackGlobalSettingsService = slackGlobalSettingsService;
        this.soyTemplateRenderer = soyTemplateRenderer;
        this.validationService = validationService;
        this.i18nService = i18nService;

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        try {
            validationService.validateForGlobal(Permission.SYS_ADMIN);
        } catch (AuthorisationException e) {
            // Skip form processing
            doGet(req, res);
            return;
        }

        final String globalWebHookUrl = req.getParameter("slackGlobalWebHookUrl").trim();
        slackGlobalSettingsService.setWebHookUrl(KEY_GLOBAL_SETTING_HOOK_URL, globalWebHookUrl);

        String slackChannelName = req.getParameter("slackChannelName");
        slackGlobalSettingsService.setChannelName(KEY_GLOBAL_SETTING_CHANNEL_NAME, slackChannelName);

        Boolean slackNotificationsEnabled = "on".equals(req.getParameter("slackNotificationsEnabled"));
        slackGlobalSettingsService.setSlackNotificationsEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_ENABLED, slackNotificationsEnabled.toString());

        Boolean slackNotificationsOpenedEnabled = "on".equals(req.getParameter("slackNotificationsOpenedEnabled"));
        slackGlobalSettingsService.setSlackNotificationsOpenedEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_OPENED_ENABLED, slackNotificationsOpenedEnabled.toString());

        Boolean slackNotificationsReopenedEnabled = "on".equals(req.getParameter("slackNotificationsReopenedEnabled"));
        slackGlobalSettingsService.setSlackNotificationsReopenedEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_REOPENED_ENABLED, slackNotificationsReopenedEnabled.toString());

        Boolean slackNotificationsUpdatedEnabled = "on".equals(req.getParameter("slackNotificationsUpdatedEnabled"));
        slackGlobalSettingsService.setSlackNotificationsUpdatedEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_UPDATED_ENABLED, slackNotificationsUpdatedEnabled.toString());

        Boolean slackNotificationsApprovedEnabled = "on".equals(req.getParameter("slackNotificationsApprovedEnabled"));
        slackGlobalSettingsService.setSlackNotificationsApprovedEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_APPROVED_ENABLED, slackNotificationsApprovedEnabled.toString());

        Boolean slackNotificationsUnapprovedEnabled = "on".equals(req.getParameter("slackNotificationsUnapprovedEnabled"));
        slackGlobalSettingsService.setSlackNotificationsUnapprovedEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_UNAPPROVED_ENABLED, slackNotificationsUnapprovedEnabled.toString());

        Boolean slackNotificationsDeclinedEnabled = "on".equals(req.getParameter("slackNotificationsDeclinedEnabled"));
        slackGlobalSettingsService.setSlackNotificationsDeclinedEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_DECLINED_ENABLED, slackNotificationsDeclinedEnabled.toString());

        Boolean slackNotificationsMergedEnabled = "on".equals(req.getParameter("slackNotificationsMergedEnabled"));
        slackGlobalSettingsService.setSlackNotificationsMergedEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_MERGED_ENABLED, slackNotificationsMergedEnabled.toString());

        Boolean slackNotificationsCommentedEnabled = "on".equals(req.getParameter("slackNotificationsCommentedEnabled"));
        slackGlobalSettingsService.setSlackNotificationsCommentedEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_COMMENTED_ENABLED, slackNotificationsCommentedEnabled.toString());

        NotificationLevel notificationLevel = NotificationLevel.VERBOSE;
        if (null != req.getParameter("slackNotificationLevel")) {
            notificationLevel = NotificationLevel.valueOf(req.getParameter("slackNotificationLevel"));
        }
        slackGlobalSettingsService.setNotificationLevel(KEY_GLOBAL_SETTING_NOTIFICATIONS_LEVEL, notificationLevel.toString());

        NotificationLevel notificationPrLevel = NotificationLevel.VERBOSE;
        if (null != req.getParameter("slackNotificationPrLevel")) {
            notificationPrLevel = NotificationLevel.valueOf(req.getParameter("slackNotificationPrLevel"));
        }
        slackGlobalSettingsService.setNotificationPrLevel(KEY_GLOBAL_SETTING_NOTIFICATIONS_PR_LEVEL, notificationPrLevel.toString());

        Boolean slackNotificationsEnabledForPush = "on".equals(req.getParameter("slackNotificationsEnabledForPush"));
        slackGlobalSettingsService.setSlackNotificationsEnabledForPush(KEY_GLOBAL_SETTING_NOTIFICATIONS_PUSH_ENABLED, slackNotificationsEnabledForPush.toString());

        Boolean slackNotificationsEnabledForPersonal = "on".equals(req.getParameter("slackNotificationsEnabledForPersonal"));
        slackGlobalSettingsService.setSlackNotificationsEnabledForPersonal(KEY_GLOBAL_SETTING_NOTIFICATIONS_PERSONAL_ENABLED, slackNotificationsEnabledForPersonal.toString());

        doGet(req, res);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doView(response);

    }

    private void doView(HttpServletResponse response)
            throws ServletException, IOException {

        validationService.validateForGlobal(Permission.ADMIN);

        String webHookUrl = slackGlobalSettingsService.getWebHookUrl(KEY_GLOBAL_SETTING_HOOK_URL);
        String channelName = slackGlobalSettingsService.getChannelName(KEY_GLOBAL_SETTING_CHANNEL_NAME);
        Boolean slackNotificationsEnabled = slackGlobalSettingsService.getSlackNotificationsEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_ENABLED);
        Boolean slackNotificationsOpenedEnabled = slackGlobalSettingsService.getSlackNotificationsOpenedEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_OPENED_ENABLED);
        Boolean slackNotificationsReopenedEnabled = slackGlobalSettingsService.getSlackNotificationsReopenedEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_REOPENED_ENABLED);
        Boolean slackNotificationsUpdatedEnabled = slackGlobalSettingsService.getSlackNotificationsUpdatedEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_UPDATED_ENABLED);
        Boolean slackNotificationsApprovedEnabled = slackGlobalSettingsService.getSlackNotificationsApprovedEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_APPROVED_ENABLED);
        Boolean slackNotificationsUnapprovedEnabled = slackGlobalSettingsService.getSlackNotificationsUnapprovedEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_UNAPPROVED_ENABLED);
        Boolean slackNotificationsDeclinedEnabled = slackGlobalSettingsService.getSlackNotificationsDeclinedEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_DECLINED_ENABLED);
        Boolean slackNotificationsMergedEnabled = slackGlobalSettingsService.getSlackNotificationsMergedEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_MERGED_ENABLED);
        Boolean slackNotificationsCommentedEnabled = slackGlobalSettingsService.getSlackNotificationsCommentedEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_COMMENTED_ENABLED);
        Boolean slackNotificationsEnabledForPush = slackGlobalSettingsService.getSlackNotificationsEnabledForPush(KEY_GLOBAL_SETTING_NOTIFICATIONS_PUSH_ENABLED);
        Boolean slackNotificationsEnabledForPersonal = slackGlobalSettingsService.getSlackNotificationsEnabledForPersonal(KEY_GLOBAL_SETTING_NOTIFICATIONS_PERSONAL_ENABLED);
        String notificationLevel = slackGlobalSettingsService.getNotificationLevel(KEY_GLOBAL_SETTING_NOTIFICATIONS_LEVEL).toString();
        String notificationPrLevel = slackGlobalSettingsService.getNotificationPrLevel(KEY_GLOBAL_SETTING_NOTIFICATIONS_PR_LEVEL).toString();

        render(response,
                "bitbucketserver.page.slack.global.settings.viewGlobalSlackSettings",
                ImmutableMap.<String, Object>builder()
                        .put("slackGlobalWebHookUrl", webHookUrl)
                        .put("slackChannelName", channelName)
                        .put("slackNotificationsEnabled", slackNotificationsEnabled)
                        .put("slackNotificationsOpenedEnabled", slackNotificationsOpenedEnabled)
                        .put("slackNotificationsReopenedEnabled", slackNotificationsReopenedEnabled)
                        .put("slackNotificationsUpdatedEnabled", slackNotificationsUpdatedEnabled)
                        .put("slackNotificationsApprovedEnabled", slackNotificationsApprovedEnabled)
                        .put("slackNotificationsUnapprovedEnabled", slackNotificationsUnapprovedEnabled)
                        .put("slackNotificationsDeclinedEnabled", slackNotificationsDeclinedEnabled)
                        .put("slackNotificationsMergedEnabled", slackNotificationsMergedEnabled)
                        .put("slackNotificationsCommentedEnabled", slackNotificationsCommentedEnabled)
                        .put("slackNotificationsEnabledForPush", slackNotificationsEnabledForPush)
                        .put("slackNotificationsEnabledForPersonal", slackNotificationsEnabledForPersonal)
                        .put("notificationLevel", notificationLevel)
                        .put("notificationPrLevel", notificationPrLevel)
                        .put("notificationLevels", new SelectFieldOptions(NotificationLevel.values()).toSoyStructure())
                        .build()
        );
    }

    private void render(HttpServletResponse response, String templateName, Map<String, Object> data)
            throws IOException, ServletException {
        pageBuilderService.assembler().resources().requireContext("plugin.adminpage.slack");
        response.setContentType("text/html;charset=UTF-8");
        try {
            soyTemplateRenderer.render(response.getWriter(), PluginMetadata.getCompleteModuleKey("soy-templates"), templateName, data);
        } catch (SoyException e) {
            Throwable cause = e.getCause();
            if (cause instanceof IOException) {
                throw (IOException) cause;
            }
            throw new ServletException(e);
        }
    }

}
