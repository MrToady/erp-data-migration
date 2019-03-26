package com.organization.data.config

import com.typesafe.config.ConfigFactory

case class AppConfig(dBConfig: DBConfig)

object AppConfig {
  def apply(): AppConfig = pureconfig.loadConfigOrThrow[AppConfig](ConfigFactory.load())
}
