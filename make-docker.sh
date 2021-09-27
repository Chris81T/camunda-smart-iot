#!/bin/bash

echo "Clean up target/camunda-smart-iot..."
rm -rf target/camunda-smart-iot
mkdir target/camunda-smart-iot

echo "Build camunda-smart-iot..."
mvn install -f ./pom.xml

echo "Copy Dockerfile to target/camunda-smart-iot..."
cp ./src/docker/Dockerfile target/camunda-smart-iot/

echo "Build Docker image"
docker build -f ./target/camunda-smart-iot/Dockerfile -t camunda-smart-iot-platform .

echo "Export generated Docker image to to target..."
docker save camunda-smart-iot-platform:latest > target/camunda-smart-iot/camunda-smart-iot-platform.tar

echo "Building docker image camunda-smart-iot-platform finished :-)"