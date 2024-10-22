from tomcat:11.0-jdk21

env JAVA_OPTS=" -Dlogback.ContextSelector=JNDI "

arg PROJECT_VERSION=0.0.0
env PROJECT_VERSION=${PROJECT_VERSION}

copy ./target/PrometheusDatabaseExporterWar-${PROJECT_VERSION}.war /app/rtsl/prom_db_exporter/
copy ./target/classes/docker.properties /app/rtsl/prom_db_exporter/
copy ./target/classes/logback.xml /app/rtsl/prom_db_exporter/

copy ./target/classes/docker.context.xml /usr/local/tomcat/conf/Catalina/localhost/

run mkdir /app/rtsl/prom_db_exporter/sources

run sed -i "s/\${project.version}/${PROJECT_VERSION}/g" /usr/local/tomcat/conf/Catalina/localhost/docker.context.xml
