@echo off
set SITEID=%1

if "%SITEID%"=="" (
  echo "siteId not config!"
  goto :eof
) else (
  goto exec
)

:exec
cd ..
echo "current directory %cd%"
if not exist ".git" (
  echo "repo not config!"
  goto :eof
) else (
  git pull origin
)
git add template/site_%SITEID%
git add task/site_%SITEID%
git add web/site_%SITEID%
git commit -m "sync"
git push origin
echo complete!