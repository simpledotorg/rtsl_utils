from tomcat:11.0-jdk21

ENV JAVA_OPTS " -Dlogback.ContextSelector=JNDI "

COPY ./target/PrometheusDatabaseExporterWar-${project.version}.war /app/rtsl/prom_db_exporter/
COPY ./target/classes/docker.properties /app/rtsl/prom_db_exporter/
COPY ./target/classes/logback.xml /app/rtsl/prom_db_exporter/

COPY ./target/classes/docker.context.xml /usr/local/tomcat/conf/Catalina/localhost/

RUN mkdir /app/rtsl/prom_db_exporter/sources
