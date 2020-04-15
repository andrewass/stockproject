package com.stockproject.configuration

import com.stockproject.configuration.util.PasswordEncoder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
class BeanConfiguration {

    @Bean
    fun restTemplate() = RestTemplate()

    @Bean
    fun passwordEncoder() = PasswordEncoder()
}