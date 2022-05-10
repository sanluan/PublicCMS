@echo off

set backup_date=%date%_%time%

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
mysqldump --add-drop-table -u%USERNAME% -p%PASSWORD% %DATABASE% --single-transaction > ../backup/publiccms-%backup_date%.sql
echo complete!