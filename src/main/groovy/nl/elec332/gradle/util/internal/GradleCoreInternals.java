package nl.elec332.gradle.util.internal;

import org.gradle.internal.jvm.Jvm;
import org.gradle.internal.os.OperatingSystem;
import org.gradle.nativeplatform.platform.NativePlatform;
import org.gradle.nativeplatform.platform.internal.OperatingSystemInternal;

/**
 * Created by Elec332 on 1/10/2021
 */
public class GradleCoreInternals {

    public static org.gradle.internal.os.OperatingSystem getOperatingSystemInfo(NativePlatform platform) {
        return ((OperatingSystemInternal) platform.getOperatingSystem()).getInternalOs();
    }

    public static OperatingSystem getCurrentOs() {
        return OperatingSystem.current();
    }

    public static Jvm getJvm() {
        return Jvm.current();
    }

}
