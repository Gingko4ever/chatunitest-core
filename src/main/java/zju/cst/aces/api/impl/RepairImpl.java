package zju.cst.aces.api.impl;

import lombok.Data;
import okhttp3.Response;
import zju.cst.aces.api.Repair;
import zju.cst.aces.api.Validator;
import zju.cst.aces.api.config.Config;
import zju.cst.aces.dto.ChatResponse;
import zju.cst.aces.dto.PromptInfo;
import zju.cst.aces.runner.MethodRunner;

import java.io.IOException;

import static zju.cst.aces.runner.AbstractRunner.*;
import static zju.cst.aces.api.impl.ChatGenerator.*;

/**
 * RepairImpl 类实现了 {@code Repair} 接口，提供了基于规则和基于语言模型的代码修复功能。
 */
@Data
public class RepairImpl implements Repair {

    /**
     * 配置信息，包含修复过程中所需的配置参数。
     */
    Config config;
    /**
     * 提示构造器实现，用于生成修复代码时所需的提示信息。
     */
    PromptConstructorImpl promptConstructorImpl;
    /**
     * 表示修复操作是否成功的标记。
     */
    boolean success = false;

    /**
     * 构造函数，初始化配置信息和提示构造器。
     *
     * @param config                配置信息实例
     * @param promptConstructorImpl 提示构造器实现
     */
    public RepairImpl(Config config, PromptConstructorImpl promptConstructorImpl) {
        this.config = config;
        this.promptConstructorImpl = promptConstructorImpl;
    }

    /**
     * 基于规则的代码修复方法，对测试用例名称、包名和导入进行修复。
     *
     * @param code 需要修复的代码
     * @return 修复后的代码
     */
    @Override
    public String ruleBasedRepair(String code) {
        code = changeTestName(code, promptConstructorImpl.getTestName());
        code = repairPackage(code, promptConstructorImpl.getPromptInfo().getClassInfo().getPackageName());
        code = repairImports(code, promptConstructorImpl.getPromptInfo().getClassInfo().getImports());
        return code;
    }

    /**
     * 基于语言模型的代码修复方法，允许指定修复轮数。
     * 首先尝试运行测试，如果不通过则生成新的提示信息并请求语言模型提供修复代码。
     *
     * @param code   需要修复的代码
     * @param rounds 修复的轮数
     * @return 修复后的代码或空字符串（如果提取代码失败）
     */
    @Override
    public String LLMBasedRepair(String code, int rounds) {
        PromptInfo promptInfo = promptConstructorImpl.getPromptInfo();
        promptInfo.setUnitTest(code);
        String fullClassName = promptInfo.getClassInfo().getPackageName() + "."
                + promptInfo.getClassInfo().getClassName();
        if (MethodRunner.runTest(config, promptConstructorImpl.getFullTestName(), promptInfo, rounds)) {
            this.success = true;
            return code;
        }

        promptConstructorImpl.generate();
        if (promptConstructorImpl.isExceedMaxTokens()) {
            config.getLog().error("Exceed max prompt tokens: " + promptInfo.methodInfo.methodName + " Skipped.");
            return code;
        }
        ChatResponse response = chat(config, promptConstructorImpl.getMessages());
        String newcode = extractCodeByResponse(response);
        if (newcode.isEmpty()) {
            config.getLog().warn("Test for method < " + promptInfo.methodInfo.methodName + " > extract code failed");
            return code;
        } else {
            return newcode;
        }
    }

    /**
     * 基于语言模型的代码修复方法，使用默认的修复轮数（0）。
     * 该方法的行为与 {@link #LLMBasedRepair(String, int)} 类似，但修复轮数固定为0。
     *
     * @param code 需要修复的代码
     * @return 修复后的代码或原始代码（如果不需要修复或提取代码失败）
     */
    @Override
    public String LLMBasedRepair(String code) {
        PromptInfo promptInfo = promptConstructorImpl.getPromptInfo();
        promptInfo.setUnitTest(code);
        String fullClassName = promptInfo.getClassInfo().getPackageName() + "."
                + promptInfo.getClassInfo().getClassName();
        if (MethodRunner.runTest(config, promptConstructorImpl.getFullTestName(), promptInfo, 0)) {
            config.getLog().info("Test for method < " + promptInfo.methodInfo.methodName + " > doesn't need repair");
            return code;
        }

        promptConstructorImpl.generate();

        if (promptConstructorImpl.isExceedMaxTokens()) {
            config.getLog().error("Exceed max prompt tokens: " + promptInfo.methodInfo.methodName + " Skipped.");
            return code;
        }
        ChatResponse response = chat(config, promptConstructorImpl.getMessages());
        String newcode = extractCodeByResponse(response);
        if (newcode.isEmpty()) {
            config.getLog().warn("Test for method < " + promptInfo.methodInfo.methodName + " > extract code failed");
            return code;
        } else {
            return newcode;
        }
    }
}
