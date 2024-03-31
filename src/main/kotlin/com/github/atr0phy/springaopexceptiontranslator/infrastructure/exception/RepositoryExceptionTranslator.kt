package com.github.atr0phy.springaopexceptiontranslator.infrastructure.exception

import com.github.atr0phy.springaopexceptiontranslator.domain.exception.DomainException
import com.github.atr0phy.springaopexceptiontranslator.domain.exception.DomainMappingException
import org.aspectj.lang.annotation.AfterThrowing
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component

@Aspect
@Component
class RepositoryExceptionTranslator {
    @AfterThrowing(value = "@within(org.springframework.stereotype.Repository)", throwing = "e")
    fun translate(e: Throwable) {
        throw when(e) {
            is DomainException -> DomainMappingException(e)
            else -> e
        }
    }
}
