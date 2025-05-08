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
        // 使用StdIO标准格式
        StdioServerTransportProvider stdioServerTransportProvider = new StdioServerTransportProvider(new ObjectMapper());

        // 获取要暴露出去的工具
        McpServerFeatures.SyncToolSpecification allBookTool = getAllBookTool();
        McpServerFeatures.SyncToolSpecification bookByYear = getBookByYear();

        // 配置mcp-server信息
        McpServer.sync(stdioServerTransportProvider)
                .serverInfo("java-mcp", "1.0.0")
                .capabilities(McpSchema.ServerCapabilities.builder()
                        .tools(true)
                        .logging()
                        .build())
                .tools(allBookTool,bookByYear)
                .build();

        logger.info("Server started...");
    }

    private static McpServerFeatures.SyncToolSpecification getAllBookTool() {
        var schema = """
            {
              "type" : "object",
              "properties" : {
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

    private static McpServerFeatures.SyncToolSpecification getBookByYear() {
        var schema = """
            {
              "type" : "object",
              "properties" : {
                "year" : {
                  "type" : "number"
                }
              },
              "required" : ["year"]
            }
            """;
        return new McpServerFeatures.SyncToolSpecification(
                new McpSchema.Tool("getBookByYear", "根据年份获取对应年份的书", schema),
                (exchange, arguments) -> {
                    // 工具的实现
                    Integer year = (Integer) arguments.get("year");
                    List<Book> bookList = bookTools.getBookListByYear(year);
                    List<McpSchema.Content> result = bookList.stream().map(p -> new McpSchema.TextContent(p.toString())).collect(Collectors.toUnmodifiableList());
                    return new McpSchema.CallToolResult(result, false);
                }
        );
    }
}