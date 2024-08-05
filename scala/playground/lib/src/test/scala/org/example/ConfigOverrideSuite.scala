package org.example

import org.junit.Assert.assertEquals
import org.scalatest.funsuite.AnyFunSuite
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ConfigOverrideSuite extends AnyFunSuite {
  test("should override") {
    def testClass = new ConfigOverride()

    val conf = Map("url" -> "overrideUrl", "dbName" -> "overrideDB")

    testClass.overrideConfig(conf).map { case (k, v) => assertEquals(conf(k), v) }
  }

  test("should not override if override config is not set") {
    def testClass = new ConfigOverride()

    val defaultConf = testClass.getDefaultConf()
    testClass.overrideConfig().map { case (k, v) => assertEquals(defaultConf(k), v) }
  }

  test("stringIntMapToStringStringMap") {
    def library = new Library()

    def testM = Map("hello" -> 0, "world" -> 1)

    assertEquals(Map("hello" -> "0", "world" -> "1"), library.stringIntMapToStringStringMap(testM))
  }
}

