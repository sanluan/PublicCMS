Table schema changes and data adjustments
1. Modify the full script (sql/init.sql)
2. Add incremental script (sql/{fromVersion}-{toVersion}.sql)
Incremental scripts
1. Add timeline record (-- {Date modified} --)
2. Add modified content (DML, DDL) [DML should occupy one line as much as possible, and DDL should occupy one line for each item]
Manually initialize and upgrade the database
1. To initialize the database, you only need to execute the script init.sql the SQL directory
2. Only the database upgrade of the stable version needs to be performed ({old version number} - {new version number}.sql)
3. To upgrade the database in the preview version, you need to execute the upgrade script after the download date of the source code in the timeline ({latest version number}-develop.sql).