package com.twitter.finaglezk

import com.twitter.conversions.time._
import com.twitter.ostrich.admin._
import com.twitter.scalatest.TestLogging
import com.twitter.util._
import org.scalatest._

abstract class AbstractSpec extends FunSpec with TestLogging {
  lazy val env = RuntimeEnvironment(this, Array("-f", "config/test.scala"))

  lazy val finaglezk = {
    val out = env.loadRuntimeConfig[FinaglezkService.ThriftServer]

    // You don't really want the thrift server active, particularly if you
    // are running repetitively via ~test
    ServiceTracker.shutdown() // all services
    out
  }
}
