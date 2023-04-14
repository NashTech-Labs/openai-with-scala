import akka.actor.ActorSystem
import akka.stream.Materializer
import com.knoldus.services.CompletionServiceImpl
import com.typesafe.config.ConfigFactory
import io.cequence.openaiscala.domain.{ChatRole, MessageSpec, ModelId}
import io.cequence.openaiscala.service.OpenAIServiceFactory
import io.cequence.openaiscala.service.Tag.{max_tokens, model}
import io.cequence.openaiscala.domain.settings.CreateChatCompletionSettings

import java.io.File
import scala.annotation.unused
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, ExecutionContext}

object Driver extends App {
  implicit val openAIActorSystem: ActorSystem = ActorSystem("openai")
  implicit val executionContext: ExecutionContext = openAIActorSystem.dispatcher
  implicit val materializer: Materializer = Materializer(openAIActorSystem)

  private val config = ConfigFactory.parseFile(new File("src/main/resources/conf/openai-scala-client.conf"))

  val openAIService = OpenAIServiceFactory(config)

  private val service = new CompletionServiceImpl(openAIService)

  @unused
  private val text =
    """Extract the name and mailing address from this email:
      |Dear Kelly,
      |It was great to talk to you at the seminar. I thought Jane's talk was quite good.
      |Thank you for the book. Here's my address 2111 Ash Lane, Crestview CA 92002
      |Best,
      |Maya
    """.stripMargin


  private val question =
    """
      |What is Passover festival?
      |""".stripMargin

  println(Await.result(service.getFirstCompletion(question), 30.second).map(_.replace(". ", ".\n")))
  println(Await.result(service.getNCompletions(question, 5), 60.second).map(_.map(_.replace(". ", ".\n")).mkString("\n\n\n")))
  openAIActorSystem.terminate().map(_ => sys.exit(1))
}
