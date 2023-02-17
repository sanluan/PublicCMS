@echo off

set backup_date=%date%_%time:~0,2%_%time:~3,2%_%time:~6,2%

set DATABASE=%1
set USERNAME=%2
set PASSWORD=%3

if "%DATABASE%"=="" (
  echo "database not config!"
  goto :eof
) else (
  goto exec
)
if "%USERNAME%"=="" (
  echo "username not config!"
  goto :eof
) else (
  goto exec
)

:exec
mysqldump --add-drop-table --single-transaction -u%USERNAME% -p%PASSWORD% %DATABASE%>../backup/sql/"publiccms-%backup_date%.sql"
echo complete!