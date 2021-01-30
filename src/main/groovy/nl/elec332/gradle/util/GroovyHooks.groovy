package nl.elec332.gradle.util

import org.gradle.api.Project

/**
 * Created by Elec332 on 18-4-2020
 */
class GroovyHooks {

    static void addArtifact(Project project, Object from) {
        project.artifacts {
            archives from
        }
    }

    static Process createProcess(String cmd) {
        def ret = cmd.execute()
        ret.waitFor()
        return ret
    }

    static String processOutputToText(Process process) {
        return process.in.text;
    }

    static void inject(Object target, Object toInject) {
        target << toInject
    }

    @Deprecated
    static String getVersion(Project project) {
        return project.version
    }

}
