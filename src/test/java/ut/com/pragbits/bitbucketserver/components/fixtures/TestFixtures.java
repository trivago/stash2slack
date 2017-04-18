package ut.com.pragbits.bitbucketserver.components.fixtures;

public class TestFixtures {

  public static final String BUGFIX_CHANNEL_JSON_PAYLOAD = "{\"channel\":\"#bugfix_channel\"," +
                                                        "\"text\":\":rocket: Push on \\u003cnull|`PROJECT_KEY/REPOSITORY_NAME`\\u003e branch \\u003chttps://bitbucket_url/repo|`bugfix/mybughere`\\u003e by `unknown user \\u003cunknown email\\u003e` (0 commits). See \\u003chttps://bitbucket_url/repo|commit list\\u003e.\"," +
                                                        "\"mrkdwn\":true," +
                                                        "\"link_names\":false," +
                                                        "\"attachments\":[]," +
                                                        "\"icon_url\":\"Local_Icon_Url\"," +
                                                        "\"icon_emoji\":\":rocket:\"}";

  public static final String LOCAL_SETTINGS_JSON_PAYLOAD = "{\"channel\":\"#local_channel\"," +
                                                        "\"text\":\":rocket: Push on \\u003cnull|`PROJECT_KEY/REPOSITORY_NAME`\\u003e branch \\u003chttps://bitbucket_url/repo|`Id`\\u003e by `unknown user \\u003cunknown email\\u003e` (0 commits). See \\u003chttps://bitbucket_url/repo|commit list\\u003e.\"," +
                                                        "\"mrkdwn\":true," +
                                                        "\"link_names\":false," +
                                                        "\"attachments\":[]," +
                                                        "\"icon_url\":\"Local_Icon_Url\"," +
                                                        "\"icon_emoji\":\":rocket:\"}";

  public static final String JSON_PAYLOAD_NO_CHANNEL = "{\"text\":\":rocket: Push on \\u003cnull|`PROJECT_KEY/REPOSITORY_NAME`\\u003e branch \\u003chttps://bitbucket_url/repo|`Id`\\u003e by `unknown user \\u003cunknown email\\u003e` (0 commits). See \\u003chttps://bitbucket_url/repo|commit list\\u003e.\"," +
                                                        "\"mrkdwn\":true," +
                                                        "\"link_names\":false," +
                                                        "\"attachments\":[]," +
                                                        "\"icon_url\":\"Local_Icon_Url\"," +
                                                        "\"icon_emoji\":\":rocket:\"}";

  public static final String BUGFIX_REF_ID = "refs/heads/bugfix/mybughere";
}
