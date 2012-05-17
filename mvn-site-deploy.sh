#!/bin/sh

export PATH=$PATH:/usr/local/bin
echo Deploying SEMANTICWEBSAPERE Maven Project SITE..
~/apache-maven-3.0.4/bin/mvn clean site:site site:attach-descriptor site:deploy
