package nl.elec332.gradle.util;

import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.plugins.BasePluginConvention;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

/**
 * Created by Elec332 on 31-3-2020
 */
public class ProjectHelper {

    @Nonnull
    public static String getProjectDirPath(Task task) {
        return getProjectDirPath(task.getProject());
    }

    @Nonnull
    public static File getProjectDir(Task task) {
        return getProjectDir(task.getProject());
    }

    @Nonnull
    public static Configuration getCompileConfiguration(Task task) {
        return getCompileConfiguration(task.getProject());
    }

    @Nonnull
    public static SourceSet getMainSourceSet(Task task) {
        return getMainSourceSet(task.getProject());
    }

    @Nonnull
    public static SourceSetContainer getSourceSets(Task task) {
        return getSourceSets(task.getProject());
    }

    @Nonnull
    public static String getDefaultMainSourceFolderPath(Task task) {
        return getDefaultMainSourceFolderPath(task.getProject());
    }

    @Nonnull
    public static String getDefaultSourceFolderPath(Task task) {
        return getDefaultSourceFolderPath(task.getProject());
    }

    @Nonnull
    public static File getDefaultMainSourceFolder(Task task) {
        return getDefaultMainSourceFolder(task.getProject());
    }

    @Nonnull
    public static File getDefaultSourceFolder(Task task) {
        return getDefaultSourceFolder(task.getProject());
    }

    @Nonnull
    public static File getBuildFolder(Task task) {
        return getBuildFolder(task.getProject());
    }

    @Nonnull
    public static BasePluginConvention getBaseConvention(Task task) {
        return getBaseConvention(task.getProject());
    }

    /////////////////////////

    @Nonnull
    public static String getProjectDirPath(Project project) {
        return Objects.requireNonNull(getProjectDir(project).getAbsolutePath());
    }

    @Nonnull
    public static File getProjectDir(Project project) {
        return Objects.requireNonNull(project.getProjectDir());
    }

    @Nonnull
    public static Configuration getCompileConfiguration(Project project) {
        return Objects.requireNonNull(project.getConfigurations().getByName("implementation"));
    }

    @Nonnull
    public static SourceSet getMainSourceSet(Project project) {
        return Objects.requireNonNull(getSourceSets(project).getByName(SourceSet.MAIN_SOURCE_SET_NAME));
    }

    @Nonnull
    public static SourceSetContainer getSourceSets(Project project) {
        return Objects.requireNonNull((SourceSetContainer) project.getExtensions().getByName("sourceSets"));
    }

    @Nonnull
    public static String getDefaultMainSourceFolderPath(Project project) {
        return Objects.requireNonNull(getDefaultMainSourceFolder(project).getAbsolutePath());
    }

    @Nonnull
    public static String getDefaultSourceFolderPath(Project project) {
        return Objects.requireNonNull(getDefaultSourceFolder(project).getAbsolutePath());
    }

    @Nonnull
    public static File getDefaultMainSourceFolder(Project project) {
        return new File(getDefaultSourceFolder(project), SourceSet.MAIN_SOURCE_SET_NAME);
    }

    @Nonnull
    public static File getDefaultSourceFolder(Project project) {
        return Objects.requireNonNull(project.file("src"));
    }

    @Nonnull
    public static File getBuildFolder(Project project) {
        return Objects.requireNonNull(project.getBuildDir());
    }

    @Nonnull
    public static BasePluginConvention getBaseConvention(Project project) {
        return Objects.requireNonNull(((BasePluginConvention) project.getProject().getConvention().getPlugins().get("base")));
    }

    /////////////////////////

    public static Task getBuildTask(Project project) {
        return getTaskByName(project, "build");
    }

    public static Task getTaskByName(Project project, String name) {
        return project.getTasks().getByName(name);
    }

    public static void beforeTaskGraphDone(Project project, Runnable runnable) {
        beforeTaskGraphDone(project, s -> true, runnable);
    }

    public static void beforeTaskGraphDone(Project project, Predicate<String> nameMatcher, Runnable runnable) {
        AtomicBoolean ran = new AtomicBoolean(false);
        project.getTasks().configureEach(a -> {
            if (!ran.get() && nameMatcher.test(a.getPath())) {
                ran.set(true);
                runnable.run();
            }
        });
    }

    public static String getVersion(Project project) {
        return GroovyHooks.getVersion(project);
    }

}
