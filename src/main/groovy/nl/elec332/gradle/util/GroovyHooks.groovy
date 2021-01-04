package nl.elec332.gradle.util

import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.artifacts.maven.MavenDeployer
import org.gradle.api.component.SoftwareComponent
import org.gradle.language.BinaryCollection
import org.gradle.nativeplatform.toolchain.NativeToolChainRegistry

import java.util.function.Consumer

/**
 * Created by Elec332 on 18-4-2020
 */
class GroovyHooks {

    static void dependencies(Project project, Consumer<DependencyHandler> c) {
        project.dependencies {
            c.accept(it)
        }
    }

    static void addArtifact(Project project, Object from) {
        project.artifacts {
            archives from
        }
    }

    static void configureMaven(Project project, Consumer<MavenDeployer> consumer) {
        project.uploadArchives {
            repositories {
                mavenDeployer {
                    consumer.accept(it)
                }
            }
        }
    }

    static void configureToolchains(Project project, Consumer<NativeToolChainRegistry> consumer) {
        project.model {
            toolChains {
                consumer.accept(it)
            }
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

}
