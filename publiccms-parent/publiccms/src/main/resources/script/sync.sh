#!/bin/sh
stty -echo
SITEID=$1

if [ -z "$SITEID" ];
then
  echo "siteId not config!"
  exit 1
fi

cd ..
echo "current directory `pwd`"
if [ ! -d ".git" ];then
  echo "repo not config!"
  exit 1
else
  git pull origin
fi

git add template/site_${SITEID}
git add task/site_${SITEID}
git add web/site_${SITEID}
git commit -m "sync"
git push origin
echo "complete!"