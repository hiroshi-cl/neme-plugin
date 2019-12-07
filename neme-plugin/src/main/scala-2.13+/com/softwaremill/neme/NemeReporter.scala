package com.softwaremill.neme

import scala.reflect.internal.util.Position
import scala.tools.nsc.Settings
import scala.tools.nsc.reporters.FilteringReporter
import scala.util.matching.Regex

class NemeReporter(original: FilteringReporter, patterns: Seq[Regex]) extends FilteringReporter {

  override def doReport(pos: Position, msg: String, severity: Severity): Unit = {
    severity match {
      case INFO =>
        original.doReport(pos, msg, severity)
      case WARNING if patterns.exists(_.findFirstIn(msg).isDefined) =>
        original.error(pos, msg)
      case WARNING =>
        original.warning(pos, msg)
      case ERROR => original.error(pos, msg)
    }
  }

  override def settings: Settings = original.settings
}
