#/bin/bash

export RESORTS_DATA_PATH=/opt/homebrew/Cellar/tomcat@9/9.0.102/libexec/webapps/resorts

/opt/homebrew/opt/tomcat@9/bin/catalina run

mvn io.openliberty.tools:liberty-maven-plugin:dev -f "/Users/casaroto/projetos/wca/sample-app-mod/pom.xml"

#http://localhost:9080/resorts/

