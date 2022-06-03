package af.samp.integ.service

import af.samp.integ.int.SimpleGateway
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.integration.annotation.ServiceActivator
import org.springframework.integration.dsl.IntegrationFlowAdapter
import org.springframework.integration.dsl.IntegrationFlowBuilder
import org.springframework.integration.dsl.MessageChannels
import org.springframework.messaging.Message
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.stereotype.Service
import kotlin.random.Random

data class GeneratedName(
  val request: String,
  val reply: String,
  val timestamp: Long
)

@Service
class SimpleService : IntegrationFlowAdapter() {
  private val logger = LoggerFactory.getLogger(SimpleService::class.java)
  private val names = arrayOf("name1", "name2", "name3")
  private val random = Random(System.currentTimeMillis())
  private val mapper = ObjectMapper()
  private val executor = ThreadPoolTaskExecutor().apply {
    corePoolSize = 16
    maxPoolSize = 16
    setThreadNamePrefix("name-t-")
    initialize()
  }

  override fun buildFlow(): IntegrationFlowBuilder {
    return from(SimpleGateway::class.java)
      .wireTap(MessageChannels.publishSubscribe("loggerChannel", executor).get())
      .channel(MessageChannels.publishSubscribe("nameRequestChannel", executor).datatype(String::class.java))
      .routeToRecipients { r ->
        r.applySequence(true)
          .recipient(MessageChannels.publishSubscribe("jsonChannel", executor).get())
      }
  }

  @ServiceActivator(inputChannel = "nameRequestChannel", outputChannel = "jsonChannel")
  fun getRandomName(@Payload name: String): GeneratedName {
    logger.debug("getRandomName: {}", name)
    Thread.sleep(random.nextInt(200).toLong())
    return GeneratedName(name, names[random.nextInt(3)], System.currentTimeMillis())
  }

  @ServiceActivator(inputChannel = "jsonChannel")
  fun convertToJson(@Payload generatedName: GeneratedName): String {
    logger.debug("convertToJson: {}", generatedName)
    return mapper.writeValueAsString(generatedName)
  }

  @ServiceActivator(inputChannel = "loggerChannel")
  fun channelLogger(@Payload message: Message<*>) {
    logger.debug("logging: {}", message)
    Thread.sleep(150)
  }
}
