@echo off
echo [INFO] use maven pom.xml copy jar to folder.
echo [INFO] compile,runtime level /mvn/runtime/lib, test,provided level/mvn/test/lib

cd ..

set local_driver=%cd:~0,2%
set local_path=%cd%

set exec_path=%0
set exec_path=%exec_path:~0,-13%"
set exec_driver=%exec_path:~1,2%

%exec_driver%
cd %exec_path%/..

call mvn dependency:copy-dependencies -DoutputDirectory=mvn/test/lib -DexcludeScope=runtime -Dsilent=true
call mvn dependency:copy-dependencies -DoutputDirectory=mvn/runtime/lib -DincludeScope=runtime -Dsilent=true

%local_driver%
cd %local_path%

pause
