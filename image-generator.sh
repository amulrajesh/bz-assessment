#!/bin/bash

if [ $# -le 0 ]
  then
    echo "Docker image version is mandatory."
    exit 1
fi

APP_VERSION=$1

echo "Building recipe-manager application : $APP_VERSION"

mvn -X clean install -U -f pom.xml

docker build -t amulrajesh/recipe-manager:${APP_VERSION}  .

docker push amulrajesh/recipe-manager:${APP_VERSION}