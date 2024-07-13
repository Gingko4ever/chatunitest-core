package zju.cst.aces.api;

/**
 * Logger 接口定义了一组方法，用于记录不同级别的日志信息。
 */
public interface Logger {
    /**
     * 记录一条信息级别的日志消息。
     *
     * @param msg 日志消息
     */
    void info(String msg);

    /**
     * 记录一条警告级别的日志消息。
     *
     * @param msg 日志消息
     */
    void warn(String msg);

    /**
     * 记录一条错误级别的日志消息。
     *
     * @param msg 日志消息
     */
    void error(String msg);

    /**
     * 记录一条调试级别的日志消息。
     *
     * @param msg 日志消息
     */
    void debug(String msg);
}
