#!/bin/sh

echo Deploying SEMANTICWEBSAPERE Maven Project to ANDROID..
echo
cat ./deploy-warning.txt
echo
echo Starting in 3 seconds..
sleep 1
echo Starting in 2 seconds..
sleep 1
echo Starting in 1 seconds..
sleep 1
~/apache-maven-3.0.4/bin/mvn clean deploy android:deploy android:run
