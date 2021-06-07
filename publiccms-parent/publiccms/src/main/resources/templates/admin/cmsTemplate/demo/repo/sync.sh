#!/bin/sh

# 自定义配置区域开始
REPO=
TEMPLATE_BRANCH=template
PAGES_BRANCH=pages
TASKS_BRANCH=task
# 自定义配置区域结束

if [ -z "$REPO" ];
then
  echo "参考地址未配置!"
  exit 1
fi

SITEID=$1 # 不能修改
echo "当前目录`pwd`"
cd ..
echo "当前站点模板路径`pwd`"
echo "=====开始同步模板======"
if [ ! -d ".git" ];
then
    echo "初始化仓库,${REPO}"
    git init
    git checkout -b ${TEMPLATE_BRANCH}
    git remote add origin ${REPO}
    git add .
    git commit -m "init ${TEMPLATE_BRANCH}"
fi
git pull origin ${TEMPLATE_BRANCH}
git add .
git commit -m "sync"
git push origin ${TEMPLATE_BRANCH}
echo "=====同步模板结束======"

cd ../../web/site_${SITEID}
echo "当前站点路径`pwd`"
echo "=====开始同步站点数据======"
if [ ! -d ".git" ];
then
    echo "初始化仓库,${REPO}"
    git init
    git checkout -b ${PAGES_BRANCH}
    git remote add origin ${REPO}
    git add .
    git commit -m "init ${PAGES_BRANCH}"
fi
git pull origin ${PAGES_BRANCH}
git add .
git commit -m "sync"
git push origin ${PAGES_BRANCH}
echo "=====同步站点数据结束======"

cd ../../task/site_${SITEID}
echo "当前站点任务数据路径`pwd`"
echo "=====开始同步站点任务数据======"
if [ ! -d ".git" ];
then
    echo "初始化仓库,${REPO}"
    git init
    git checkout -b ${TASKS_BRANCH}
    git remote add origin ${REPO}
    git add .
    git commit -m "init ${TASKS_BRANCH}"
fi
git pull origin ${TASKS_BRANCH}
git add .
git commit -m "sync"
git push origin ${TASKS_BRANCH}
echo "=====同步站点任务数据结束======"