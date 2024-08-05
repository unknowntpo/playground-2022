package org.example

class ConfigOverride {
  def default: Map[String, String] = Map("url" -> "default", "dbName" -> "defaultDB")

  def overrideConfig(overrideConf: Map[String,String]): Map[String,String] = {
    default ++ overrideConf
  }
}
