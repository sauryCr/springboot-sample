
package com.example.demo.flink;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KafkaObj {

    private List<Datum> data;
    private String database;
    private Long es;
    private Long id;
    private List<String> pkNames;
    private String table;
    private Long ts;
    private String type;

}
