package com.github.atr0phy.springaopexceptiontranslator.presentation

import com.github.atr0phy.springaopexceptiontranslator.domain.exception.DomainException
import com.github.atr0phy.springaopexceptiontranslator.domain.exception.DomainMappingException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class ExceptionHandler {
    private val logger = LoggerFactory.getLogger(this::class.qualifiedName)

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DomainException::class)
    @ResponseBody
    fun domainExceptionHandler(exception: DomainException): Map<String, String?> {
        return mapOf(
            "message" to exception.message,
        )
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(DomainMappingException::class)
    @ResponseBody
    fun domainMappingExceptionHandler(exception: DomainMappingException): Map<String, String?> {
        logger.warn("DB上に不正なデータが存在します、 ${exception.cause?.message}")
        return mapOf(
            "message" to "内部エラーが発生しました",
        )
    }
}
