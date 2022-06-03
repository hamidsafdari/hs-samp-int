package af.samp.integ.conf

import org.springframework.context.annotation.Configuration
import org.springframework.integration.annotation.IntegrationComponentScan
import org.springframework.integration.config.EnableIntegration

@Configuration
@EnableIntegration
@IntegrationComponentScan("af.samp.integ.int")
class IntegrationConfig
