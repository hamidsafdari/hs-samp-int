package af.samp.integ.int

import org.springframework.integration.annotation.Gateway
import org.springframework.integration.annotation.MessagingGateway
import reactor.core.publisher.Mono

@MessagingGateway
interface SimpleGateway {
  @Gateway(requestChannel = "nameRequestChannel", replyTimeout = 3000)
  fun getName(name: String): Mono<String>
}
