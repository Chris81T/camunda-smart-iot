FROM camunda/camunda-bpm-platform:run-7.16.0
LABEL Christian Thomas <christian@ckthomas.de>

# remove example app
RUN rm -rf ./internal/example/camunda-bpm-run-modules-example-invoice.jar

# TODO - remove intial DEMO DEMO !!!!

# copy hassio engine plugin in lib folder (activated over bpm-platform.xml)
COPY ./target/camunda-smart-iot-1.0.0-SNAPSHOT.jar ./configuration/userlib/camunda-smart-iot.jar

# copy custom configuration
COPY ./src/docker/configuration/ ./configuration/

# copy serveral 3rd party libs like kotlin and Co
COPY ./target/lib/ ./configuration/userlib/