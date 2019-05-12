#!/bin/bash

cd /var/lib/tomcat8/webapps
service tomcat8 stop
rm -r ROOT
rm ROOT.war
cp /home/plre2/ProductlinRE2-0.1.0.war ROOT.war
service tomcat8 start
service tomcat8 stop
cd ROOT/WEB-INF/classes
cp /home/plre2/application.properties .
cd /home/plre2
service tomcat8 start
