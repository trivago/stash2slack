package ut.com.pragbits.bitbucketserver.components;

import com.atlassian.bitbucket.commit.Commit;
import com.atlassian.bitbucket.commit.CommitService;
import com.atlassian.bitbucket.event.repository.RepositoryPushEvent;
import com.atlassian.bitbucket.nav.NavBuilder;
import com.atlassian.bitbucket.project.Project;
import com.atlassian.bitbucket.repository.*;
import com.atlassian.bitbucket.util.Page;
import com.pragbits.bitbucketserver.NotificationLevel;
import com.pragbits.bitbucketserver.SlackGlobalSettingsService;
import com.pragbits.bitbucketserver.SlackSettings;
import com.pragbits.bitbucketserver.SlackSettingsService;
import com.pragbits.bitbucketserver.components.RepositoryPushActivityListener;
import com.pragbits.bitbucketserver.tools.SlackNotifier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ut.com.pragbits.bitbucketserver.components.fixtures.TestFixtures;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class RepositoryPushActivityListenerTest {
  @Mock private SlackGlobalSettingsService slackGlobalSettingsService;
  @Mock private SlackSettingsService slackSettingsService;
  @Mock private SlackSettings slackSettings;
  @Mock private CommitService commitService;
  @Mock private NavBuilder navBuilder;
  @Mock private NavBuilder.Project navBuilderProject;
  @Mock private NavBuilder.Repo navBuilderRepo;
  @Mock private NavBuilder.ListCommits navBuilderListCommits;
  @Mock private SlackNotifier slackNotifier;
  @Mock private Repository repository;
  @Mock private Project project;
  @Mock private RefChange refChange;
  @Mock private MinimalRef minimalRef;
  @Mock private Page<Commit> pageOfCommits;

  private RepositoryPushEvent event;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);

    when(project.getKey()).thenReturn("PROJECT_KEY");
    when(repository.getProject()).thenReturn(project);
    when(repository.getName()).thenReturn("REPOSITORY_NAME");
    when(repository.getSlug()).thenReturn("SLUG");

    when(slackGlobalSettingsService.getWebHookUrl()).thenReturn(TestFixtures.SLACK_GLOBAL_HOOK_URL);
    when(slackGlobalSettingsService.getChannelName()).thenReturn("#global_channel");
    when(slackGlobalSettingsService.getIconUrl()).thenReturn("Icon_Url.png");
    when(slackGlobalSettingsService.getIconEmoji()).thenReturn("Icon_Emoji.png");
    when(slackGlobalSettingsService.getNotificationLevel()).thenReturn(NotificationLevel.COMPACT);

    when(slackSettings.getSlackWebHookUrl()).thenReturn(TestFixtures.SLACK_LOCAL_HOOK_URL);
    when(slackSettings.getSlackChannelName()).thenReturn("#local_channel");
    when(slackSettings.isSlackNotificationsOverrideEnabled()).thenReturn(true);
    when(slackSettings.getSlackIconUrl()).thenReturn("Local_Icon_Url");
    when(slackSettings.getSlackIconEmoji()).thenReturn(":rocket:");
    when(slackSettings.getNotificationLevel()).thenReturn(NotificationLevel.VERBOSE);

    when(slackSettingsService.getSlackSettings(repository)).thenReturn(slackSettings);

    when(minimalRef.getDisplayId()).thenReturn("displayId");
    when(minimalRef.getId()).thenReturn("Id");
    when(minimalRef.getType()).thenReturn(StandardRefType.BRANCH);
    when(refChange.getRef()).thenReturn(minimalRef);
    when(refChange.getType()).thenReturn(RefChangeType.UPDATE);
    when(refChange.getFromHash()).thenReturn("12345");
    when(refChange.getToHash()).thenReturn("67890");

    List<RefChange> refChanges = new ArrayList<>();
    refChanges.add(refChange);

    when(navBuilderProject.repo(repository.getSlug())).thenReturn(navBuilderRepo);
    when(navBuilder.project(project.getKey())).thenReturn(navBuilderProject);

    when(navBuilderListCommits.buildAbsolute()).thenReturn("https://bitbucket_url/repo");
    when(navBuilderListCommits.until(refChange.getRef().getId())).thenReturn(navBuilderListCommits);
    when(navBuilderListCommits.until(TestFixtures.BUGFIX_REF_ID)).thenReturn(navBuilderListCommits);
    when(navBuilderRepo.commits()).thenReturn(navBuilderListCommits);

    when(pageOfCommits.getValues()).thenReturn(new ArrayList<>());
    when(commitService.getCommitsBetween(any(), any())).thenReturn(pageOfCommits);

    event = new RepositoryPushEvent(new Object(), repository, refChanges);
  }

  @After
  public void tearDown() throws Exception {



  }

  @Test
  public void notifySlackChannelNotInvokedWhenDisabled() throws Exception {
    when(slackSettings.isSlackNotificationsEnabledForPush()).thenReturn(false);

    RepositoryPushActivityListener listener = new RepositoryPushActivityListener(slackGlobalSettingsService, slackSettingsService, commitService, navBuilder, slackNotifier);
    listener.NotifySlackChannel(event);

    verify(slackNotifier, never()).SendSlackNotification(any(), any());
  }

  @Test
  public void notifySlackChannelNotInvokedWhenInvalidHook() throws Exception {
    when(slackSettings.isSlackNotificationsEnabledForPush()).thenReturn(true);

    when(slackSettings.getSlackWebHookUrl()).thenReturn("");
    when(slackGlobalSettingsService.getWebHookUrl()).thenReturn("");

    RepositoryPushActivityListener listener = new RepositoryPushActivityListener(slackGlobalSettingsService, slackSettingsService, commitService, navBuilder, slackNotifier);
    listener.NotifySlackChannel(event);

    verify(slackNotifier, never()).SendSlackNotification(any(), any());
  }

  @Test
  public void notifySlackChannelNotInvokedWhenForkedRepoAndOptionDisabled() throws Exception {
    when(slackSettings.isSlackNotificationsEnabledForPush()).thenReturn(true);

    when(repository.isFork()).thenReturn(true);
    when(slackSettings.isSlackNotificationsEnabledForPersonal()).thenReturn(false);
    when(slackGlobalSettingsService.getSlackNotificationsEnabledForPersonal()).thenReturn(false);

    RepositoryPushActivityListener listener = new RepositoryPushActivityListener(slackGlobalSettingsService, slackSettingsService, commitService, navBuilder, slackNotifier);
    listener.NotifySlackChannel(event);

    verify(slackNotifier, never()).SendSlackNotification(any(), any());
  }

  @Test
  public void notifySlackChannelInvokedWhenEnabled() throws Exception {
    when(slackSettings.isSlackNotificationsEnabledForPush()).thenReturn(true);

    RepositoryPushActivityListener listener = new RepositoryPushActivityListener(slackGlobalSettingsService, slackSettingsService, commitService, navBuilder, slackNotifier);
    listener.NotifySlackChannel(event);

    verify(slackNotifier).SendSlackNotification(eq(TestFixtures.SLACK_LOCAL_HOOK_URL), eq(TestFixtures.PUSH_OVERRIDE_SETTINGS_PAYLOAD));
  }

  @Test
  public void notifySlackChannelInvokedWithPatternMatching() throws Exception {
    when(slackSettings.isSlackNotificationsEnabledForPush()).thenReturn(true);
    when(slackSettings.getSlackChannelName()).thenReturn("master/.*->#master_channel,bugfix/.*->#bugfix_channel");
    when(minimalRef.getId()).thenReturn(TestFixtures.BUGFIX_REF_ID);

    RepositoryPushActivityListener listener = new RepositoryPushActivityListener(slackGlobalSettingsService, slackSettingsService, commitService, navBuilder, slackNotifier);
    listener.NotifySlackChannel(event);

    verify(slackNotifier).SendSlackNotification(eq(TestFixtures.SLACK_LOCAL_HOOK_URL), eq(TestFixtures.PUSH_BUGFIX_CHANNEL_PAYLOAD));
  }

  @Test
  public void notifySlackChannelEmptyChannel() throws Exception {
    when(slackSettings.isSlackNotificationsEnabledForPush()).thenReturn(true);
    when(slackSettings.getSlackChannelName()).thenReturn("");
    when(slackGlobalSettingsService.getChannelName()).thenReturn("");

    RepositoryPushActivityListener listener = new RepositoryPushActivityListener(slackGlobalSettingsService, slackSettingsService, commitService, navBuilder, slackNotifier);
    listener.NotifySlackChannel(event);

    verify(slackNotifier).SendSlackNotification(eq(TestFixtures.SLACK_LOCAL_HOOK_URL), eq(TestFixtures.PUSH_NO_CHANNEL_PAYLOAD));
  }

}