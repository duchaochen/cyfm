@echo off
echo [Pre-Requirement] Makesure install JDK 6.0+ and set the JAVA_HOME.
echo [Pre-Requirement] Makesure install Maven 3.0.3+ and set the PATH.

set MVN=mvn
set MAVEN_OPTS=%MAVEN_OPTS% -XX:MaxPermSize=128m

echo [Step 1] Install all springside modules and archetype to local maven repository.
cd ppcxy-parent
call %MVN% clean install
if errorlevel 1 goto error

cd ..
call %MVN% -Dmaven.test.skip=true clean install
if errorlevel 1 goto error

echo [Step 2] Initialize schema and data for all example projects.
cd cyfm-web
call %MVN% antrun:run -Prefresh-db
if errorlevel 1 goto error

start "cyfm-web" %MVN% clean jetty:run -Djetty.port=8081

echo [INFO] Please wait a moment. When you see "[INFO] Started Jetty Server" in both 2 popup consoles, you can access below demo sites:
echo [INFO] http://localhost:8081/cyfm-web

goto end
:error
echo Error Happen!!
:end
pause
