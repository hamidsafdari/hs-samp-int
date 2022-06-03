package af.samp.integ.service

import org.springframework.integration.annotation.ServiceActivator
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Service

@Service
class SimpleService {
  private val names = arrayOf("name1", "name2", "name3")

  @ServiceActivator(inputChannel = "nameRequestChannel")
  fun getRandomName(@Payload name: String): Int {
    return names.indexOf(name)
  }
}
