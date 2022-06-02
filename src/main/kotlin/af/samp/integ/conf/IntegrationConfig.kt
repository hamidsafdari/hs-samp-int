package af.samp.integ.conf

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.annotation.InboundChannelAdapter
import org.springframework.integration.annotation.Poller
import org.springframework.integration.annotation.ServiceActivator
import org.springframework.integration.channel.interceptor.WireTap
import org.springframework.integration.config.EnableIntegration
import org.springframework.integration.dsl.MessageChannels
import org.springframework.integration.file.FileReadingMessageSource
import org.springframework.integration.file.FileWritingMessageHandler
import org.springframework.integration.file.filters.SimplePatternFileListFilter
import org.springframework.integration.file.support.FileExistsMode
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.handler.annotation.Payload
import java.io.File

@Configuration
@EnableIntegration
class IntegrationConfig @Autowired constructor(private val appContext: ApplicationContext) {
  private val logger = LoggerFactory.getLogger(IntegrationConfig::class.java)

  init {
    val inputDir = File(INPUT_DIR)
    if (!inputDir.exists()) {
      inputDir.mkdirs()
    }
    val outputDir = File(OUTPUT_DIR)
    if (!outputDir.exists()) {
      outputDir.mkdirs()
    }
  }

  @Bean
  fun fileChannel(): MessageChannel {
    return MessageChannels.publishSubscribe()
      .wireTap(loggerChannel())
      .get()
  }

  @Bean
  fun loggerChannel(): MessageChannel = MessageChannels.publishSubscribe().get()

  @ServiceActivator(inputChannel = "loggerChannel")
  fun databaseLogger(@Payload message: Message<*>) {
    logger.debug("database logging message: {}", message)
  }

  @Bean
  @InboundChannelAdapter(value = "fileChannel", poller = [Poller(fixedDelay = "1000")])
  fun fileReadingMessageSource() = FileReadingMessageSource().apply {
    setDirectory(File(INPUT_DIR))
    setFilter(SimplePatternFileListFilter(FILE_PATTERN))
  }

  @Bean
  @ServiceActivator(inputChannel = "fileChannel")
  fun fileWritingMessageHandler() = FileWritingMessageHandler(File(OUTPUT_DIR)).apply {
    setFileExistsMode(FileExistsMode.REPLACE)
    setExpectReply(false)
  }

  companion object {
    private const val INPUT_DIR = "the_source_dir"
    private const val OUTPUT_DIR = "the_dest_dir"
    private const val FILE_PATTERN = "*.jpg"
  }
}
