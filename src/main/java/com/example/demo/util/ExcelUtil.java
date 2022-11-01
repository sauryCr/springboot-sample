package com.example.demo.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.util.Pair;

import java.util.List;
import java.util.Map;

@Slf4j
public class ExcelUtil<T> {
    public static <T> void write(String fileName, List<T> data) {
        EasyExcel.write(fileName, data.get(0).getClass()).sheet(fileName).doWrite(data);
    }

    public static Pair<ExcelWriter, WriteSheet> writerAndSheet(String fileName, Class<?> clazz) {
        // 这里 需要指定写用哪个class去写
        try (ExcelWriter excelWriter = EasyExcel.write(fileName, clazz).build()) {
            // 这里注意 如果同一个sheet只要创建一次
            WriteSheet sheet = EasyExcel.writerSheet("商品详情").build();

            return Pair.create(excelWriter, sheet);
        }
    }


    public static <T> void repeatWrite(Pair<ExcelWriter, WriteSheet> pair, List<T> data) {
        pair.getFirst().write(data, pair.getSecond());
    }



}
