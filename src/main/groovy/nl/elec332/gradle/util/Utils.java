package nl.elec332.gradle.util;

import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.dsl.DependencyHandler;
import org.gradle.api.file.FileCollection;

import java.io.File;

/**
 * Created by Elec332 on 18-4-2020
 */
public class Utils {

    public static boolean isNullOrEmpty(String s) {
        return s == null || s.equals("");
    }

    public static void addFileDependency(Configuration cfg, DependencyHandler dependencyHandler, FileCollection file) {
        dependencyHandler.add(cfg.getName(), file);
    }

}
