#!/bin/sh

if [ "$TRAVIS_BRANCH" == "master" ]; then
  ./gradlew uploadDebugToHockeyApp
  exit 0
fi

./gradlew test
