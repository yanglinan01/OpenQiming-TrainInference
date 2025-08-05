package com.ctdi.cnos.llm.log.config;

import com.ctdi.cnos.llm.util.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.elasticsearch.client.RestHighLevelClient;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yuyong
 * @date 2024/11/18 16:29
 */
@Configuration
public class ESConfig {

    @Value("${es.cluster:}")
    private String clusterNodes;
    @Value("${es.username:}")
    private String username;
    @Value("${es.password:}")
    private String password;

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        List<HttpHost> hosts = parseClusterNodes(clusterNodes);

        RestClientBuilder builder = RestClient.builder(hosts.toArray(new HttpHost[0]));

        if (StringUtils.isNotEmpty(this.username) && StringUtils.isNotEmpty(this.password)) {
            // 使用 CredentialsProvider 对象进行身份验证
            CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
            builder = builder.setHttpClientConfigCallback(httpAsyncClientBuilder ->
                    httpAsyncClientBuilder
                            .disableAuthCaching()
                            .setDefaultCredentialsProvider(credentialsProvider));
        }

        return new RestHighLevelClient(builder);
    }

    /**
     * 解析集群节点配置字符串，并返回 HttpHost 列表。
     *
     * @param nodesStr 集群节点配置字符串，格式为 host1:port1,host2:port2,...
     * @return HttpHost 列表
     */
    private List<HttpHost> parseClusterNodes(String nodesStr) {
        if (StringUtils.isEmpty(nodesStr)) {
            throw new IllegalArgumentException("Cluster nodes configuration is required.");
        }

        return Arrays.stream(nodesStr.split(","))
                .map(node -> {
                    String[] parts = node.split(":");
                    if (parts.length != 2) {
                        throw new IllegalArgumentException("Invalid node format: " + node);
                    }
                    return new HttpHost(parts[0], Integer.parseInt(parts[1]), "http");
                }).collect(Collectors.toList());
    }
}
