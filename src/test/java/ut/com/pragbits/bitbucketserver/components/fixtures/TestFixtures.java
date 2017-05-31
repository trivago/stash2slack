package ut.com.pragbits.bitbucketserver.components.fixtures;

public class TestFixtures {

  public static final String PUSH_BUGFIX_CHANNEL_PAYLOAD = "{\"channel\":\"#bugfix_channel\"," +
                                                        "\"text\":\":rocket: Push on \\u003cnull|`PROJECT_KEY/REPOSITORY_NAME`\\u003e branch \\u003chttps://bitbucket_url/repo|`bugfix/mybughere`\\u003e by `unknown user \\u003cunknown email\\u003e` (0 commits). See \\u003chttps://bitbucket_url/repo|commit list\\u003e.\"," +
                                                        "\"mrkdwn\":true," +
                                                        "\"link_names\":false," +
                                                        "\"attachments\":[]," +
                                                        "\"icon_url\":\"Local_Icon_Url\"," +
                                                        "\"icon_emoji\":\":rocket:\"}";

  public static final String PUSH_OVERRIDE_SETTINGS_PAYLOAD = "{\"channel\":\"#local_channel\"," +
                                                        "\"text\":\":rocket: Push on \\u003cnull|`PROJECT_KEY/REPOSITORY_NAME`\\u003e branch \\u003chttps://bitbucket_url/repo|`Id`\\u003e by `unknown user \\u003cunknown email\\u003e` (0 commits). See \\u003chttps://bitbucket_url/repo|commit list\\u003e.\"," +
                                                        "\"mrkdwn\":true," +
                                                        "\"link_names\":false," +
                                                        "\"attachments\":[]," +
                                                        "\"icon_url\":\"Local_Icon_Url\"," +
                                                        "\"icon_emoji\":\":rocket:\"}";

  public static final String PUSH_NO_CHANNEL_PAYLOAD = "{\"text\":\":rocket: Push on \\u003cnull|`PROJECT_KEY/REPOSITORY_NAME`\\u003e branch \\u003chttps://bitbucket_url/repo|`Id`\\u003e by `unknown user \\u003cunknown email\\u003e` (0 commits). See \\u003chttps://bitbucket_url/repo|commit list\\u003e.\"," +
                                                        "\"mrkdwn\":true," +
                                                        "\"link_names\":false," +
                                                        "\"attachments\":[]," +
                                                        "\"icon_url\":\"Local_Icon_Url\"," +
                                                        "\"icon_emoji\":\":rocket:\"}";

  public static final String PR_BUGFIX_CHANNEL_PAYLOAD = "{\"channel\":\"#bugfix_channel\"," +
                                                        "\"mrkdwn\":true," +
                                                        "\"link_names\":true," +
                                                        "\"attachments\":" +
                                                          "[{\"mrkdwn_in\":" +
                                                              "[\"pretext\",\"text\",\"title\",\"fields\",\"fallback\"]," +
                                                              "\"fields\":[]," +
                                                              "\"fallback\":\"unknown user opened pull request \\\"pull request TITLE\\\". \\u003chttps://bitbucket_url/pull_request|(open)\\u003e\"," +
                                                              "\"color\":\"#2267c4\"," +
                                                              "\"text\":\"opened pull request \\u003chttps://bitbucket_url/pull_request|#99999999999999: pull request TITLE\\u003e\"," +
                                                              "\"author_name\":\"unknown user\"," +
                                                              "\"author_icon\":\"\"}]," +
                                                        "\"icon_url\":\"Local_Icon_Url\"," +
                                                        "\"icon_emoji\":\":rocket:\"}";

  public static final String PR_OVERRIDE_SETTINGS_PAYLOAD = "{\"channel\":\"#local_channel\"," +
                                                        "\"mrkdwn\":true," +
                                                        "\"link_names\":true," +
                                                        "\"attachments\":" +
                                                          "[{\"mrkdwn_in\":" +
                                                              "[\"pretext\",\"text\",\"title\",\"fields\",\"fallback\"]," +
                                                              "\"fields\":[]," +
                                                              "\"fallback\":\"unknown user opened pull request \\\"pull request TITLE\\\". \\u003chttps://bitbucket_url/pull_request|(open)\\u003e\"," +
                                                              "\"color\":\"#2267c4\"," +
                                                              "\"text\":\"opened pull request \\u003chttps://bitbucket_url/pull_request|#99999999999999: pull request TITLE\\u003e\"," +
                                                              "\"author_name\":\"unknown user\"," +
                                                              "\"author_icon\":\"\"}]," +
                                                        "\"icon_url\":\"Local_Icon_Url\"," +
                                                        "\"icon_emoji\":\":rocket:\"}";

  public static final String PR_NO_CHANNEL_PAYLOAD = "{\"channel\":\"#local_channel\"," +
                                                      "\"mrkdwn\":true," +
                                                      "\"link_names\":true," +
                                                      "\"attachments\":" +
                                                        "[{\"mrkdwn_in\":" +
                                                            "[\"pretext\",\"text\",\"title\",\"fields\",\"fallback\"]," +
                                                            "\"fields\":[]," +
                                                            "\"fallback\":\"unknown user opened pull request \\\"pull request TITLE\\\". \\u003chttps://bitbucket_url/pull_request|(open)\\u003e\"," +
                                                            "\"color\":\"#2267c4\"," +
                                                            "\"text\":\"opened pull request \\u003chttps://bitbucket_url/pull_request|#99999999999999: pull request TITLE\\u003e\"," +
                                                            "\"author_name\":\"unknown user\"," +
                                                            "\"author_icon\":\"\"}]," +
                                                      "\"icon_url\":\"Local_Icon_Url\"," +
                                                      "\"icon_emoji\":\":rocket:\"}";

  public static final String BUGFIX_REF_ID = "refs/heads/bugfix/mybughere";

  public static final String SLACK_LOCAL_HOOK_URL = "https://hooks.slack.com/localhook";

  public static final String SLACK_GLOBAL_HOOK_URL = "https://hooks.slack.com/globalhook";
}
