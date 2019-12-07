package com.softwaremill.neme

import scala.tools.nsc.plugins.{Plugin, PluginComponent}
import scala.tools.nsc.Global

class NemePlugin(val global: Global) extends Plugin {
  plugin =>

  val name = "neme"
  val description = "Scala compiler plugin for turning non exhaustive match warnings into errors"
  val components: List[PluginComponent] = List.empty

  private val defaultPattern = "match may not be exhaustive.".r

  override def init(options: List[String], error: String => Unit): Boolean =
    try {
      val patterns = options
        .map(_.split("=", 2))
        .collect({
          case Array("regex", pattern) =>
            pattern.split(';').iterator.map(_.r)
        })
        .flatten

      global.reporter = new NemeReporter(global.reporter, if (patterns.isEmpty) Seq(defaultPattern) else patterns)
      true
    } catch {
      case _: Throwable => false
    }

  override val optionsHelp: Option[String] = Some(
    """  -P:neme:regex=...  Semicolon separated regexes for filtering warning messages globally
    """.stripMargin
  )
}
