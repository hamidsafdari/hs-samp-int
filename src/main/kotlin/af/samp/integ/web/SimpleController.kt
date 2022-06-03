package af.samp.integ.web

import af.samp.integ.int.SimpleGateway
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/simple")
class SimpleController @Autowired constructor(private val simpleGateway: SimpleGateway) {
  @GetMapping("/request1/{name}")
  fun request1(@PathVariable name: String): Int {
    return simpleGateway.getName(name)
  }
}
