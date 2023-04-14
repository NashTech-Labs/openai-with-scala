package com.knoldus.services

import io.cequence.openaiscala.domain.response.ModelInfo

import scala.concurrent.Future

trait CompletionService {
  def listAllModels(): Future[Seq[ModelInfo]]

  def getFirstCompletion(text: String): Future[Either[String, String]]

  def getNCompletions(text: String, n: Int): Future[Either[String, Seq[String]]]
}
