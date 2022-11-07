package com.example.demo;

import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.util.Collector;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.IOException;

public class MyKafkaDeserialrecordDeserializerizer implements org.apache.flink.connector.kafka.source.reader.deserializer.KafkaRecordDeserializationSchema<Object> {
    @Override
    public void deserialize(ConsumerRecord<byte[], byte[]> record, Collector<Object> out) throws IOException {
        out.collect(record);
    }

    @Override
    public TypeInformation<Object> getProducedType() {
        return TypeInformation.of(new TypeHint<>(){});
    }
}
