# Stash2Slack (for Bitbucket Server)

Plugin subscribes to Bitbucket Server events and send notifications to
Slack channels.

Currently the following events are supported:

* PullRequestActivity - Event that is raised when an activity is created for a pull request.
* RepositoryPushEvent - Event that is raised when a user pushes one or more refs to a repository

## Installation

Grab the latest release, and upload it to your stash instance on 
the manage addons page.

## Configuration

You need to create an incoming web hook in slack. That will give you the
hook url and the default channel name. Notifications will go to the
defaul slack channel, unless you override them in the configuration for
a given repository.

You can enter the webhook url in the global settings. Just go to
http://your.stash.host/plugins/servlet/slack-global-settings/admin and
edit the hook url.

The global settings can be set up for both push notifications and for pull requests.
Then, for each repository those settings can be overriden if needed.

Pull requests can be further filtered by events (opened, commented, etc). Also, notifications can be verbose,
compact or minimal to slack.

