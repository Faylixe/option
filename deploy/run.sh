#!/bin/bash

if [[ $TRAVIS_PULL_REQUEST == "false" ]]
then
	export KEYS_DIR="`pwd`/deploy"
	mvn deploy -Prelease --settings deploy/settings.xml -DperformRelease=true -DskipTests=true
fi