package af.samp.integ.web

import af.samp.integ.int.SimpleGateway
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/simple")
class SimpleController @Autowired constructor(private val simpleGateway: SimpleGateway) {
  private val logger = LoggerFactory.getLogger(SimpleController::class.java)

  @GetMapping("/request1/{name}")
  fun request1(@PathVariable name: String): Mono<String> {
    logger.debug("controller")
    return simpleGateway.getName(name)
  }
}
