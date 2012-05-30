#!/bin/sh

export MAVEN_OPTS=-Xmx1024m

echo Packaging SEMANTICWEBSAPERE Maven Project..
~/apache-maven-3.0.4/bin/mvn clean package
