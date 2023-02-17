#!/bin/sh
stty -echo

backup_date=`date +%Y-%m-%d'_'%H:%M:%S`

DATABASE=$1
USERNAME=$2
PASSWORD=$3

if [ -z "$DATABASE" ];
then
  echo "database not config!"
  exit 1
fi
if [ -z "$USERNAME" ];
then
  echo "username not config!"
  exit 1
fi

mysqldump --add-drop-table --single-transaction -u${USERNAME} -p${PASSWORD} ${DATABASE} |gzip > ../backup/sql/publiccms-${backup_date}.sql.gz
echo "complete!"
