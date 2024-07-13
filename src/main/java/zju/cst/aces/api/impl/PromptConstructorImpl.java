package zju.cst.aces.api.impl;

import com.google.j2objc.annotations.ObjectiveCName;
import lombok.Data;
import zju.cst.aces.api.PromptConstructor;
import zju.cst.aces.api.config.Config;
import zju.cst.aces.dto.ClassInfo;
import zju.cst.aces.dto.Message;
import zju.cst.aces.dto.MethodInfo;
import zju.cst.aces.dto.PromptInfo;
import zju.cst.aces.prompt.PromptGenerator;
import zju.cst.aces.runner.AbstractRunner;
import zju.cst.aces.util.TokenCounter;

import java.io.IOException;
import java.util.List;
import zju.cst.aces.api.PromptConstructor;

/**
 * PromptConstructorImpl 类实现了 {@code PromptConstructor} 接口，用于构造用于生成测试用例的提示信息。
 */
@Data
public class PromptConstructorImpl implements PromptConstructor {
    /**
     * 配置信息，包含构造提示信息所需的配置参数。
     */
    Config config;
    /**
     * 用于生成提示的 PromptInfo 实例。
     */
    PromptInfo promptInfo;
    /**
     * 构造的提示信息列表。
     */
    List<Message> messages;
    /**
     * 用于记录提示信息中令牌的总数。
     */
    int tokenCount = 0;
    /**
     * 测试用例的简单名称。
     */
    String testName;
    /**
     * 测试用例的全名，包括包名。
     */
    String fullTestName;
    /**
     * 分隔符，用于在测试用例名称中分隔类名和方法名。
     */
    static final String separator = "_";

    /**
     * 构造函数，初始化配置信息。
     *
     * @param config 配置信息实例
     */
    public PromptConstructorImpl(Config config) {
        this.config = config;
    }

    /**
     * 生成提示信息列表。
     * 
     * @return 提示信息列表
     * @throws IOException 如果在生成提示信息过程中发生 I/O 错误
     */
    @Override
    public List<Message> generate() {
        try {
            if (promptInfo == null) {
                throw new RuntimeException("PromptInfo is null, you need to initialize it first.");
            }
            this.messages = new PromptGenerator(config).generateMessages(promptInfo);
            countToken();
            return this.messages;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 设置包含依赖关系的提示信息。
     *
     * @param classInfo  类信息
     * @param methodInfo 方法信息
     * @throws IOException 如果在设置过程中发生 I/O 错误
     */
    public void setPromptInfoWithDep(ClassInfo classInfo, MethodInfo methodInfo) throws IOException {
        this.promptInfo = AbstractRunner.generatePromptInfoWithDep(config, classInfo, methodInfo);
    }

    /**
     * 设置不包含依赖关系的提示信息。
     *
     * @param classInfo  类信息
     * @param methodInfo 方法信息
     * @throws IOException 如果在设置过程中发生 I/O 错误
     */
    public void setPromptInfoWithoutDep(ClassInfo classInfo, MethodInfo methodInfo) throws IOException {
        this.promptInfo = AbstractRunner.generatePromptInfoWithoutDep(config, classInfo, methodInfo);
    }

    /**
     * 设置测试用例的全名。
     *
     * @param fullTestName 测试用例的全名
     */
    public void setFullTestName(String fullTestName) {
        this.fullTestName = fullTestName;
        this.testName = fullTestName.substring(fullTestName.lastIndexOf(".") + 1);
        this.promptInfo.setFullTestName(this.fullTestName);
    }

    /**
     * 设置测试用例的简单名称。
     *
     * @param testName 测试用例的简单名称
     */
    public void setTestName(String testName) {
        this.testName = testName;
    }

    /**
     * 计算并更新提示信息中的令牌总数。
     */
    public void countToken() {
        for (Message p : messages) {
            this.tokenCount += TokenCounter.countToken(p.getContent());
        }
    }

    /**
     * 检查提示信息中的令牌总数是否超出了配置的最大令牌数。
     *
     * @return 如果超出返回 true，否则返回 false
     */
    public boolean isExceedMaxTokens() {
        if (this.tokenCount > config.maxPromptTokens) {
            return true;
        } else {
            return false;
        }
    }
}
