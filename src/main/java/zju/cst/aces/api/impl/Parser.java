package zju.cst.aces.api.impl;

import lombok.Data;
import zju.cst.aces.api.PreProcess;
import zju.cst.aces.api.Task;
import zju.cst.aces.api.config.Config;
import zju.cst.aces.parser.ProjectParser;

import zju.cst.aces.api.PreProcess;

/**
 * Parser 类实现了 PreProcess 接口，用于处理项目解析的预处理步骤。
 */
@Data
public class Parser implements PreProcess {

    /**
     * 项目解析器，用于解析项目结构和类信息。
     */
    ProjectParser parser;
    /**
     * 配置对象，包含解析过程中所需的配置信息。
     */
    Config config;

    /**
     * 构造函数，初始化解析器并设置配置对象。
     *
     * @param config 配置对象
     */
    public Parser(Config config) {
        this.config = config;
        this.parser = new ProjectParser(config);
    }

    /**
     * 实现预处理接口，调用内部的解析方法。
     */
    @Override
    public void process() {
        this.parse();
    }

    /**
     * 解析项目中的类信息，检查目标文件夹是否存在且项目是否为 POM 打包类型。
     */
    public void parse() {
        try {
            Task.checkTargetFolder(config.getProject());
        } catch (RuntimeException e) {
            config.getLog().error(e.toString());
            return;
        }
        if (config.getProject().getPackaging().equals("pom")) {
            config.getLog().info("\n==========================\n[ChatUniTest] Skip pom-packaging ...");
            return;
        }
        if (!config.getParseOutput().toFile().exists()) {
            config.getLog().info("\n==========================\n[ChatUniTest] Parsing class info ...");
            parser.parse();
            config.getLog().info("\n==========================\n[ChatUniTest] Parse finished");
        } else {
            config.getLog()
                    .info("\n==========================\n[ChatUniTest] Parse output already exists, skip parsing!");
        }
    }
}
