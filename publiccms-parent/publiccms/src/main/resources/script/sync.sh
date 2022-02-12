#!/bin/sh
stty -echo
SITEID=$1
BRANCH=$2
REPO=$3

if [ -z "$SITEID" ];
then
  echo "siteId not config!"
  exit 1
fi
if [ -z "$BRANCH" ];
then
  echo "branch not config!"
  exit 1
fi

cd ..
echo "current directory `pwd`"
if [ ! -d ".git" ];then
if [ -z "$REPO" ];
then
  echo "repo not config!"
  exit 1
fi
  echo "init ${REPO}"
  git init
  git checkout -b ${BRANCH}
  git remote add origin ${REPO}
else
  git pull origin ${BRANCH}
fi

git add template/site_${SITEID}
git add task/site_${SITEID}
git add web/site_${SITEID}
git commit -m "sync"
git push origin ${BRANCH}
echo "complete!"