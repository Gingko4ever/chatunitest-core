package zju.cst.aces.api.impl;

import okhttp3.Response;
import zju.cst.aces.api.Generator;
import zju.cst.aces.api.config.Config;
import zju.cst.aces.dto.ChatResponse;
import zju.cst.aces.dto.Message;
import zju.cst.aces.runner.AbstractRunner;
import zju.cst.aces.util.AskGPT;
import zju.cst.aces.util.CodeExtractor;

import java.util.List;
import zju.cst.aces.api.Generator;

/**
 * 实现 Generator 接口，使用基于聊天的 API 来生成代码。
 */
public class ChatGenerator implements Generator {

    /**
     * 生成器使用的配置设置。
     */
    Config config;

    /**
     * 构建一个新的 ChatGenerator 实例。
     *
     * @param config 生成器的配置设置。
     */
    public ChatGenerator(Config config) {
        this.config = config;
    }

    /**
     * 根据消息列表生成代码。
     *
     * @param messages 用于生成代码的消息列表。
     * @return 生成的代码字符串。
     */
    @Override
    public String generate(List<Message> messages) {
        return extractCodeByResponse(chat(config, messages));
    }

    /**
     * 向 API 发送聊天请求并返回响应。
     *
     * @param config   请求的配置设置。
     * @param messages 要在聊天请求中发送的消息列表。
     * @return 从 API 收到的响应。
     * @throws RuntimeException 如果响应为 null，则抛出运行时异常。
     */
    public static ChatResponse chat(Config config, List<Message> messages) {
        ChatResponse response = new AskGPT(config).askChatGPT(messages);
        if (response == null) {
            throw new RuntimeException("Response is null, failed to get response.");
        }
        return response;
    }

    /**
     * 从响应中提取代码。
     *
     * @param response 包含代码的响应对象。
     * @return 提取的代码字符串。
     */
    public static String extractCodeByResponse(ChatResponse response) {
        return new CodeExtractor(getContentByResponse(response)).getExtractedCode();
    }

    /**
     * 从响应中获取内容。
     *
     * @param response 响应对象。
     * @return 响应的内容。
     */
    public static String getContentByResponse(ChatResponse response) {
        return AbstractRunner.parseResponse(response);
    }

    /**
     * 从内容字符串中提取代码。
     *
     * @param content 包含代码的内容字符串。
     * @return 提取的代码字符串。
     */
    public static String extractCodeByContent(String content) {
        return new CodeExtractor(content).getExtractedCode();
    }
}
