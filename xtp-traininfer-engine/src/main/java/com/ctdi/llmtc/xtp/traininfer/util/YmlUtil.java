package com.ctdi.llmtc.xtp.traininfer.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.yaml.YamlUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author ctdi
 * @since 2025/6/11
 */
@Slf4j
public class YmlUtil {

    public static void dump(Object object, String path) throws IOException {
        YAMLFactory yamlFactory = new YAMLFactory();
        yamlFactory.disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER);
        yamlFactory.enable(YAMLGenerator.Feature.MINIMIZE_QUOTES);
        yamlFactory.enable(YAMLGenerator.Feature.ALWAYS_QUOTE_NUMBERS_AS_STRINGS); // 仅纯数字加引号
        ObjectMapper mapper = new ObjectMapper(yamlFactory);
        mapper.writeValue(new File(path), object);
    }

    public static <T> T load(String path, Class<T> cls) {
        return YamlUtil.loadByPath(path, cls);
    }

    public static void genK8sConfig(String taskId, String tplPath, String outPath) {
        // 读取文件内容
        String content = FileUtil.readString(tplPath, StandardCharsets.UTF_8);
        // 替换所有 <taskid> 占位符
        content = StrUtil.replace(content, "<taskid>", taskId);
        cn.hutool.core.io.FileUtil.writeString(content, outPath, StandardCharsets.UTF_8);
    }

    public static void genParamConfig(Map<String, Object> updatesMap, String tplPath, String outPath) throws IOException {
        Map<String, Object> trainParamMap = YmlUtil.load(tplPath, Map.class);
        for (Map.Entry<String, Object> entry : updatesMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (trainParamMap.containsKey(key)) {
                trainParamMap.put(key, value);
                log.info("Success: Key [{}] was modified to {}.", key, value);
            } else {
                log.info("Warning: Key [{}] not found in the YAML file.", key);
            }
        }
        YmlUtil.dump(trainParamMap, outPath);
    }

}
