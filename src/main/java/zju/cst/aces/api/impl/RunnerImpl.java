package zju.cst.aces.api.impl;

import zju.cst.aces.api.Runner;
import zju.cst.aces.api.config.Config;
import zju.cst.aces.dto.ClassInfo;
import zju.cst.aces.dto.MethodInfo;
import zju.cst.aces.dto.PromptInfo;
import zju.cst.aces.dto.RoundRecord;
import zju.cst.aces.runner.AbstractRunner;
import zju.cst.aces.runner.ClassRunner;
import zju.cst.aces.runner.MethodRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * RunnerImpl 是 Runner 接口的实现类，负责根据配置启动类或方法级别的测试用例生成任务。
 */
public class RunnerImpl implements Runner {
    /**
     * 配置对象，用于控制和定制测试用例生成的行为。
     */
    Config config;

    /**
     * 构造函数，初始化配置对象。
     *
     * @param config 配置对象
     */
    public RunnerImpl(Config config) {
        this.config = config;
    }

    /**
     * 启动针对特定类的测试用例生成任务。
     *
     * @param fullClassName 类的全名
     */
    public void runClass(String fullClassName) {
        try {
            new ClassRunner(config, fullClassName).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 启动针对特定类和方法的测试用例生成任务。
     *
     * @param fullClassName 类的全名
     * @param methodInfo    方法信息
     */
    public void runMethod(String fullClassName, MethodInfo methodInfo) {
        try {
            new MethodRunner(config, fullClassName, methodInfo).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
