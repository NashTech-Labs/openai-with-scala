package com.knoldus.services

import io.cequence.openaiscala.domain.response.ModelInfo
import io.cequence.openaiscala.domain.settings.CreateCompletionSettings
import io.cequence.openaiscala.service.OpenAIService
import io.cequence.openaiscala.service.OpenAIServiceFactory.DefaultSettings

import scala.annotation.tailrec
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import com.knoldus.utils.Conversions._

class CompletionServiceImpl(service: OpenAIService) extends CompletionService {
  override def listAllModels(): Future[Seq[ModelInfo]] = {
    service.listModels
  }

  override def getFirstCompletion(text: String): Future[Either[String, String]] = {
    service.createCompletion(text) map { response =>
      response.choices.toList match {
        case ::(head, _) => Right(head.text)
        case Nil => Left("Got No response")
      }
    }
  }

  override def getNCompletions(text: String, n: Int): Future[Either[String, Seq[String]]] = {
    Future.sequence(executeNTimes(n, text)(service.createCompletion)).map { responses =>
      responses.map { response =>
        response.choices.toList match {
          case head :: _ => Right(head.text)
          case Nil => Left("Got No response")
        }
      }
    }.map(convertSeqEither)
  }

  @tailrec
  private[this] def executeNTimes[T](n: Int, text: String)(f: (String, CreateCompletionSettings) => Future[T], accumulator: Seq[Future[T]] = Nil): Seq[Future[T]] = {
    if (n <= 0) accumulator
    else executeNTimes(n - 1, text)(f, accumulator :+ f(text, DefaultSettings.CreateCompletion))
  }
}
