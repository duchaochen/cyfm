#!/bin/bash

echo "[Pre-Requirement] Makesure install JDK 7.0+ and set the JAVA_HOME."
echo "[Pre-Requirement] Makesure install Maven 3.0.3+ and set the PATH."

export MAVEN_OPTS="$MAVEN_OPTS -Xmx1024m -XX:MaxPermSize=128M -Djava.security.egd=file:/dev/./urandom"

echo "[Step 1] Install all cyfm modules and archetype to local maven repository."

cd ppcxy-parent
mvn clean install

cd ..
mvn clean install

if [ $? -ne 0 ];then
  echo "Quit  because maven install fail"
  exit -1
fi


echo [Step 2] Initialize schema and data for cyfm-web projects.
cd cyfm-web
mvn antrun:run -Prefresh-db

if [ $? -ne 0 ];then
  echo "Quit  because maven install fail"
  exit -1
fi


echo "[Step 3] Start cyfm-web projects."
echo "[INFO] Please wait a moment. When you see "[INFO] Started Jetty Server" in both 2 popup consoles, you can access below demo sites: "
echo "[INFO] http://localhost:8081/cyfm-web"
mvn clean jetty:run -Djetty.port=8081
