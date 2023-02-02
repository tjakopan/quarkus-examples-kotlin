package com.example

import io.kotest.matchers.shouldBe
import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer
import io.quarkus.test.junit.QuarkusTest
import io.smallrye.common.annotation.Identifier
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import java.util.*
import javax.inject.Inject
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.toJavaDuration

@QuarkusTest
class QuotesProcessorTest {
    @Inject
    @Identifier("default-kafka-broker")
    private lateinit var kafkaConfig: Map<String, Any>

    private lateinit var quoteRequestProducer: KafkaProducer<String, String>
    private lateinit var quoteConsumer: KafkaConsumer<String, Quote>

    @BeforeTest
    fun setUp() {
        quoteConsumer =
            KafkaConsumer(consumerConfig(), StringDeserializer(), ObjectMapperDeserializer(Quote::class.java))
        quoteRequestProducer = KafkaProducer(kafkaConfig, StringSerializer(), StringSerializer())
    }

    fun consumerConfig(): Properties = Properties().apply {
        putAll(kafkaConfig)
        put(ConsumerConfig.GROUP_ID_CONFIG, "test-group-id")
        put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true")
        put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
    }

    @AfterTest
    fun tearDown() {
        quoteRequestProducer.close()
        quoteConsumer.close()
    }

    @Test
    fun testProcessor() {
        quoteConsumer.subscribe(listOf("quotes"))
        val quoteId = UUID.randomUUID()
        quoteRequestProducer.send(ProducerRecord("quote-requests", quoteId.toString()))

        val records = quoteConsumer.poll(10000.milliseconds.toJavaDuration())
        val quote = records.records("quotes").first().value()

        quote.id shouldBe quoteId.toString()
    }
}