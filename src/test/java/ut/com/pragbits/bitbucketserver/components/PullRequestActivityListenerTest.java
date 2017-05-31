package ut.com.pragbits.bitbucketserver.components;

import com.atlassian.bitbucket.avatar.AvatarService;
import com.atlassian.bitbucket.event.pull.PullRequestActivityEvent;
import com.atlassian.bitbucket.nav.NavBuilder;
import com.atlassian.bitbucket.project.Project;
import com.atlassian.bitbucket.pull.*;
import com.atlassian.bitbucket.repository.Repository;
import com.atlassian.bitbucket.user.ApplicationUser;
import com.pragbits.bitbucketserver.NotificationLevel;
import com.pragbits.bitbucketserver.SlackGlobalSettingsService;
import com.pragbits.bitbucketserver.SlackSettings;
import com.pragbits.bitbucketserver.SlackSettingsService;
import com.pragbits.bitbucketserver.components.PullRequestActivityListener;
import com.pragbits.bitbucketserver.tools.SlackNotifier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ut.com.pragbits.bitbucketserver.components.fixtures.TestFixtures;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;

public class PullRequestActivityListenerTest {
  @Mock private SlackGlobalSettingsService slackGlobalSettingsService;
  @Mock private SlackSettingsService slackSettingsService;
  @Mock private SlackSettings slackSettings;
  @Mock private NavBuilder navBuilder;
  @Mock private NavBuilder.Project navBuilderProject;
  @Mock private NavBuilder.Repo navBuilderRepo;
  @Mock private NavBuilder.PullRequest navBuilderPullRequest;
  @Mock private NavBuilder.PullRequestOverview navBuilderPullRequestOverview;
  @Mock private SlackNotifier slackNotifier;
  @Mock private AvatarService avatarService;
  @Mock private PullRequestActivity pullRequestActivity;
  @Mock private PullRequest pullRequest;
  @Mock private PullRequestParticipant pullRequestParticipant1;
  @Mock private PullRequestParticipant pullRequestParticipant2;
  @Mock private ApplicationUser applicationUser1;
  @Mock private ApplicationUser applicationUser2;
  @Mock private PullRequestRef pullRequestRefFrom;
  @Mock private PullRequestRef pullRequestRefTo;
  @Mock private Repository repository;
  @Mock private Repository forkedRepository;
  @Mock private Project project;
  @Mock private Project forkedProject;
  @Mock private PullRequestActivityEvent event;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);

    when(project.getKey()).thenReturn("PROJECT_KEY");
    when(repository.getProject()).thenReturn(project);
    when(repository.getName()).thenReturn("REPOSITORY_NAME");
    when(repository.getSlug()).thenReturn("SLUG");
    when(repository.isFork()).thenReturn(true);

    when(forkedProject.getKey()).thenReturn("PROJECT_KEY_ORIGINAL");
    when(forkedRepository.getProject()).thenReturn(forkedProject);
    when(forkedRepository.getName()).thenReturn("REPOSITORY_NAME_ORIGINAL");
    when(forkedRepository.getSlug()).thenReturn("SLUG_ORIG");

    when(slackGlobalSettingsService.getWebHookUrl()).thenReturn(TestFixtures.SLACK_GLOBAL_HOOK_URL);
    when(slackGlobalSettingsService.getChannelName()).thenReturn("#global_channel");
    when(slackGlobalSettingsService.getIconUrl()).thenReturn("Icon_Url.png");
    when(slackGlobalSettingsService.getIconEmoji()).thenReturn("Icon_Emoji.png");
    when(slackGlobalSettingsService.getNotificationPrLevel()).thenReturn(NotificationLevel.COMPACT);

    when(slackSettings.getSlackWebHookUrl()).thenReturn(TestFixtures.SLACK_LOCAL_HOOK_URL);
    when(slackSettings.getSlackChannelName()).thenReturn("#local_channel");
    when(slackSettings.getSlackIconUrl()).thenReturn("Local_Icon_Url");
    when(slackSettings.getSlackIconEmoji()).thenReturn(":rocket:");
    when(slackSettings.getNotificationLevel()).thenReturn(NotificationLevel.VERBOSE);

    when(slackSettingsService.getSlackSettings(forkedRepository)).thenReturn(slackSettings);

    when(avatarService.getUrlForPerson(any(), any())).thenReturn("http://domain/avatarUrl.png");

    when(pullRequestRefFrom.getDisplayId()).thenReturn("DisplayIdFrom123");
    when(pullRequestRefFrom.getRepository()).thenReturn(repository);
    when(pullRequestRefTo.getDisplayId()).thenReturn("DisplayIdTo456");
    when(pullRequestRefTo.getRepository()).thenReturn(forkedRepository);

    when(applicationUser1.getDisplayName()).thenReturn("User Foo Bar");
    when(applicationUser1.getSlug()).thenReturn("USER_1_SLUG");
    when(applicationUser2.getDisplayName()).thenReturn("Code Review Dude");
    when(applicationUser2.getSlug()).thenReturn("USER_2_SLUG");

    when(pullRequestParticipant1.getUser()).thenReturn(applicationUser1);
    when(pullRequestParticipant2.getUser()).thenReturn(applicationUser2);
    Set<PullRequestParticipant> pullRequestReviewers = new HashSet<>();
    pullRequestReviewers.add(pullRequestParticipant1);
    pullRequestReviewers.add(pullRequestParticipant2);

    when(pullRequest.getReviewers()).thenReturn(pullRequestReviewers);
    when(pullRequest.getFromRef()).thenReturn(pullRequestRefFrom);
    when(pullRequest.getToRef()).thenReturn(pullRequestRefTo);
    when(pullRequest.getId()).thenReturn(99999999999999L);
    when(pullRequest.getDescription()).thenReturn("pull request DESCRIPTION with some additional details and such");
    when(pullRequest.getTitle()).thenReturn("pull request TITLE");

    when(pullRequestActivity.getPullRequest()).thenReturn(pullRequest);
    when(pullRequestActivity.getId()).thenReturn(123214214L);
    when(pullRequestActivity.getAction()).thenReturn(PullRequestAction.OPENED);
    when(pullRequestActivity.getUser()).thenReturn(applicationUser1);

    when(navBuilderPullRequestOverview.buildAbsolute()).thenReturn("https://bitbucket_url/pull_request");
    when(navBuilderPullRequest.overview()).thenReturn(navBuilderPullRequestOverview);
    when(navBuilderRepo.pullRequest(pullRequest.getId())).thenReturn(navBuilderPullRequest);
    when(navBuilderProject.repo(forkedRepository.getSlug())).thenReturn(navBuilderRepo);
    when(navBuilder.project(forkedProject.getKey())).thenReturn(navBuilderProject);

    event = new PullRequestActivityEvent(new Object(), pullRequestActivity);
  }

  @After
  public void tearDown() throws Exception {



  }

  @Test
  public void notifySlackChannelNotInvokedWhenDisabled() throws Exception {
    when(slackSettings.isSlackNotificationsEnabled()).thenReturn(false);

    PullRequestActivityListener listener = new PullRequestActivityListener(slackGlobalSettingsService, slackSettingsService, navBuilder, slackNotifier, avatarService);
    listener.NotifySlackChannel(event);

    verify(slackNotifier, never()).SendSlackNotification(any(), any());
  }

  @Test
  public void notifySlackChannelNotInvokedWhenInvalidHook() throws Exception {
    when(slackSettings.isSlackNotificationsEnabledForPush()).thenReturn(true);

    when(slackSettings.getSlackWebHookUrl()).thenReturn("");
    when(slackGlobalSettingsService.getWebHookUrl()).thenReturn("");

    PullRequestActivityListener listener = new PullRequestActivityListener(slackGlobalSettingsService, slackSettingsService, navBuilder, slackNotifier, avatarService);
    listener.NotifySlackChannel(event);

    verify(slackNotifier, never()).SendSlackNotification(any(), any());
  }

  @Test
  public void notifySlackChannelNotInvokedWhenForkedRepoAndOptionDisabled() throws Exception {
    when(slackSettings.isSlackNotificationsEnabledForPush()).thenReturn(true);

    when(forkedRepository.isFork()).thenReturn(true);
    when(slackSettings.isSlackNotificationsEnabledForPersonal()).thenReturn(false);
    when(slackGlobalSettingsService.getSlackNotificationsEnabledForPersonal()).thenReturn(false);

    PullRequestActivityListener listener = new PullRequestActivityListener(slackGlobalSettingsService, slackSettingsService, navBuilder, slackNotifier, avatarService);
    listener.NotifySlackChannel(event);

    verify(slackNotifier, never()).SendSlackNotification(any(), any());
  }

  @Test
  public void notifySlackChannelInvokedWhenEnabled() throws Exception {
    when(slackSettings.isSlackNotificationsEnabled()).thenReturn(true);
    when(slackSettings.isSlackNotificationsOverrideEnabled()).thenReturn(true);
    when(slackSettings.isSlackNotificationsOpenedEnabled()).thenReturn(true);

    PullRequestActivityListener listener = new PullRequestActivityListener(slackGlobalSettingsService, slackSettingsService, navBuilder, slackNotifier, avatarService);
    listener.NotifySlackChannel(event);

    verify(slackNotifier).SendSlackNotification(eq(TestFixtures.SLACK_LOCAL_HOOK_URL), eq(TestFixtures.PR_OVERRIDE_SETTINGS_PAYLOAD));
  }

  @Test
  public void notifySlackChannelInvokedWithPatternMatching() throws Exception {
    when(slackSettings.isSlackNotificationsEnabled()).thenReturn(true);
    when(slackSettings.isSlackNotificationsOverrideEnabled()).thenReturn(true);
    when(slackSettings.isSlackNotificationsOpenedEnabled()).thenReturn(true);
    when(slackSettings.getSlackChannelName()).thenReturn("master/.*->#master_channel,bugfix/.*->#bugfix_channel");
    when(pullRequestRefTo.getDisplayId()).thenReturn(TestFixtures.BUGFIX_REF_ID);

    PullRequestActivityListener listener = new PullRequestActivityListener(slackGlobalSettingsService, slackSettingsService, navBuilder, slackNotifier, avatarService);
    listener.NotifySlackChannel(event);

    verify(slackNotifier).SendSlackNotification(eq(TestFixtures.SLACK_LOCAL_HOOK_URL), eq(TestFixtures.PR_BUGFIX_CHANNEL_PAYLOAD));
  }

  @Test
  public void notifySlackChannelEmptyChannel() throws Exception {
    when(slackSettings.isSlackNotificationsEnabled()).thenReturn(true);
    when(slackSettings.isSlackNotificationsOverrideEnabled()).thenReturn(true);
    when(slackSettings.isSlackNotificationsOpenedEnabled()).thenReturn(true);

    PullRequestActivityListener listener = new PullRequestActivityListener(slackGlobalSettingsService, slackSettingsService, navBuilder, slackNotifier, avatarService);
    listener.NotifySlackChannel(event);

    verify(slackNotifier).SendSlackNotification(eq(TestFixtures.SLACK_LOCAL_HOOK_URL), eq(TestFixtures.PR_NO_CHANNEL_PAYLOAD));
  }

}