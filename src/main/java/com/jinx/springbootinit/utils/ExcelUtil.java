package com.jinx.springbootinit.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Slf4j
public class ExcelUtil {
    public static String excelToCsv(MultipartFile file){
        //TODO 实现文件转换
        List<Map<Integer,String>> list = null;
        try {
            list = EasyExcel.read(file.getInputStream())
                    .excelType(ExcelTypeEnum.XLSX)
                    .sheet()
                    .headRowNumber(0)
                    .doReadSync();
        } catch (IOException e) {
            log.error("数据处理异常");
        }
        if(CollUtil.isEmpty(list)){
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        LinkedHashMap<Integer, String> headMap = (LinkedHashMap)list.get(0);
        List<String> headList = headMap.values().stream().filter(ObjectUtil::isNotEmpty).collect(Collectors.toList());
        stringBuilder.append(CollUtil.join(headList, ",")).append("\n");
        for(int i = 1; i<list.size();i++){
            LinkedHashMap<Integer,String> map = (LinkedHashMap)list.get(i);
            List<String> collect = map.values().stream().filter(ObjectUtil::isNotEmpty).collect(Collectors.toList());
            stringBuilder.append(CollUtil.join(collect, ",")).append("\n");
        }
        return stringBuilder.toString();

    }
}
