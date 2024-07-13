package zju.cst.aces.api.impl;

import org.apache.maven.project.MavenProject;
import zju.cst.aces.api.Project;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * ProjectImpl 类实现了 Project 接口，提供了对 Maven 项目的封装。
 */
public class ProjectImpl implements Project {

    /**
     * Maven 项目对象。
     */
    MavenProject project;

    /**
     * 构造函数，初始化 Maven 项目对象。
     *
     * @param project Maven 项目对象
     */
    public ProjectImpl(MavenProject project) {
        this.project = project;
    }

    /**
     * 实现获取父项目的方法。
     *
     * @return 父项目的实例，如果没有父项目则返回 {@code null}
     */
    @Override
    public Project getParent() {
        if (project.getParent() == null) {
            return null;
        }
        return new ProjectImpl(project.getParent());
    }

    /**
     * 获取项目的基目录。
     * 
     * @return 项目的基目录文件。
     */
    @Override
    public File getBasedir() {
        return project.getBasedir();
    }

    /**
     * 获取项目的打包类型。
     * 
     * @return 项目的打包类型，例如 "jar" 或 "war"。
     */
    @Override
    public String getPackaging() {
        return project.getPackaging();
    }

    /**
     * 获取项目的组 ID。
     * 
     * @return 项目的组 ID。
     */
    @Override
    public String getGroupId() {
        return project.getGroupId();
    }

    /**
     * 获取项目的构件 ID。
     * 
     * @return 项目的构件 ID。
     */
    @Override
    public String getArtifactId() {
        return project.getArtifactId();
    }

    /**
     * 获取项目的编译源代码根目录列表。
     * 
     * @return 编译源代码根目录的列表。
     */
    @Override
    public List<String> getCompileSourceRoots() {
        return project.getCompileSourceRoots();
    }

    /**
     * 获取项目的构件文件路径。
     * 
     * @return 项目的构件文件路径。
     */
    @Override
    public Path getArtifactPath() {
        return Paths.get(project.getBuild().getDirectory()).resolve(project.getBuild().getFinalName() + ".jar");
    }

    /**
     * 获取项目的构建输出路径。
     * 
     * @return 项目的构建输出路径。
     */
    @Override
    public Path getBuildPath() {
        return Paths.get(project.getBuild().getOutputDirectory());
    }

}
