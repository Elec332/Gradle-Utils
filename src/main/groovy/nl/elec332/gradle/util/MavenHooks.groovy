package nl.elec332.gradle.util

import org.gradle.api.Project
import org.gradle.api.artifacts.ModuleDependency
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.api.publish.maven.MavenDependency
import org.gradle.api.publish.maven.MavenPublication

import java.util.function.Consumer


/**
 * Created by Elec332 on 1/30/2021
 */
class MavenHooks {

    static void configureMaven(Project project, Consumer<MavenPublication> consumer) {
        configureMaven(project, "publishDefaultMaven", consumer);
    }

    static void configureMaven(Project project, String name, Consumer<MavenPublication> consumer) {
        project.publishing {
            publications {
                "$name"(MavenPublication) {
                    consumer.accept(it)
                }
            }
        }
    }

    static void setMavenRepository(Project project, Consumer<MavenArtifactRepository> consumer) {
        project.publishing {
            repositories {
                maven {
                    consumer.accept(it)
                }
            }
        }
    }

    static void addDependency(MavenPublication publication, ModuleDependency dependency, String scope) {
        Set<MavenDependency> deps;
        if (scope.equals("compile")) {
            deps = dependency.getApiDependencyConstraints()
        } else if (scope.equals("runtime")) {
            deps = dependency.getRuntimeDependencyConstraints()
        } else {
            throw new UnsupportedOperationException("Unmsupported scope: " + scope)
        }
        dependency.addModuleDependency(dependency, Collections.emptySet(), deps);
    }

}
