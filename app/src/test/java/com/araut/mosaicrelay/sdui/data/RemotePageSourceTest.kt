package com.araut.mosaicrelay.sdui.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.IOException

class RemotePageSourceTest {

    private lateinit var server: MockWebServer

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start()
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `successful response returns body`() = runBlocking {
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("""{"pageId":"home"}"""),
        )

        val source = RemotePageSource(
            client = OkHttpClient(),
            url = server.url("/home.json").toString(),
            dispatcher = Dispatchers.Unconfined,
        )

        assertEquals(
            """{"pageId":"home"}""",
            source.read(),
        )
    }

    @Test(expected = IOException::class)
    fun `unsuccessful response throws IOException`(): Unit = runBlocking {
        server.enqueue(
            MockResponse()
                .setResponseCode(503),
        )

        val source = RemotePageSource(
            client = OkHttpClient(),
            url = server.url("/home.json").toString(),
            dispatcher = Dispatchers.Unconfined,
        )

        source.read()
    }
}