@echo off
set SITEID=%1
set BRANCH=%2
set REPO=%3

if "%SITEID%"=="" (
  echo "siteId not config!"
  goto :eof
) else (
  goto exec
)
if "%BRANCH%"=="" (
  echo "branch not config!"
  goto :eof
) else (
  goto exec
)

:exec
cd ..
echo "current directory %cd%"
if not exist ".git" (
  if "%REPO%"=="" (
    echo "repo not config!"
    goto :eof
  )
  echo "init %REPO%"
  git init
  git checkout -b %BRANCH%
  git remote add origin %REPO%
) else (
  git pull origin %BRANCH%
)
git add template/site_%SITEID%
git add task/site_%SITEID%
git add web/site_%SITEID%
git commit -m "sync"
git push origin %BRANCH%
echo "complete!"