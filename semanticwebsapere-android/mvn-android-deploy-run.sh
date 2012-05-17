#!/bin/sh

echo Deploying SEMANTICWEBSAPERE Maven Project to ANDROID..
echo
cat ./deploy-warning.txt
echo
echo Starting in 5 seconds..
echo
sleep 5
~/apache-maven-3.0.4/bin/mvn clean deploy android:deploy android:run
