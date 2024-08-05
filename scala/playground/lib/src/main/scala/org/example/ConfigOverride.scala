package org.example

class ConfigOverride {
  def default: Map[String, String] = Map("url" -> "default", "dbName" -> "defaultDB")

  def overrideConfig(overrideConf: Map[String,String] = Map.empty): Map[String,String] = {
    default ++ overrideConf
  }

  def getDefaultConf() : Map[String,String] = {
    default
  }
}
