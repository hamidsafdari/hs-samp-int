package af.samp.integ.int

import org.springframework.integration.annotation.Gateway
import org.springframework.integration.annotation.MessagingGateway

@MessagingGateway
interface SimpleGateway {
  @Gateway(requestChannel = "nameRequestChannel", replyTimeout = 3000)
  fun getName(name: String): Int
}
