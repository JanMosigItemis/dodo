#!/bin/bash

# if no travis tag
if [ -z "${TRAVIS_TAG}" ]; then
    # see https://graysonkoonce.com/getting-the-current-branch-name-during-a-pull-request-in-travis-ci/
    export BRANCH=$(if [ "$TRAVIS_PULL_REQUEST" == "false" ]; then echo $TRAVIS_BRANCH; else echo $TRAVIS_PULL_REQUEST_BRANCH; fi)
    echo "TRAVIS_BRANCH=$TRAVIS_BRANCH, PR=$PR, BRANCH=$BRANCH"
    # only one of those tags is expected to exist
    export SNAPSHOT_TAG=`(git tag -l | grep dodo-snapshot-build)`
	git remote add gh https://JanMosigItemis:${GITHUB_API_KEY}@github.com/JanMosigItemis/dodo.git
    # if snapshot tag already exists
	if [ ! -z "${SNAPSHOT_TAG}" ]; then 
	    git push gh --delete "${SNAPSHOT_TAG}"
        git tag -d "${SNAPSHOT_TAG}"
    fi 
    
    export TRAVIS_TAG=dodo-snapshot-build-`(date +'%Y%m%d%H%M%S')`
    git tag -a "${TRAVIS_TAG}" -m "${TRAVIS_TAG}"
    # git push gh HEAD:"${BRANCH}" --follow-tags
    git remote remove gh
fi