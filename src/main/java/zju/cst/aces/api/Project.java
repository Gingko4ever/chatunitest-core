package zju.cst.aces.api;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

/**
 * Project 接口提供了对项目结构和属性的访问方法。
 */
public interface Project {
    /**
     * 获取该项目的父项目。
     * 
     * @return 父项目的实例，如果没有父项目则返回 {@code null}
     */
    Project getParent();

    /**
     * 获取项目的基目录。
     * 
     * @return 项目的基目录文件。
     */
    File getBasedir();

    /**
     * 获取项目的打包类型。
     * 
     * @return 项目的打包类型，例如 "jar" 或 "war"。
     */
    String getPackaging();

    /**
     * 获取项目的组ID。
     * 
     * @return 项目的组ID。
     */
    String getGroupId();

    /**
     * 获取项目的构件ID。
     * 
     * @return 项目的构件ID。
     */
    String getArtifactId();

    /**
     * 获取项目的编译源代码根目录列表。
     * 
     * @return 编译源代码根目录的字符串列表。
     */
    List<String> getCompileSourceRoots();

    /**
     * 获取项目的构件文件路径。
     * 
     * @return 构件文件的路径。
     */
    Path getArtifactPath();

    /**
     * 获取项目的构建输出路径。
     * 
     * @return 构建输出的路径。
     */
    Path getBuildPath();

}
