package com.example.albumphotos.data.generic

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import kotlinx.datetime.Clock
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

internal class CachedValueTest {

    @BeforeTest
    fun setup() {
        mockkObject(Clock.System)
    }

    @AfterTest
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `Given that cache duration is exactly reached, When checking expiration, Then return false`() {
        // Given
        val randomTimeMs = 100_000L
        val maxDurationMs = 900_000L

        // Setting time
        every { Clock.System.now().toEpochMilliseconds() } returns randomTimeMs

        // When
        val result = CachedValue("")

        // Advancing time
        every { Clock.System.now().toEpochMilliseconds() } returns (randomTimeMs + maxDurationMs)

        // Then
        assertFalse(result.isExpired(false))
    }

    @Test
    fun `Given that cache duration is just elapsed, When checking expiration, Then return true`() {
        // Given
        val randomTimeMs = 100_000L
        val maxDurationMs = 900_000L

        // Setting time
        every { Clock.System.now().toEpochMilliseconds() } returns randomTimeMs

        // When
        val result = CachedValue("")

        // Advancing time
        every { Clock.System.now().toEpochMilliseconds() } returns (randomTimeMs + maxDurationMs + 1)

        // Then
        assertTrue(result.isExpired(false))
    }

    @Test
    fun `Given that cache duration is exactly reached, When taking value, Then return value`() {
        // Given
        val randomTimeMs = 100_000L
        val maxDurationMs = 900_000L

        // Setting time
        every { Clock.System.now().toEpochMilliseconds() } returns randomTimeMs

        // When
        val result = CachedValue("")
        every { Clock.System.now().toEpochMilliseconds() } returns (randomTimeMs + maxDurationMs)

        // Then
        assertEquals(
            expected = result.takeValue(false),
            actual = ""
        )
    }

    @Test
    fun `Given that cache duration is just elapsed, When taking value, Then return null`() {
        // Given
        val randomTimeMs = 100_000L
        val maxDurationMs = 900_000L

        // Setting time
        every { Clock.System.now().toEpochMilliseconds() } returns randomTimeMs

        // When
        val result = CachedValue("")

        // Advancing time
        every { Clock.System.now().toEpochMilliseconds() } returns (randomTimeMs + maxDurationMs + 1)

        // Then
        assertNull(result.takeValue(false))
    }
}
