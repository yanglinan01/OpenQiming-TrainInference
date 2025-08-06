package com.ctdi.cnos.llm.util;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.util.List;
import java.util.Properties;

/**
 * Token计数。
 *
 * @author laiqi
 * @since 2024/11/26
 */
public final class TokenCounterUtil {

    static StanfordCoreNLP pipeline;

    static {
        // 设置Stanford CoreNLP的属性
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize");

        // 创建StanfordCoreNLP对象
        pipeline = new StanfordCoreNLP(props);
    }

    /**
     * 计算文本中Token的数量。
     * @param text 文本
     * @return Token数量
     */
    public static int countTokens(String text) {
        // 创建Annotation对象
        Annotation document = new Annotation(text);

        // 注解文档
        pipeline.annotate(document);

        // 获取Token列表
        List<CoreLabel> tokens = document.get(CoreAnnotations.TokensAnnotation.class);

        // 计算Token数量
        return tokens.size();
    }

    public static void main(String[] args) {
        // 示例文本
        String text = "我的网络连接不稳定，经常断开，怎么办？";

        // 设置Stanford CoreNLP的属性
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize");

        // 创建StanfordCoreNLP对象
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // 创建Annotation对象
        Annotation document = new Annotation(text);

        // 注解文档
        pipeline.annotate(document);

        // 获取Token列表
        List<CoreLabel> tokens = document.get(CoreAnnotations.TokensAnnotation.class);

        // 计算Token数量
        int numTokens = tokens.size();

        // 输出结果
        System.out.println("Token数量: " + numTokens);
        for (CoreLabel token : tokens) {
            System.out.println("Token: " + token.word());
        }

    }
}