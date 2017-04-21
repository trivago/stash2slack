package com.pragbits.bitbucketserver;

import com.atlassian.soy.renderer.SoyException;
import com.atlassian.soy.renderer.SoyTemplateRenderer;
import com.atlassian.bitbucket.AuthorisationException;
import com.atlassian.bitbucket.auth.AuthenticationContext;
import com.atlassian.bitbucket.repository.Repository;
import com.atlassian.bitbucket.repository.RepositoryService;
import com.atlassian.bitbucket.user.ApplicationUser;
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

public class SlackUserSettingsServlet extends HttpServlet {
    private final PageBuilderService pageBuilderService;
    private final SlackUserSettingsService slackSettingsService;
    private final SoyTemplateRenderer soyTemplateRenderer;
    private final PermissionValidationService validationService;
    private final I18nService i18nService;
    private final AuthenticationContext authenticationContext;

    
    public SlackUserSettingsServlet(PageBuilderService pageBuilderService,
    								SlackUserSettingsService slackSettingsService,
                                    SoyTemplateRenderer soyTemplateRenderer,
                                    PermissionValidationService validationService,
                                    I18nService i18nService,
                                    AuthenticationContext authenticationContext) {
        this.pageBuilderService = pageBuilderService;
        this.slackSettingsService = slackSettingsService;
        this.soyTemplateRenderer = soyTemplateRenderer;
        this.validationService = validationService;
        this.i18nService = i18nService;
        this.authenticationContext = authenticationContext;

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {

    	ApplicationUser user = authenticationContext.getCurrentUser();

    	NotificationLevel notificationLevel = NotificationLevel.VERBOSE;
          if (null != req.getParameter("slackNotificationLevel")) {
              notificationLevel = NotificationLevel.valueOf(req.getParameter("slackNotificationLevel"));
          }

          NotificationLevel notificationPrLevel = NotificationLevel.VERBOSE;
          if (null != req.getParameter("slackNotificationPrLevel")) {
              notificationPrLevel = NotificationLevel.valueOf(req.getParameter("slackNotificationPrLevel"));
          }

          slackSettingsService.setSlackSettings(
                  user,
                  new ImmutableSlackSettings(
                          true,
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
                          "",
                          "",
                          "",
                          "",
                          ""
                  )
          );


        doGet(req, res);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {       
    	ApplicationUser user = authenticationContext.getCurrentUser();
        if (user == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        doView(user, response);
        
    }

    private void doView(ApplicationUser user, HttpServletResponse response)
            throws ServletException, IOException {
    	
        validationService.validateAuthenticated();
        SlackSettings slackSettings = slackSettingsService.getSlackSettings(user);
        render(response,
                "bitbucketserver.page.slack.user.settings.viewUserSlackSettings",
                ImmutableMap.<String, Object>builder()
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
