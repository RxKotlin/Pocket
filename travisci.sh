#!/bin/bash

if [ "$TRAVIS_PULL_REQUEST" == "false" ] && [ "$TRAVIS_BRANCH" == "master" ]; then
  ./gradlew uploadDebugToHockeyApp
  exit 0
fi

./gradlew test
