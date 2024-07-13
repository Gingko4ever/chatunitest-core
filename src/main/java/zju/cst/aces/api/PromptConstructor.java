package zju.cst.aces.api;

import zju.cst.aces.dto.Message;

import java.util.List;

/**
 * PromptConstructor 接口定义了构造提示信息的方法。
 */
public interface PromptConstructor {

    /**
     * 生成用于交互或处理的提示信息列表。
     * 
     * @return 提示信息的列表。
     */
    List<Message> generate();

}
