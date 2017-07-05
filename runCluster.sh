#!/bin/bash

#bye bye whatever is there now!
echo "recreating home directories from scratch"
rm -Rf target/*

#create cluster home dirs for mysql lib
mkdir -p target/bitbucket-node-2/home/lib
mkdir -p target/bitbucket-node-1/home/lib
mkdir -p target/bitbucket-node-2/home/shared
mkdir -p target/bitbucket-node-1/home/shared

echo "providing bitbucket-config to clustered home for db settings"
# set bitbucket-config.properties with defaults and force db pool below CF max limit of 40 connections
cat >>target/bitbucket-node-1/home/shared/bitbucket-config.properties <<EOF
logging.logger.com.atlassian.bitbucket=INFO
logging.logger.com.atlassian.bitbucket.internal.project=WARN
logging.logger.ROOT=WARN
feature.getting.started.page=false
plugin.branch-permissions.feature.splash=false
db.pool.partition.count=2
db.pool.partition.connection.maximum=9
EOF

cat target/bitbucket-node-1/home/shared/bitbucket-config.properties > target/bitbucket-node-2/home/shared/bitbucket-config.properties

echo "starting test group"
# run with cluster option
atlas-run --testGroup clusterTestGroup --jvmargs "-Dhttp.proxyHost=localhost -Dhttp.proxyPort=3128 -Dhttps.proxyHost=localhost -Dhttps.proxyPort=3128"