package aps.backflip.curlylab_gateway

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class FallbackController {

    @GetMapping("/fallback/user")
    fun userFallback() = ResponseEntity
        .status(HttpStatus.SERVICE_UNAVAILABLE)
        .body("User Service is unavailable")

    @GetMapping("/fallback/product")
    fun productFallback() = ResponseEntity
        .status(HttpStatus.SERVICE_UNAVAILABLE)
        .body("Product Service is unavailable")

    @GetMapping("/fallback/blog")
    fun blogFallback() = ResponseEntity
        .status(HttpStatus.SERVICE_UNAVAILABLE)
        .body("Blog Service is unavailable")

    @GetMapping("/fallback/composition")
    fun compositionFallback() = ResponseEntity
        .status(HttpStatus.SERVICE_UNAVAILABLE)
        .body("Composition Service is unavailable")

    @GetMapping("/fallback/hairtyping")
    fun typingFallback() = ResponseEntity
        .status(HttpStatus.SERVICE_UNAVAILABLE)
        .body("Hairtyping Service is unavailable")
}