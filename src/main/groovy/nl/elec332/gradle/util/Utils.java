package nl.elec332.gradle.util;

import nl.elec332.gradle.util.abstraction.IProjectObjects;
import nl.elec332.gradle.util.abstraction.ITaskDependency;
import nl.elec332.gradle.util.internal.GradleCoreInternals;
import nl.elec332.gradle.util.internal.ProjectObjects;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.dsl.DependencyHandler;
import org.gradle.api.file.FileCollection;
import org.gradle.api.internal.tasks.DefaultTaskDependency;
import org.gradle.nativeplatform.toolchain.NativeToolChainRegistry;
import org.gradle.nativeplatform.toolchain.internal.NativeToolChainRegistryInternal;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * Created by Elec332 on 18-4-2020
 */
@SuppressWarnings("UnstableApiUsage")
public class Utils {

    public static boolean isNullOrEmpty(String s) {
        return s == null || s.equals("");
    }

    public static boolean isUnix() {
        return GradleCoreInternals.getCurrentOs().isUnix();
    }

    public static boolean isWindows() {
        return GradleCoreInternals.getCurrentOs().isWindows();
    }

    public static void addFileDependency(Configuration cfg, DependencyHandler dependencyHandler, FileCollection file) {
        dependencyHandler.add(cfg.getName(), file);
    }

    public static NativeToolChainRegistry realizeToolChainRegistry(Project project) {
        return GradleCoreInternals.getModel(project).realize("toolChains", NativeToolChainRegistryInternal.class);
    }

    public static ITaskDependency newTaskDependency(Project project) {
        DefaultTaskDependency deps = GradleCoreInternals.newTaskDependency(project);
        return new ITaskDependency() {

            @Override
            public Set<Object> getMutableValues() {
                return deps.getMutableValues();
            }

            @Override
            public void setValues(Iterable<?> values) {
                deps.setValues(values);
            }

            @Override
            public ITaskDependency add(Object... values) {
                deps.add(values);
                return this;
            }

            @Nonnull
            @Override
            public Set<? extends Task> getDependencies(@Nullable Task task) {
                return deps.getDependencies(task);
            }

        };
    }

    public static IProjectObjects getProjectObjects(Project project) {
        if (helpers.containsKey(project)) {
            return helpers.get(project);
        }
        ProjectObjects ret = project.getObjects().newInstance(ProjectObjects.class, project);
        helpers.put(project, ret);
        return ret;
    }

    private static final Map<Project, ProjectObjects> helpers = new WeakHashMap<>();

}
