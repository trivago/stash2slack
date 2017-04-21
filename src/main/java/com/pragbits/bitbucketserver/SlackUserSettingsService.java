package com.pragbits.bitbucketserver;

import com.atlassian.bitbucket.user.ApplicationUser;

import javax.annotation.Nonnull;

public interface SlackUserSettingsService {

    @Nonnull
    SlackSettings getSlackSettings(@Nonnull ApplicationUser user);

    @Nonnull
    SlackSettings setSlackSettings(@Nonnull ApplicationUser user, @Nonnull SlackSettings settings);

}
