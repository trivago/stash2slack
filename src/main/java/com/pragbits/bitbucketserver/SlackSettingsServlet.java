package com.pragbits.bitbucketserver;

import com.atlassian.soy.renderer.SoyException;
import com.atlassian.soy.renderer.SoyTemplateRenderer;
import com.atlassian.bitbucket.AuthorisationException;
import com.atlassian.bitbucket.repository.Repository;
import com.atlassian.bitbucket.repository.RepositoryService;
import com.atlassian.bitbucket.permission.Permission;
import com.atlassian.bitbucket.permission.PermissionValidationService;
import com.atlassian.webresource.api.assembler.PageBuilderService;
import com.atlassian.bitbucket.i18n.I18nService;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.pragbits.bitbucketserver.soy.SelectFieldOptions;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class SlackSettingsServlet extends HttpServlet {
    private final PageBuilderService pageBuilderService;
    private final SlackSettingsService slackSettingsService;
    private final RepositoryService repositoryService;
    private final SoyTemplateRenderer soyTemplateRenderer;
    private final PermissionValidationService validationService;
    private final I18nService i18nService;
    
    private Repository repository = null;

    public SlackSettingsServlet(PageBuilderService pageBuilderService,
                                    SlackSettingsService slackSettingsService,
                                    RepositoryService repositoryService,
                                    SoyTemplateRenderer soyTemplateRenderer,
                                    PermissionValidationService validationService,
                                    I18nService i18nService) {
        this.pageBuilderService = pageBuilderService;
        this.slackSettingsService = slackSettingsService;
        this.repositoryService = repositoryService;
        this.soyTemplateRenderer = soyTemplateRenderer;
        this.validationService = validationService;
        this.i18nService = i18nService;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {

        try {
            validationService.validateForRepository(this.repository, Permission.REPO_ADMIN);
        } catch (AuthorisationException e) {
            // Skip form processing
            doGet(req, res);
            return;
        }

        NotificationLevel notificationLevel = NotificationLevel.VERBOSE;
        if (null != req.getParameter("slackNotificationLevel")) {
            notificationLevel = NotificationLevel.valueOf(req.getParameter("slackNotificationLevel"));
        }

        NotificationLevel notificationPrLevel = NotificationLevel.VERBOSE;
        if (null != req.getParameter("slackNotificationPrLevel")) {
            notificationPrLevel = NotificationLevel.valueOf(req.getParameter("slackNotificationPrLevel"));
        }

        slackSettingsService.setSlackSettings(
                repository,
                new ImmutableSlackSettings(
                        "on".equals(req.getParameter("slackNotificationsOverrideEnabled")),
                        "on".equals(req.getParameter("slackNotificationsEnabled")),
                        "on".equals(req.getParameter("slackNotificationsOpenedEnabled")),
                        "on".equals(req.getParameter("slackNotificationsReopenedEnabled")),
                        "on".equals(req.getParameter("slackNotificationsUpdatedEnabled")),
                        "on".equals(req.getParameter("slackNotificationsApprovedEnabled")),
                        "on".equals(req.getParameter("slackNotificationsUnapprovedEnabled")),
                        "on".equals(req.getParameter("slackNotificationsDeclinedEnabled")),
                        "on".equals(req.getParameter("slackNotificationsMergedEnabled")),
                        "on".equals(req.getParameter("slackNotificationsCommentedEnabled")),
                        "on".equals(req.getParameter("slackNotificationsEnabledForPush")),
                        "on".equals(req.getParameter("slackNotificationsEnabledForPersonal")),
                        notificationLevel,
                        notificationPrLevel,
                        req.getParameter("slackChannelName"),
                        req.getParameter("slackWebHookUrl").trim(),
                        req.getParameter("slackUsername").trim(),
                        req.getParameter("slackIconUrl").trim(),
                        req.getParameter("slackIconEmoji").trim()
                )
        );

        doGet(req, res);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (Strings.isNullOrEmpty(pathInfo) || pathInfo.equals("/")) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        String[] pathParts = pathInfo.substring(1).split("/");
        if (pathParts.length != 4) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        String projectKey = pathParts[1];
        String repoSlug = pathParts[3];
        
        this.repository = repositoryService.getBySlug(projectKey, repoSlug);
        if (repository == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        doView(repository, response);

    }

    private void doView(Repository repository, HttpServletResponse response)
            throws ServletException, IOException {
        validationService.validateForRepository(repository, Permission.REPO_ADMIN);
        SlackSettings slackSettings = slackSettingsService.getSlackSettings(repository);
        render(response,
                "bitbucketserver.page.slack.settings.viewSlackSettings",
                ImmutableMap.<String, Object>builder()
                        .put("repository", repository)
                        .put("slackSettings", slackSettings)
                        .put("notificationLevels", new SelectFieldOptions(NotificationLevel.values()).toSoyStructure())
                        .build()
        );
    }

    private void render(HttpServletResponse response, String templateName, Map<String, Object> data)
            throws IOException, ServletException {
        pageBuilderService.assembler().resources().requireContext("plugin.page.slack");
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
