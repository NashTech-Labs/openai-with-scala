package com.knoldus.utils

object Conversions {
  def convertSeqEither(input: Seq[Either[String, String]]): Either[String, Seq[String]] = {
    input.foldRight[Either[String, List[String]]](Right(List.empty)) {
      case (Right(str), Right(acc)) => Right(str :: acc)
      case (Left(err), _) => Left(err)
      case (_, Left(err)) => Left(err)
    }.map(_.reverse)
  }
}
