package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

import java.util.List;
import java.util.stream.Collectors;

public class Main {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(Main.class);
    private static final BookTools bookTools = new BookTools();
    public static void main(String[] args) {
        // STDIO Server Transport
        StdioServerTransportProvider stdioServerTransportProvider = new StdioServerTransportProvider(new ObjectMapper());

        McpServerFeatures.SyncToolSpecification sysTool = getSysTool();

        McpServer.sync(stdioServerTransportProvider)
                .serverInfo("java-mcp", "1.0.0")
                .capabilities(McpSchema.ServerCapabilities.builder()
                        .tools(true)
                        .logging()
                        .build())
                .tools(sysTool)
                .build();

        logger.info("Server started...");
    }

    private static McpServerFeatures.SyncToolSpecification getSysTool() {
// Sync tool specification
        var schema = """
            {
              "type" : "object",
              "id" : "urn:jsonschema:Operation",
              "properties" : {
                "operation" : {
                  "type" : "string"
                },
                "a" : {
                  "type" : "number"
                },
                "b" : {
                  "type" : "number"
                }
              }
            }
            """;
        return new McpServerFeatures.SyncToolSpecification(
                new McpSchema.Tool("getAllBooks", "获取所有的书", schema),
                (exchange, arguments) -> {
                    // 工具的实现
                    List<Book> bookList = bookTools.getBookList();
                    List<McpSchema.Content> result = bookList.stream().map(p -> new McpSchema.TextContent(p.toString())).collect(Collectors.toUnmodifiableList());
                    return new McpSchema.CallToolResult(result, false);
                }
        );
    }
}