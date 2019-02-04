#!/bin/bash

export VER="1.0.1-SNAPSHOT"
export TIMESTAMP=`(date +'%Y%m%d%H%M%S')`
export ARTIFACT_DIR="target"

# see https://graysonkoonce.com/getting-the-current-branch-name-during-a-pull-request-in-travis-ci/
export BRANCH=$(if [ "$TRAVIS_PULL_REQUEST" == "false" ]; then echo $TRAVIS_BRANCH; else echo $TRAVIS_PULL_REQUEST_BRANCH; fi)
echo "TARVIS_TAG=${TRAVIS_TAG}, TRAVIS_BRANCH=$TRAVIS_BRANCH, PR=$PR, BRANCH=$BRANCH, VER=${VER}, TIMESTAMP=${TIMESTAMP}"
	
# if no travis tag and we are not on master
if [ -z "${TRAVIS_TAG}" ] && [ "${BRANCH}" != "master" ] ; then
    # only one of those tags is expected to exist
    export SNAPSHOT_TAG=`(git tag -l | grep dodo-snapshot-build)`
	git remote add gh https://JanMosigItemis:${GITHUB_API_KEY}@github.com/JanMosigItemis/dodo.git
    # if snapshot tag already exists
	if [ ! -z "${SNAPSHOT_TAG}" ]; then 
	    git push gh --delete "${SNAPSHOT_TAG}"
        git tag -d "${SNAPSHOT_TAG}"
    fi 
    
    export TRAVIS_TAG=dodo-snapshot-build-"${TIMESTAMP}"
    git tag -a "${TRAVIS_TAG}" -m "${TRAVIS_TAG}"
    # git push gh HEAD:"${BRANCH}" --follow-tags
    git remote remove gh
	export ARTIFACT_WIN=dodo-"${VER}"-win.jar
	export ARTIFACT_MAC=dodo-"${VER}"-mac.jar
	export ARTIFACT_LINUX=dodo-"${VER}"-linux.jar
else 
    mv "${ARTIFACT_DIR}"/dodo-"${VER}"-win.jar "${ARTIFACT_DIR}"/dodo-"${VER}"-win-"${TIMESTAMP}".jar
    mv "${ARTIFACT_DIR}"/dodo-"${VER}"-mac.jar "${ARTIFACT_DIR}"/dodo-"${VER}"-mac-"${TIMESTAMP}".jar
    mv "${ARTIFACT_DIR}"/dodo-"${VER}"-linux.jar "${ARTIFACT_DIR}"/dodo-"${VER}"-linux-"${TIMESTAMP}".jar
    export ARTIFACT_WIN=dodo-"${VER}"-win-"${TIMESTAMP}".jar
    export ARTIFACT_MAC=dodo-"${VER}"-mac-"${TIMESTAMP}".jar
    export ARTIFACT_LINUX=dodo-"${VER}"-linux-"${TIMESTAMP}".jar
fi 

echo "ARTIFACT_WIN=${ARTIFACT_WIN}"
echo "ARTIFACT_MAC=${ARTIFACT_MAC}"
echo "ARTIFACT_LINUX=${ARTIFACT_LINUX}"
cd "${ARTIFACT_DIR}"
sha512sum "${ARTIFACT_WIN}" > "${ARTIFACT_WIN}".sha512
sha512sum "${ARTIFACT_MAC}" > "${ARTIFACT_MAC}".sha512
sha512sum "${ARTIFACT_LINUX}" > "${ARTIFACT_LINUX}".sha512
cd ..
