package af.samp.integ.service

import kotlinx.coroutines.DEBUG_PROPERTY_NAME
import org.slf4j.LoggerFactory
import org.springframework.integration.annotation.ServiceActivator
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Service
import kotlin.math.log
import kotlin.random.Random

@Service
class SimpleService {
  private val logger = LoggerFactory.getLogger(SimpleService::class.java)
  private val names = arrayOf("name1", "name2", "name3")
  private val random = Random(System.currentTimeMillis())

  @ServiceActivator(inputChannel = "nameRequestChannel")
  fun getRandomName(@Payload name: String): String {
    logger.debug("service")
    Thread.sleep(random.nextInt(200).toLong())
    return "${System.currentTimeMillis()}| request: $name | reply: ${names.get(random.nextInt(3))}"
  }
}
