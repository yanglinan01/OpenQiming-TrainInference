package com.ctdi.llmtc.xtp.traininfer.util;

import java.io.IOException;
import java.util.List;

public class JSONBuilder {

    public static String buildVs(List<String> taskIds) throws IOException {
        StringBuilder httpRoutes = new StringBuilder();

        for (int i = 0; i < taskIds.size(); i++) {
            String taskId = taskIds.get(i);

            // Rule 1: Match by header "task"
            String headerMatch = """
                {
                    "match": [
                        {
                            "headers": {
                                "task": { "exact": "%s" }
                            }
                        }
                    ],
                    "route": [
                        {
                            "destination": {
                                "port": { "number": 8000 },
                                "host": "inference-service-%s"
                            }
                        }
                    ]
                }
                """.formatted(taskId, taskId);

            // Rule 2: Match by URI path
            String uriMatch = """
                {
                    "match": [
                        {
                            "uri": { "exact": "/%s/v1/chat/completions" }
                        }
                    ],
                    "rewrite": {
                        "uri": "/v1/chat/completions"
                    },
                    "route": [
                        {
                            "destination": {
                                "port": { "number": 8000 },
                                "host": "inference-service-%s"
                            }
                        }
                    ]
                }
                """.formatted(taskId, taskId);

            if (i > 0 || !httpRoutes.isEmpty()) {
                httpRoutes.append(",\n");
            }
            httpRoutes.append(headerMatch).append(",\n").append(uriMatch);
        }

        String vs ="""
        {
            "apiVersion": "networking.istio.io/v1alpha3",
            "kind": "VirtualService",
            "metadata": {
                "name": "qwen2-inference-vs"
            },
            "spec": {
                "hosts": ["*"],
                "gateways": ["qwen2-inference-gateway"],
                "http": [
                    %s
                ]
            }
        }
        """.formatted(httpRoutes.toString());

        return JSONUtil.toPrettyJson(vs);
    }
}
