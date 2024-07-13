package zju.cst.aces.api;

import zju.cst.aces.dto.MethodInfo;

/**
 * Runner 接口定义了执行测试用例生成和运行的方法。
 */
public interface Runner {

    /**
     * 为指定的类全名运行测试用例生成。
     * 
     * @param fullClassName 需要生成测试用例的类的全名。
     */
    public void runClass(String fullClassName);

    /**
     * 为指定的类全名和方法信息运行测试用例生成。
     * 
     * @param fullClassName 需要生成测试用例的类的全名。
     * @param methodInfo    需要生成测试用例的方法的信息。
     */
    public void runMethod(String fullClassName, MethodInfo methodInfo);
}