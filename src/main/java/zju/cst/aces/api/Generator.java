package zju.cst.aces.api;

import zju.cst.aces.api.config.Config;
import zju.cst.aces.dto.Message;

import java.util.List;

/**
 * Generator 接口定义了一个方法，用于生成某些类型的输出。
 */
public interface Generator {
    /**
     * 根据给定的消息列表生成输出。
     * 
     * @param messages 要生成输出的消息列表
     * @return 生成的字符串输出
     */
    String generate(List<Message> messages);

}
