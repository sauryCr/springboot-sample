package com.example.demo;

import com.example.demo.flink.Datum;
import com.example.demo.flink.KafkaObj;
import com.example.demo.flink.KafkaObjFlat;
import com.example.demo.util.Json;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.DataTypes;
import org.apache.flink.table.api.Schema;
import org.apache.flink.table.api.StatementSet;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamStatementSet;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.connector.ChangelogMode;
import org.apache.flink.table.expressions.AggregateExpression;
import org.apache.flink.table.types.AbstractDataType;
import org.apache.flink.table.types.DataType;
import org.apache.flink.types.Row;
import org.apache.flink.util.Collector;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import static org.apache.flink.table.api.Expressions.$;


@SpringBootApplication
@EnableHystrix
@Slf4j
public class DemoApplication {

    public static void main(String[] args) throws Exception {
        Configuration configuration = new Configuration();
        configuration.setString(RestOptions.BIND_PORT, "8001");
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(configuration);
        StreamTableEnvironment tableEnvironment = StreamTableEnvironment.create(env);

        String t = "CREATE TABLE group_members (" +
                "  id BIGINT, product_id BIGINT, group_id BIGINT, user_id varchar, " +
                "  PRIMARY KEY (product_id, group_id, user_id) NOT ENFORCED  " +
                ") WITH (" +
                " 'connector' = 'kafka'," +
                " 'topic' = 'hikari-group-info'," +
                " 'properties.bootstrap.servers' = 'vm151.test.ceshi.th.corp.yodao.com:9092,vm152.test.ceshi.th.corp.yodao.com:9092,vm153.test.ceshi.th.corp.yodao.com:9092'," +
                " 'properties.group.id' = 'hikari-group-info'," +
                " 'format' = 'canal-json', " +
                " 'scan.startup.mode' = 'earliest-offset'" +
                ")";

        tableEnvironment.executeSql(t);
        tableEnvironment.executeSql("select * from group_members ").print();

        tableEnvironment.executeSql("select product_id, group_id, count(distinct user_id) as cnt from group_members group by product_id, group_id").print();
        env.execute();

//        Configuration configuration = new Configuration();
//        configuration.setString(RestOptions.BIND_PORT, "8001");
//        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(configuration);
//        KafkaSource<Object> source = KafkaSource.builder()
//                .setBootstrapServers("vm151.test.ceshi.th.corp.yodao.com:9092,vm152.test.ceshi.th.corp.yodao.com:9092,vm153.test.ceshi.th.corp.yodao.com:9092")
//                .setGroupId("hikari-group-info")
//                .setTopics("hikari-group-info")
//                .setStartingOffsets(OffsetsInitializer.earliest())
//                .setDeserializer(new MyKafkaDeserialrecordDeserializerizer())
//                .build();
//        DataStream<KafkaObjFlat> kafka_source = env.fromSource(source, WatermarkStrategy.noWatermarks(), "Kafka Source")
//                .filter(Objects::nonNull)
//                .flatMap(new FlatMapFunction<Object, KafkaObj>() {
//                    @Override
//                    public void flatMap(Object o, Collector<KafkaObj> collector) throws Exception {
//                        ConsumerRecord<byte[], byte[]> consumerRecord = (ConsumerRecord<byte[], byte[]>) o;
//                        String value = new String(consumerRecord.value(), StandardCharsets.UTF_8);
//                        Optional<KafkaObj> parse = Json.parse(value, KafkaObj.class);
//                        collector.collect(parse.orElse(null));
//                    }
//                })
//                .filter(Objects::nonNull)
//                .flatMap(new FlatMapFunction<KafkaObj, KafkaObjFlat>() {
//                    @Override
//                    public void flatMap(KafkaObj kafkaObj, Collector<KafkaObjFlat> collector) throws Exception {
//                        for (Datum datum : kafkaObj.getData()) {
//                            KafkaObjFlat flat = new KafkaObjFlat();
//                            flat.setDatabase(kafkaObj.getDatabase());
//                            flat.setEs(kafkaObj.getEs());
//                            flat.setId(kafkaObj.getId());
//                            flat.setPkNames(kafkaObj.getPkNames());
//                            flat.setTable(kafkaObj.getTable());
//                            flat.setTs(kafkaObj.getTs());
//                            flat.setType(kafkaObj.getType());
//                            flat.setGroup_id(datum.getGroup_id());
//                            flat.setProduct_id(datum.getProduct_id());
//                            flat.setUser_id(datum.getUser_id());
//                            collector.collect(flat);
//                        }
//                    }
//                });
//
//        List<String> fields = Arrays.asList("product_id", "group_id", "user_id", "database", "es", "id", "pkNames", "table", "ts" ,"type");
////        "product_id, group_id, user_id, type, ts, es, id, table, pkNames, database" Types.LONG, Types.STRING, Types.STRING, Types.LONG, Types.LONG, Types.LONG, Types.STRING, Types.OBJECT_ARRAY(Types.STRING), Types.STRING
//        List<AbstractDataType<? extends AbstractDataType<?>>> dataTypes = Arrays.asList(DataTypes.BIGINT().notNull(), DataTypes.BIGINT().notNull(), DataTypes.STRING().notNull(), DataTypes.STRING(), DataTypes.BIGINT(), DataTypes.BIGINT(), DataTypes.RAW(List.class), DataTypes.STRING(), DataTypes.BIGINT(), DataTypes.STRING());
//
//        StreamTableEnvironment streamTableEnvironment = StreamTableEnvironment.create(env);
//        Table table = streamTableEnvironment.fromChangelogStream(kafka_source);
//
//        Schema schema = Schema.newBuilder().fromFields(fields, dataTypes).primaryKey("user_id").build();
//        DataStream<Row> rowDataStream = streamTableEnvironment.toChangelogStream(table, schema, ChangelogMode.upsert());
//
//        Table table1 = streamTableEnvironment.fromChangelogStream(rowDataStream);
//        streamTableEnvironment.createTemporaryView("group_members", table1);
//        streamTableEnvironment.executeSql("select product_id, group_id, count(distinct user_id) as cnt from group_members where type='INSERT' group by group_id, product_id").print();
////        table.filter($("type").isEqual("INSERT")).groupBy($("product_id"), $("group_id")).aggregate($("user_id")).select().execute().print();
//        env.execute("tidb kafka source test");
    }
}
