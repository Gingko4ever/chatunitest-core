package zju.cst.aces.api;

/**
 * PreProcess 接口定义了预处理操作的契约。
 * 任何实现了此接口的类都应当提供具体的处理逻辑。
 */
public interface PreProcess {
    /**
     * 执行预处理操作。
     */
    void process();

}
