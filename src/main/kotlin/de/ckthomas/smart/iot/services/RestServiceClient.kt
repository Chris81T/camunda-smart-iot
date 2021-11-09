package de.ckthomas.smart.iot.services

import okhttp3.Request.Builder

import okhttp3.*
import org.slf4j.LoggerFactory

import kotlin.Throws
import java.io.IOException

/**
 * This facade provides common logic for a simpler usage.
 * @author Christian Thomas
 */
class RestServiceClient
/**
 * Use the factory to get an instance of this.
 */ internal constructor(private val basePath: String?, private val authKey: String?, private val authValue: String?) {
    /**
     * use only one instance of the http-client.
     */
    private val httpClient = OkHttpClient()
    fun createRequestBuilder(url: String): Builder {
        val prefix = if (basePath != null) "$basePath/" else ""
        val finalUrl = prefix + url
        LOGGER.info("Using final url = {} for creating request builder", finalUrl)
        val builder: Builder = Builder()
            .url(finalUrl)
        if (authKey != null && authValue != null) {
            LOGGER.info("Adding header with authKey = {}, authValue = {}", authKey, "***secret***")
            builder.addHeader(authKey, authValue)
        }
        return builder
    }

    fun createPostRequest(url: String, jsonBody: String?): Request {
        return createRequestBuilder(url)
            .post(RequestBody.create(jsonBody, JSON))
            .build()
    }

    fun createCall(request: Request?): Call {
        return httpClient.newCall(request!!)
    }

    @Throws(IOException::class)
    fun execute(request: Request?): Response {
        return createCall(request).execute()
    }

    @Throws(IOException::class)
    fun execute(url: String, jsonBody: String?): Response {
        return execute(createPostRequest(url, jsonBody))
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(RestServiceClient::class.java)
        val JSON: MediaType = get.get("application/json; charset=utf-8")
    }
}