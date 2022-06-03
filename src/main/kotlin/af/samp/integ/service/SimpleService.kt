package af.samp.integ.service

import org.springframework.integration.annotation.ServiceActivator
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Service
import kotlin.random.Random

@Service
class SimpleService {
  private val names = arrayOf("name1", "name2", "name3")
  private val random = Random(System.currentTimeMillis())

  @ServiceActivator(inputChannel = "nameRequestChannel")
  fun getRandomName(@Payload name: String): String {
    Thread.sleep(random.nextInt(200).toLong())
    return "${System.currentTimeMillis()}| request: $name | reply: ${names.get(random.nextInt(3))}"
  }
}
