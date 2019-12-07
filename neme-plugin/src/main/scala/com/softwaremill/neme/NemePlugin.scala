package com.softwaremill.neme

import scala.tools.nsc.plugins.{Plugin, PluginComponent}
import scala.tools.nsc.Global

class NemePlugin(val global: Global) extends Plugin {
  plugin =>

  val name = "neme"
  val description = "Scala compiler plugin for turning non exhaustive match warnings into errors"
  val components: List[PluginComponent] = List.empty

  private lazy val reporter =
    new NemeReporter(global.reporter)

  override def init(options: List[String], error: String => Unit): Boolean = {
    global.reporter = reporter
    true
  }
}
