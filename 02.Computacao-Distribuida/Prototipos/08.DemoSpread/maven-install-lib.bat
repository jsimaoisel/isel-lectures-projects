set JAVA_HOME=C:\Program Files\Java\jdk-11.0.10
set PATH="D:\maven\apache-maven-3.8.1\bin";%PATH%
mvn install:install-file -Dfile=spread-4.4.0.jar  -DgroupId=org.spread -DartifactId=spread -Dversion=4.4.0 -Dpackaging=jar