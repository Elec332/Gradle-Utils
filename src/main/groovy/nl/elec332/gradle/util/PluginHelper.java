package nl.elec332.gradle.util;

import org.gradle.tooling.UnsupportedVersionException;
import org.gradle.util.GradleVersion;

/**
 * Created by Elec332 on 6-4-2020
 */
public class PluginHelper {

    public static void checkMinimumGradleVersion(String version) {
        if (!isAtLeastVersion(version)) {
            throw new UnsupportedVersionException("This plugin requires at least Gradle version " + version);
        }
    }

    public static boolean isAtLeastVersion(String version) {
        return GradleVersion.current().compareTo(GradleVersion.version(version)) >= 0;
    }

}
