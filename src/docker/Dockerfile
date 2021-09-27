FROM camunda/camunda-bpm-platform:run-7.15.0
LABEL Christian Thomas <christian@ckthomas.de>

# copy hassio engine plugin in lib folder (activated over bpm-platform.xml)
COPY ./target/camunda-smart-iot-1.0.0-SNAPSHOT.jar ./configuration/userlib/camunda-smart-iot.jar

# copy serveral 3rd party libs like kotlin and Co
COPY ./target/lib/ ./configuration/userlib/