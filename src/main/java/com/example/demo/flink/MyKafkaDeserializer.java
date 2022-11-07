package com.example.demo.flink;

import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.connector.kafka.source.reader.deserializer.KafkaRecordDeserializationSchema;
import org.apache.flink.util.Collector;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.IOException;

public class MyKafkaDeserializer implements KafkaRecordDeserializationSchema<String> {
    String encoding = "UTF8";
    @Override
    public void deserialize(ConsumerRecord<byte[], byte[]> record, Collector<String> collector) throws IOException {
//        collector.collect(new ConsumerRecord(record.topic(),
//                record.partition(),
//                record.offset(),
//                record.timestamp(),
//                record.timestampType(),
//                record.checksum(),
//                record.serializedKeySize(),
//                record.serializedValueSize(),
//                /*这里我没有进行空值判断，生产一定记得处理*/
//                new  String(record.key(), encoding),
//                new  String(record.value(), encoding)));
        collector.collect(record.toString());
    }

    @Override
    public TypeInformation<String> getProducedType() {
        return TypeInformation.of(new TypeHint<>(){});
    }
}
