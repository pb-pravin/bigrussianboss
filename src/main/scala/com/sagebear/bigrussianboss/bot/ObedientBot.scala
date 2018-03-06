package com.sagebear.bigrussianboss.bot

import com.sagebear.bigrussianboss.Script
import com.sagebear.bigrussianboss.bot.SensorsAndActuators.CanNotDoThis

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

class ObedientBot(private var phrases: List[String]) extends SensorsAndActuators {
  override def observe(text: String)(a: Script.Action)(implicit ec: ExecutionContext): Future[SensorsAndActuators] = {
    Future(new ObedientBot(phrases))
  }

  override def act(a: Script.Action)(implicit ec: ExecutionContext): Future[String] = {
    this.phrases match {
      case head :: tail =>
        this.phrases = tail
        Future(head)
      case _ =>
        Future.failed(CanNotDoThis)
    }
  }
}

object ObedientBot {
  def client(phrases: String*): Try[ObedientBot] = Try(new ObedientBot(phrases.toList))

  def operator(phrases: String*): Try[ObedientBot] = Try(new ObedientBot(phrases.toList))
}