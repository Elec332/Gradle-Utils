package nl.elec332.gradle.util;

import nl.elec332.gradle.util.internal.GradleCoreInternals;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.attributes.Attribute;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.compile.JavaCompile;
import org.gradle.api.tasks.javadoc.Javadoc;
import org.gradle.language.jvm.tasks.ProcessResources;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Objects;

/**
 * Created by Elec332 on 31-3-2020
 */
public class JavaPluginHelper {

    @Nonnull
    public static String getJavaHome() {
        return GradleCoreInternals.getJvm().getJavaHome().getAbsolutePath();
    }

    @Nonnull
    public static Task getClassesTask(Project project) {
        return Objects.requireNonNull(ProjectHelper.getTaskByName(project, JavaPlugin.CLASSES_TASK_NAME));
    }

    @Nonnull
    public static JavaCompile getJavaCompileTask(Project project) {
        return Objects.requireNonNull((JavaCompile) ProjectHelper.getTaskByName(project, JavaPlugin.COMPILE_JAVA_TASK_NAME));
    }

    @Nonnull
    public static Javadoc getJavaDocTask(Project project) {
        return Objects.requireNonNull((Javadoc) ProjectHelper.getTaskByName(project, JavaPlugin.JAVADOC_TASK_NAME));
    }

    @Nonnull
    public static JavaPluginConvention getJavaConvention(Project project) {
        return Objects.requireNonNull(project.getConvention().getPlugin(JavaPluginConvention.class));
    }

    @Nonnull
    public static SourceSet getMainJavaSourceSet(Project project) {
        return Objects.requireNonNull(getJavaConvention(project).getSourceSets().getByName(SourceSet.MAIN_SOURCE_SET_NAME));
    }

    @Nonnull
    public static JavaPluginExtension getJavaExtension(Project project) {
        return project.getExtensions().getByType(JavaPluginExtension.class);
    }

    @Nonnull
    public static JavaCompile getJavaCompileTask(Project project, SourceSet sourceSet) {
        return Objects.requireNonNull((JavaCompile) project.getTasks().getByName(sourceSet.getCompileJavaTaskName()));
    }

    @Nonnull
    @SuppressWarnings("UnstableApiUsage")
    public static ProcessResources getResourcesTask(Project project, SourceSet sourceSet) {
        return Objects.requireNonNull((ProcessResources) project.getTasks().getByName(sourceSet.getProcessResourcesTaskName()));
    }

    @Nullable
    public static String getModuleName(Project project) {
        return JavaPluginHelper.getMainJavaSourceSet(project).getJava().getFiles().stream()
                .filter(f -> f.getName().equals("module-info.java"))
                .findFirst()
                .map(f -> {
                    try {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
                        return reader.lines()
                                .filter(s -> s.contains("module "))
                                .findFirst()
                                .map(s -> {
                                    String ret = s.substring(s.indexOf("module ") + 7);
                                    if (ret.contains("{")) {
                                        ret = ret.substring(0, ret.indexOf("{"));
                                    }
                                    if (ret.contains("/")) {
                                        ret = ret.substring(0, ret.indexOf("/"));
                                    }
                                    return ret.trim();
                                }).orElse(null);
                    } catch (Exception e) {
                        return null;
                    }
                }).orElse(null);
    }

    @Nonnull
    public static SourceSet childSet(Project project, SourceSet parent, String name) {
        SourceSet ret = JavaPluginHelper.getJavaConvention(project).getSourceSets().create(name);

        extendConfigurations(project, ret.getApiConfigurationName(), parent.getApiConfigurationName());
        extendConfigurations(project, ret.getImplementationConfigurationName(), parent.getImplementationConfigurationName());
        extendConfigurations(project, ret.getCompileClasspathConfigurationName(), parent.getCompileClasspathConfigurationName());
        extendConfigurations(project, ret.getCompileOnlyConfigurationName(), parent.getCompileOnlyConfigurationName());
        extendConfigurations(project, ret.getApiElementsConfigurationName(), parent.getApiElementsConfigurationName());
        extendConfigurations(project, ret.getAnnotationProcessorConfigurationName(), parent.getAnnotationProcessorConfigurationName());
        //extendConfigurations(project, ret.getJavadocElementsConfigurationName(), parent.getJavadocElementsConfigurationName());
        //extendConfigurations(project, ret.getSourcesElementsConfigurationName(), parent.getSourcesElementsConfigurationName());
        extendConfigurations(project, ret.getRuntimeClasspathConfigurationName(), parent.getRuntimeClasspathConfigurationName());
        extendConfigurations(project, ret.getRuntimeElementsConfigurationName(), parent.getRuntimeElementsConfigurationName());
        extendConfigurations(project, ret.getRuntimeOnlyConfigurationName(), parent.getRuntimeOnlyConfigurationName());

        project.getTasks().getByName(ret.getCompileJavaTaskName()).dependsOn(parent.getClassesTaskName());
        project.getDependencies().add(ret.getCompileClasspathConfigurationName(), project.files(parent.getJava().getOutputDir()));

        return ret;
    }

    @SuppressWarnings("unchecked")
    private static <T> void extendConfigurations(Project project, String child, String parentName) {
        Configuration cfg = project.getConfigurations().maybeCreate(child);
        Configuration parent = project.getConfigurations().getByName(parentName);
        parent.getAttributes().keySet().forEach(a -> {
            Attribute<T> attr = (Attribute<T>) a;
            cfg.getAttributes().attribute(attr, Objects.requireNonNull(parent.getAttributes().getAttribute(attr)));
        });
        cfg.extendsFrom(parent);
    }

}
