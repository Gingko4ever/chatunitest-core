
package zju.cst.aces.api.impl;

import com.github.javaparser.ParseProblemException;
import com.github.javaparser.StaticJavaParser;
import lombok.Data;
import org.junit.platform.launcher.listeners.TestExecutionSummary;
import zju.cst.aces.api.Validator;
import zju.cst.aces.api.config.Config;
import zju.cst.aces.dto.PromptInfo;
import zju.cst.aces.util.TestCompiler;

import java.nio.file.Path;
import java.util.List;

import zju.cst.aces.api.Validator;

/**
 * ValidatorImpl类实现了Validator接口，提供代码验证的实现。
 * 它使用TestCompiler进行语法、语义和运行时验证，以及代码的编译和执行。
 */
@Data
public class ValidatorImpl implements Validator {

    private final TestCompiler compiler;

    /**
     * 构造函数，初始化TestCompiler并设置必要的路径和类路径元素。
     *
     * @param testOutputPath    输出测试类的路径。
     * @param compileOutputPath 编译输出的路径。
     * @param targetPath        目标路径，通常是项目的目标目录。
     * @param classpathElements 类路径元素列表，用于编译过程。
     */
    public ValidatorImpl(Path testOutputPath, Path compileOutputPath, Path targetPath, List<String> classpathElements) {
        this.compiler = new TestCompiler(testOutputPath, compileOutputPath, targetPath, classpathElements);
    }

    /**
     * 验证给定的代码是否有语法错误。
     *
     * @param code 要验证的Java代码。
     * @return 如果代码没有语法错误，返回true；否则返回false。
     */
    @Override
    public boolean syntacticValidate(String code) {
        try {
            StaticJavaParser.parse(code);
            return true;
        } catch (ParseProblemException e) {
            return false;
        }
    }

    /**
     * 验证给定的代码是否有语义错误，尝试编译代码。
     *
     * @param code       要验证的Java代码。
     * @param className  类的名称，用于编译。
     * @param outputPath 输出路径，用于保存编译后的类文件。
     * @param promptInfo 提示信息，可能包含额外的编译选项或上下文。
     * @return 如果代码没有语义错误且编译成功，返回true；否则返回false。
     */
    @Override
    public boolean semanticValidate(String code, String className, Path outputPath, PromptInfo promptInfo) {
        compiler.setCode(code);
        return compiler.compileTest(className, outputPath, promptInfo);
    }

    /**
     * 验证给定的测试代码在运行时是否通过所有测试。
     *
     * @param fullTestName 完全限定的测试类名称。
     * @return 如果所有测试都通过，返回true；否则返回false。
     */
    @Override
    public boolean runtimeValidate(String fullTestName) {
        return compiler.executeTest(fullTestName).getTestsFailedCount() == 0;
    }

    /**
     * 编译给定的测试代码。
     *
     * @param className  类的名称，用于编译。
     * @param outputPath 输出路径，用于保存编译后的类文件。
     * @param promptInfo 提示信息，可能包含额外的编译选项或上下文。
     * @return 如果编译成功，返回true；否则返回false。
     */
    @Override
    public boolean compile(String className, Path outputPath, PromptInfo promptInfo) {
        return compiler.compileTest(className, outputPath, promptInfo);
    }

    /**
     * 执行给定的测试代码并返回测试执行摘要。
     *
     * @param fullTestName 完全限定的测试类名称。
     * @return 测试执行摘要，包含测试的结果信息。
     */
    @Override
    public TestExecutionSummary execute(String fullTestName) {
        return compiler.executeTest(fullTestName);
    }
}