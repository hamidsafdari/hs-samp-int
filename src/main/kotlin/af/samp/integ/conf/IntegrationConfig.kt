package af.samp.integ.conf

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.annotation.IntegrationComponentScan
import org.springframework.integration.annotation.ServiceActivator
import org.springframework.integration.config.EnableIntegration
import org.springframework.integration.dsl.MessageChannels
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

@Configuration
@EnableIntegration
@IntegrationComponentScan("af.samp.integ.int")
class IntegrationConfig @Autowired constructor(private val appContext: ApplicationContext) {
  private val logger = LoggerFactory.getLogger(IntegrationConfig::class.java)
  private val executor = ThreadPoolTaskExecutor().apply {
    corePoolSize = 3
    maxPoolSize = 3
    setThreadNamePrefix("name-t-")
    initialize()
  }

  @Bean
  fun nameRequestChannel(): MessageChannel = MessageChannels.direct()
    .wireTap(loggerChannel())
    .get()

  @Bean
  fun loggerChannel(): MessageChannel = MessageChannels.publishSubscribe().get()

  @ServiceActivator(inputChannel = "loggerChannel")
  fun channelLogger(@Payload message: Message<*>) {
    logger.debug("processing message: {}", message)
  }
}
