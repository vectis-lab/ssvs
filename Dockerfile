FROM jboss/wildfly:23.0.2.Final
MAINTAINER Dmitry Degrave dmeetry@gmail.com
RUN /opt/jboss/wildfly/bin/add-user.sh admin admin --silent
ADD ssvs2.war /opt/jboss/wildfly/standalone/deployments/
ADD standalone.xml /opt/jboss/wildfly/standalone/configuration/
ADD standalone.sh /opt/jboss/wildfly/bin/
ADD lib/mysql-connector-java-8.0.17.jar /opt/jboss/wildfly/modules/system/layers/base/com/mysql/main/
ADD module.xml /opt/jboss/wildfly/modules/system/layers/base/com/mysql/main/
ADD sgc.pem /
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]