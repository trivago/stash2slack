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

public class SlackUserSettingsServlet extends HttpServlet {
    private final PageBuilderService pageBuilderService;
    private final SlackSettingsService slackSettingsService;
    private final RepositoryService repositoryService;
    private final SoyTemplateRenderer soyTemplateRenderer;
    private final PermissionValidationService validationService;
    private final I18nService i18nService;
    
    private Repository repository = null;

    public SlackUserSettingsServlet(PageBuilderService pageBuilderService,
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


        doGet(req, res);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        doView(null, response);

    }

    private void doView(Repository repository, HttpServletResponse response)
            throws ServletException, IOException {

        render(response,
                "bitbucketserver.page.slack.user.settings.viewUserSlackSettings",
                ImmutableMap.<String, Object>builder()
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
