package utils

import com.amazonaws.ClientConfiguration

object ConfigurationUtils {
  val APPLICATION_NAME = "amazon-kinesis-learning"
  val VERSION = "1.0.0"

  def getClientConfigWithUserAgent(): ClientConfiguration = {
    val config = new ClientConfiguration()
    val userAgent = new StringBuilder(ClientConfiguration.DEFAULT_USER_AGENT)
    userAgent.append(" ")
    userAgent.append(APPLICATION_NAME)
    userAgent.append("/")
    userAgent.append(VERSION)
    config.setUserAgentPrefix(userAgent.toString())
    config
  }
}
