package com.github.atr0phy.springaopexceptiontranslator

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
class SpringAopExceptionTranslatorApplication

fun main(args: Array<String>) {
    runApplication<SpringAopExceptionTranslatorApplication>(*args)
}
