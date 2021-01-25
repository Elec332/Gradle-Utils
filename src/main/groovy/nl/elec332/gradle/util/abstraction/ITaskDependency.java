package nl.elec332.gradle.util.abstraction;

import org.gradle.api.tasks.TaskDependency;

import java.util.Set;

/**
 * Created by Elec332 on 1/25/2021
 */
public interface ITaskDependency extends TaskDependency {

    Set<Object> getMutableValues();

    void setValues(Iterable<?> values);

    ITaskDependency add(Object... values);

}
