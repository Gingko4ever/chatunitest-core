package zju.cst.aces.api;

import org.junit.platform.launcher.listeners.TestExecutionSummary;
import zju.cst.aces.dto.PromptInfo;

import java.nio.file.Path;

/**
 * Validator 接口定义了对生成的测试用例进行验证的方法。
 */
public interface Validator {

    /**
     * 语法验证代码是否符合 Java 语言规范。
     * 
     * @param code 需要验证的代码。
     * @return 如果代码符合语法规范返回 {@code true}，否则返回 {@code false}。
     */
    boolean syntacticValidate(String code);

    /**
     * 语义验证代码在给定的类名和输出路径下是否符合语义。
     * 
     * @param code       需要验证的代码。
     * @param className  类名。
     * @param outputPath 输出路径。
     * @param promptInfo 提示信息。
     * @return 如果代码符合语义规范返回 {@code true}，否则返回 {@code false}。
     */
    boolean semanticValidate(String code, String className, Path outputPath, PromptInfo promptInfo);

    /**
     * 运行时验证测试用例是否能够成功执行。
     * 
     * @param fullTestName 测试用例的全名。
     * @return 如果测试用例能够成功执行返回 {@code true}，否则返回 {@code false}。
     */
    boolean runtimeValidate(String fullTestName);

    /**
     * 编译测试用例。
     * 
     * @param className  类名。
     * @param outputPath 输出路径。
     * @param promptInfo 提示信息。
     * @return 如果编译成功返回 {@code true}，否则返回 {@code false}。
     */
    public boolean compile(String className, Path outputPath, PromptInfo promptInfo);

    /**
     * 执行测试用例并返回执行摘要。
     * 
     * @param fullTestName 测试用例的全名。
     * @return 测试执行的摘要信息。
     */
    public TestExecutionSummary execute(String fullTestName);
}
