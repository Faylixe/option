#!/bin/bash

export KEYS_DIR="`pwd`/deploy"
mvn deploy -Prelease --settings deploy/settings.xml -DperformRelease=true -DskipTests=true
