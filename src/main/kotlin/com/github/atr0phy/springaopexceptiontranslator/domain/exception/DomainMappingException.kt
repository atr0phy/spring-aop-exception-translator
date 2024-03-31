package com.github.atr0phy.springaopexceptiontranslator.domain.exception

class DomainMappingException(
    ex: DomainException?,
): RuntimeException(ex)
