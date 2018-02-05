package com.sagebear.bigrussianboss

import java.util.Locale

import com.github.javafaker.Faker
import com.sagebear.bigrussianboss.Script._
import com.sagebear.bigrussianboss.bot.{Cli, RuleBased}
import com.sagebear.bigrussianboss.intent.Intents._
import org.scalatest.FlatSpec

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.Random

class ScriptTest extends FlatSpec {
  private val script = примеры(
   Пример(
      Клиент приветствует,
      Оператор приветствует,
      Клиент спрашивает Вопрос_про_покупку_пива,
      Оператор спрашивает Вопрос_про_адрес,
      Клиент говорит Информацию_про_свой_адрес,
      Оператор говорит Информацию_где_купить_пиво,
      Оператор прощается,
      Клиент прощается
    ),
    Пример(
      Клиент приветствует,
      Оператор приветствует,
      Клиент спрашивает Вопрос_про_покупку_пива,
      Оператор спрашивает (Вопрос_про_телефон и Вопрос_про_адрес),
      Клиент говорит Информацию_про_свой_адрес,
      Оператор спрашивает Вопрос_про_телефон,
      Клиент говорит Глупости,
      Оператор спрашивает Вопрос_про_телефон,
      Клиент говорит Информацию_про_свой_телефон,
      Оператор говорит Информацию_где_купить_пиво,
      Оператор прощается,
      Клиент прощается
    )
  )

  private implicit val rnd: Random = new Random(0)
  private val faker = new Faker(new Locale("ru"))
  private val clientAddress = faker.address().streetAddress()
  private val clientPhone = faker.phoneNumber().cellPhone()
  private val client = RuleBased.client(clientAddress, clientPhone).get
  private val operator = RuleBased.operator.get
  private val operatorH = new Cli

  it should "work for robots" in {
    assert(Await.result(script.execute(client, operator), 1.hour) ===
      s""">> чо как
         |:: чо как
         |>> где найти пива?
         |:: скажи свой адрес
         |>> я живу на ${clientAddress.toLowerCase}
         |:: иди в ближайший к ${clientAddress.toLowerCase} ларек
         |:: прощай
         |>> прощай
         |""".stripMargin)
  }
}
