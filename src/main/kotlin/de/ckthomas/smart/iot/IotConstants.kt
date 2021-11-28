package de.ckthomas.smart.iot

/**
 * Author: Christian Thomas
 * Created on: 28.09.2021
 *
 * Check license details @ project root
 */
object IotConstants {

    object ConnectorIds {
        const val ID_COMMON = "hassio-common"
        const val ID_SWITCH = "hassio-switch"
        const val ID_COVER = "hassio-cover"
        const val ID_LIGHT = "hassio-light"
    }

    object Common {
        const val AUTH_KEY = "authKey"
        const val AUTH_VAL = "authValue"
        const val BASE_PATH = "basePath"
        const val PATH_SERVICES = "services"
        const val KEY_URL_PATH = "path"
        const val KEY_URL_DOMAIN = "domain"
        const val KEY_URL_SERVICE = "service"
        const val KEY_JSON_BODY = "jsonBody"
    }

    object Switch {
        const val DOMAIN = "switch"
        const val JSON_BODY_ENTITY_ID = "entity_id"
    }

    object Light {
        const val DOMAIN = "light"
        const val JSON_BODY_ENTITY_ID = "entity_id"
    }

    object Cover {
        const val DOMAIN = "cover"
        const val JSON_BODY_ENTITY_ID = "entity_id"
    }

    object EnginePlugin {
        const val MQTT_TOPIC_SEPERATOR = ","
        const val MQTT_SERVER_URI = "mqttServerURI"
        const val MQTT_USERNAME = "mqttUser"
        const val MQTT_PASSWORD = "mqttPassword"
        const val MQTT_TO_BPMN_SIGNAL_TOPIC = "mqttToBpmnSignalTopic"
        const val MQTT_PROCESS_START_TOPIC = "mqttProcessStartTopic"
        const val MQTT_PROCESS_START_TOPIC_DEFAULT = "camundahassio/processstart"
    }

    object EngineListener {
        const val ELEM_SIGNAL_REF = "signalRef"
        const val ELEM_SIGNAL_NAME = "name"

        /**
         * while using a signal boundary/catch event, it is useful to set the resultVariable (name of the final process
         * variable, that will be created). Only, if a complex JSON is given, that values will be as process variables
         * interpreted.
         */
        const val EXT_PROP_RESULT_VAR_NAME = "resultVariable"

        /**
         * if no resultVariable as extension property is set, following fallback will be used.
         */
        const val EXT_PROP_FALLBACK_VAR_NAME = "mqttResultVariable"

        /**
         * E.g. a primitive value will set as process variable with that name, when the process via signal will be
         * started.
         */
        const val SIGNAL_START_RESULT_VAR_NAME = "mqttInputVariable"
    }
}