package zju.cst.aces.api.impl;

import zju.cst.aces.util.LogFormatter;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * LoggerImpl 类实现了 zju.cst.aces.api.Logger 接口，提供了日志记录功能。
 * 它使用 java.util.logging 包中的 Logger 来输出日志信息。
 */
public class LoggerImpl implements zju.cst.aces.api.Logger {

    /**
     * java.util.logging 包中的 Logger 实例，用于记录日志。
     */
    java.util.logging.Logger log;

    /**
     * 构造函数，初始化 LoggerImpl 实例。
     * 设置日志记录器的名称为 "ChatUniTest"，并为其添加控制台处理器。
     * 控制台处理器使用 LogFormatter 来格式化日志输出。
     */
    public LoggerImpl() {
        this.log = java.util.logging.Logger.getLogger("ChatUniTest");
        Handler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.ALL);
        consoleHandler.setFormatter(new LogFormatter());
        this.log.addHandler(consoleHandler);
        this.log.setUseParentHandlers(false);
    }

    /**
     * 记录一条信息级别的日志。
     *
     * @param msg 要记录的信息
     */
    @Override
    public void info(String msg) {
        log.info(msg);
    }

    /**
     * 记录一条警告级别的日志。
     *
     * @param msg 要记录的警告信息
     */
    @Override
    public void warn(String msg) {
        log.warning(msg);
    }

    /**
     * 记录一条错误级别的日志。
     *
     * @param msg 要记录的错误信息
     */
    @Override
    public void error(String msg) {
        log.severe(msg);
    }

    /**
     * 记录一条调试级别的日志。
     *
     * @param msg 要记录的调试信息
     */
    @Override
    public void debug(String msg) {
        log.config(msg);
    }
}
