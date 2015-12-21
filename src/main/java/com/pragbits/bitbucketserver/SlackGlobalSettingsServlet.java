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

        slackGlobalSettingsService.setWebHookUrl(req.getParameter("slackGlobalWebHookUrl").trim());
        slackGlobalSettingsService.setChannelName(req.getParameter("slackChannelName"));
        slackGlobalSettingsService.setUsername(req.getParameter("slackUsername"));
        slackGlobalSettingsService.setIconUrl(req.getParameter("slackIconUrl"));
        slackGlobalSettingsService.setIconEmoji(req.getParameter("slackIconEmoji"));

        slackGlobalSettingsService.setSlackNotificationsEnabled(bool(req, "slackNotificationsEnabled"));
        slackGlobalSettingsService.setSlackNotificationsOpenedEnabled(bool(req, "slackNotificationsOpenedEnabled"));
        slackGlobalSettingsService.setSlackNotificationsReopenedEnabled(bool(req, "slackNotificationsReopenedEnabled"));
        slackGlobalSettingsService.setSlackNotificationsUpdatedEnabled(bool(req, "slackNotificationsUpdatedEnabled"));
        slackGlobalSettingsService.setSlackNotificationsApprovedEnabled(bool(req, "slackNotificationsApprovedEnabled"));
        slackGlobalSettingsService.setSlackNotificationsUnapprovedEnabled(bool(req, "slackNotificationsUnapprovedEnabled"));
        slackGlobalSettingsService.setSlackNotificationsDeclinedEnabled(bool(req, "slackNotificationsDeclinedEnabled"));
        slackGlobalSettingsService.setSlackNotificationsMergedEnabled(bool(req, "slackNotificationsMergedEnabled"));
        slackGlobalSettingsService.setSlackNotificationsCommentedEnabled(bool(req, "slackNotificationsCommentedEnabled"));

        NotificationLevel notificationLevel = NotificationLevel.VERBOSE;
        if (null != req.getParameter("slackNotificationLevel")) {
            notificationLevel = NotificationLevel.valueOf(req.getParameter("slackNotificationLevel"));
        }
        slackGlobalSettingsService.setNotificationLevel(notificationLevel.toString());

        NotificationLevel notificationPrLevel = NotificationLevel.VERBOSE;
        if (null != req.getParameter("slackNotificationPrLevel")) {
            notificationPrLevel = NotificationLevel.valueOf(req.getParameter("slackNotificationPrLevel"));
        }
        slackGlobalSettingsService.setNotificationPrLevel(notificationPrLevel.toString());

        slackGlobalSettingsService.setSlackNotificationsEnabledForPush(bool(req, "slackNotificationsEnabledForPush"));
        slackGlobalSettingsService.setSlackNotificationsEnabledForPersonal(bool(req, "slackNotificationsEnabledForPersonal"));

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

        String webHookUrl = slackGlobalSettingsService.getWebHookUrl();
        String channelName = slackGlobalSettingsService.getChannelName();
        String userName = slackGlobalSettingsService.getUsername();
        String iconUrl = slackGlobalSettingsService.getIconUrl();
        String iconEmoji = slackGlobalSettingsService.getIconEmoji();
        Boolean slackNotificationsEnabled = slackGlobalSettingsService.getSlackNotificationsEnabled();
        Boolean slackNotificationsOpenedEnabled = slackGlobalSettingsService.getSlackNotificationsOpenedEnabled();
        Boolean slackNotificationsReopenedEnabled = slackGlobalSettingsService.getSlackNotificationsReopenedEnabled();
        Boolean slackNotificationsUpdatedEnabled = slackGlobalSettingsService.getSlackNotificationsUpdatedEnabled();
        Boolean slackNotificationsApprovedEnabled = slackGlobalSettingsService.getSlackNotificationsApprovedEnabled();
        Boolean slackNotificationsUnapprovedEnabled = slackGlobalSettingsService.getSlackNotificationsUnapprovedEnabled();
        Boolean slackNotificationsDeclinedEnabled = slackGlobalSettingsService.getSlackNotificationsDeclinedEnabled();
        Boolean slackNotificationsMergedEnabled = slackGlobalSettingsService.getSlackNotificationsMergedEnabled();
        Boolean slackNotificationsCommentedEnabled = slackGlobalSettingsService.getSlackNotificationsCommentedEnabled();
        Boolean slackNotificationsEnabledForPush = slackGlobalSettingsService.getSlackNotificationsEnabledForPush();
        Boolean slackNotificationsEnabledForPersonal = slackGlobalSettingsService.getSlackNotificationsEnabledForPersonal();
        String notificationLevel = slackGlobalSettingsService.getNotificationLevel().toString();
        String notificationPrLevel = slackGlobalSettingsService.getNotificationPrLevel().toString();

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
                        .put("slackUsername", userName)
                        .put("slackIconUrl", iconUrl)
                        .put("slackIconEmoji", iconEmoji)
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

    private boolean bool(HttpServletRequest req, String name) {
        return "on".equals(req.getParameter(name));
    }
}
