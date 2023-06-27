package nl.elec332.gradle.util.internal;

import org.gradle.api.Project;
import org.gradle.api.internal.project.ProjectInternal;
import org.gradle.api.internal.tasks.DefaultTaskDependency;
import org.gradle.api.internal.tasks.TaskContainerInternal;
import org.gradle.internal.jvm.Jvm;
import org.gradle.internal.os.OperatingSystem;
import org.gradle.internal.service.ServiceRegistry;
import org.gradle.model.internal.registry.ModelRegistry;
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

    public static ServiceRegistry getServices(Project project) {
        return ((ProjectInternal) project).getServices();
    }

    public static ModelRegistry getModel(Project project) {
        return getServices(project).get(ModelRegistry.class);
    }

//    public static FileOperations getFileOperations(Project project, File source) {
//        ServiceRegistry services = getServices(project);
//        FileLookup fileLookup = services.get(FileLookup.class);
//        FileCollectionFactory fileCollectionFactory = services.get(FileCollectionFactory.class);
//        FileResolver resolver = fileLookup.getFileResolver(source);
//        FileCollectionFactory fileCollectionFactoryWithBase = fileCollectionFactory.withResolver(resolver);
//        return DefaultFileOperations.createSimple(resolver, fileCollectionFactoryWithBase, services);
//    }

    public static DefaultTaskDependency newTaskDependency(Project project) {
        TaskContainerInternal tasks = (TaskContainerInternal) project.getTasks();
        return new DefaultTaskDependency(tasks, null);
    }

}
