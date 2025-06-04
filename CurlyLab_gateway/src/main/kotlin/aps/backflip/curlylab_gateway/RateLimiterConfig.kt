package aps.backflip.curlylab_gateway

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class RateLimiterConfig {
    @Bean
    fun remoteAddrKeyResolver(): KeyResolver {
        return KeyResolver { exchange ->
            Mono.just(exchange.request.remoteAddress?.address?.hostAddress ?: "global")
        }
    }
}