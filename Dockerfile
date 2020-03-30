FROM java:latest
ADD publiccms-parent/publiccms/target/publiccms.war /opt/publiccms.war
ADD data /data
ENV PORT 8080
ENV CONTEXTPATH "/publiccms"
ENV FILEPATH  "/data/publiccms"
VOLUME $FILEPATH
ENTRYPOINT java -jar -Dcms.port=$PORT -Dcms.contextPath=$CONTEXTPATH -Dcms.filePath=$FILEPATH /opt/publiccms.war > /var/log/publiccms.log
EXPOSE $PORT