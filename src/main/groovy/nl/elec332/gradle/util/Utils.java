package nl.elec332.gradle.util;

import nl.elec332.gradle.util.internal.GradleCoreInternals;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.dsl.DependencyHandler;
import org.gradle.api.file.FileCollection;

/**
 * Created by Elec332 on 18-4-2020
 */
public class Utils {

    public static boolean isUnix() {
        return GradleCoreInternals.getCurrentOs().isUnix();
    }

    public static boolean isWindows() {
        return GradleCoreInternals.getCurrentOs().isWindows();
    }

    public static boolean isNullOrEmpty(String s) {
        return s == null || s.equals("");
    }

    public static void addFileDependency(Configuration cfg, DependencyHandler dependencyHandler, FileCollection file) {
        dependencyHandler.add(cfg.getName(), file);
    }

}
