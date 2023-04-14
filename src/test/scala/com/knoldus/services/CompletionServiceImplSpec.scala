package com.knoldus.services

import akka.actor.ActorSystem
import akka.stream.Materializer
import com.typesafe.config.ConfigFactory
import io.cequence.openaiscala.service.{OpenAIService, OpenAIServiceFactory}
import org.mockito.Mockito.mock
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers

import java.io.File
import scala.concurrent.{Await, ExecutionContext}
import scala.concurrent.duration.DurationInt

class CompletionServiceImplSpec extends AnyFlatSpec with Matchers {

  private def init(): CompletionServiceImpl = {
    implicit val openAIActorSystem: ActorSystem = ActorSystem("openai")
    implicit val executionContext: ExecutionContext = openAIActorSystem.dispatcher
    implicit val materializer: Materializer = Materializer(openAIActorSystem)

    val config = ConfigFactory.parseFile(new File("src/main/resources/conf/openai-scala-client.conf"))
    val openAIService = OpenAIServiceFactory(config)
    val service = new CompletionServiceImpl(openAIService)

    service
  }

  private val serviceImpl = init()

  "listAllModels" should "return a non-empty list of models" in {
    val result = Await.result(serviceImpl.listAllModels(), 30.seconds)

    result.size must be > 1
  }

  "getFirstCompletion" should "return a single response" in {
    val text = "My Name is Prakhar. Please wish me good morning using my name"
    val result = Await.result(serviceImpl.getFirstCompletion(text), 30.seconds)

    result.isRight must be(true)
  }

  "getNCompletion" should "return more than one response" in {
    val question = "Who are Jewish people?"
    val numberOfResponse = 5
    val result = Await.result(serviceImpl.getNCompletions(question, numberOfResponse), 30.seconds)

    result.isRight must be(true)
    result.map(_.size) must be(Right(numberOfResponse))
  }
}
