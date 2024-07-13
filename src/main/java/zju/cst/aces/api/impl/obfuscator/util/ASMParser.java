package zju.cst.aces.api.impl.obfuscator.util;

import okio.BufferedSource;
import okio.Okio;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import zju.cst.aces.api.config.Config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

/**
 * ASMParser 是一个用于解析 Java 类文件和 JAR 文件的工具类，使用 ASM 框架读取和分析字节码。
 */
public class ASMParser {

    /**
     * 配置对象，用于控制解析过程中的行为。
     */
    private final Config config;

    /**
     * 构造函数，初始化配置对象。
     *
     * @param config 配置对象
     */
    public ASMParser(Config config) {
        this.config = config;
    }

    /**
     * 根据给定的类节点集合和方法签名集合获取相关条目。
     *
     * @param classNodes 类节点集合
     * @param methodSigs 方法签名集合
     * @return 相关条目的集合
     */
    Set<String> getEntries(Set<ClassNode> classNodes, Collection<String> methodSigs) {
        Set<String> entries = new HashSet<>();
        return entries;
    }

    /**
     * 从单个 .class 文件加载类信息。
     *
     * @param classFile 类文件
     * @return 解析后的类节点集合
     * @throws IOException 如果读取文件时发生 I/O 错误
     */
    public Set<ClassNode> loadClasses(File classFile) throws IOException {
        Set<ClassNode> classes = new HashSet<>();
        InputStream is = new FileInputStream(classFile);
        return readClass(classFile.getName(), is, classes);
    }

    /**
     * 从 JAR 文件加载类信息。
     *
     * @param jarFile JAR 文件
     * @return 解析后的类节点集合
     * @throws IOException 如果读取 JAR 文件时发生 I/O 错误
     */
    public Set<ClassNode> loadClasses(JarFile jarFile) throws IOException {
        Set<ClassNode> targetClasses = new HashSet<>();
        Stream<JarEntry> str = jarFile.stream();
        str.forEach(z -> readJar(jarFile, z, targetClasses));
        jarFile.close();
        return targetClasses;
    }

    /**
     * 读取单个类文件的内容并解析为 ClassNode。
     *
     * @param className     类名
     * @param is            输入流
     * @param targetClasses 目标类节点集合
     * @return 解析后的类节点集合
     */
    private Set<ClassNode> readClass(String className, InputStream is, Set<ClassNode> targetClasses) {
        try {
            BufferedSource source = Okio.buffer(Okio.source(is));
            byte[] bytes = source.readByteArray();
            String cafebabe = String.format("%02X%02X%02X%02X", bytes[0], bytes[1], bytes[2], bytes[3]);
            if (!cafebabe.toLowerCase().equals("cafebabe")) {
                // This class doesn't have a valid magic
                return targetClasses;
            }
            ClassNode cn = getNode(bytes);
            targetClasses.add(cn);
        } catch (Exception e) {
            // config.getLog().warn("Fail to read class {}" + className + e);
            throw new RuntimeException("Fail to read class {}" + className + ": " + e);
        }
        return targetClasses;
    }

    /**
     * 读取 JAR 文件中的单个条目并解析为 ClassNode。
     *
     * @param jar           JAR 文件
     * @param entry         JAR 条目
     * @param targetClasses 目标类节点集合
     * @return 解析后的类节点集合
     */
    private Set<ClassNode> readJar(JarFile jar, JarEntry entry, Set<ClassNode> targetClasses) {
        String name = entry.getName();
        if (name.endsWith(".class")) {
            String className = name.replace(".class", "").replace("/", ".");
            // if relevant options are not specified, classNames will be empty
            try (InputStream jis = jar.getInputStream(entry)) {
                return readClass(className, jis, targetClasses);
            } catch (IOException e) {
                config.getLog().warn("Fail to read class {} in jar {}" + entry + jar.getName() + e);
            }
        } else if (name.endsWith("jar") || name.endsWith("war")) {

        }
        return targetClasses;
    }

    /**
     * 将字节数组转换为 ClassNode。
     *
     * @param bytes 字节数组
     * @return ClassNode 对象
     */
    private ClassNode getNode(byte[] bytes) {
        ClassReader cr = new ClassReader(bytes);
        ClassNode cn = new ClassNode();
        try {
            cr.accept(cn, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // garbage collection friendly
        cr = null;
        return cn;
    }
}