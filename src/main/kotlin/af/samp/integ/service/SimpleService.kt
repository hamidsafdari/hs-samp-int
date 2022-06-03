package af.samp.integ.service

import af.samp.integ.int.SimpleGateway
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

@Service
class SimpleService : IntegrationFlowAdapter() {
  private val logger = LoggerFactory.getLogger(SimpleService::class.java)
  private val names = arrayOf("name1", "name2", "name3")
  private val random = Random(System.currentTimeMillis())
  private val executor = ThreadPoolTaskExecutor().apply {
    corePoolSize = 16
    maxPoolSize = 16
    setThreadNamePrefix("name-t-")
    initialize()
  }

  override fun buildFlow(): IntegrationFlowBuilder {
    return from(SimpleGateway::class.java)
      .channel(MessageChannels.publishSubscribe("nameRequestChannel", executor).datatype(String::class.java))
      .wireTap(MessageChannels.publishSubscribe("loggerChannel", executor).get())
  }

  @ServiceActivator(inputChannel = "nameRequestChannel")
  fun getRandomName(@Payload name: String): String {
    logger.debug("service")
    Thread.sleep(random.nextInt(200).toLong())
    return "${System.currentTimeMillis()}| request: $name | reply: ${names[random.nextInt(3)]}"
  }

  @ServiceActivator(inputChannel = "loggerChannel")
  fun channelLogger(@Payload message: Message<*>) {
    Thread.sleep(150)
    logger.debug("logging: {}", message)
  }
}
