package com.example.albumphotos.domain.extension

import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.api.extension.ParameterResolver
import org.junit.jupiter.api.extension.TestInstancePostProcessor

abstract class TestExtension : TestInstancePostProcessor, ParameterResolver {

    override fun postProcessTestInstance(testInstance: Any?, context: ExtensionContext?) {
        // no-op
    }

    override fun supportsParameter(parameterContext: ParameterContext?, extensionContext: ExtensionContext?): Boolean {
        // no-op
        return false
    }

    override fun resolveParameter(parameterContext: ParameterContext?, extensionContext: ExtensionContext?): Any? {
        // no-op
        return null
    }
}
