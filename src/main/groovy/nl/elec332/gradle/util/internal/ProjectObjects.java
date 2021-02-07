package nl.elec332.gradle.util.internal;

import nl.elec332.gradle.util.abstraction.IProjectObjects;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.attributes.Attribute;
import org.gradle.api.attributes.AttributeContainer;
import org.gradle.api.internal.artifacts.configurations.DefaultConfiguration;
import org.gradle.api.internal.attributes.AttributeContainerInternal;
import org.gradle.api.internal.attributes.ImmutableAttributeContainerWithErrorMessage;
import org.gradle.api.internal.attributes.ImmutableAttributes;
import org.gradle.api.internal.attributes.ImmutableAttributesFactory;
import org.gradle.language.nativeplatform.internal.toolchains.ToolChainSelector;

import javax.inject.Inject;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;

/**
 * Created by Elec332 on 2/7/2021
 */
public class ProjectObjects implements IProjectObjects {

    @Inject
    public ProjectObjects(Project project, ToolChainSelector toolChainSelector, ImmutableAttributesFactory attributesFactory) {
        this.project = project;
        this.toolChainSelector = toolChainSelector;
        this.attributesFactory = attributesFactory;
    }

    private final Project project;
    public final ToolChainSelector toolChainSelector;
    public final ImmutableAttributesFactory attributesFactory;

    @Override
    public <T> AttributeContainer concat(AttributeContainer attributeContainer, Attribute<T> attribute, T value) {
        if (attributeContainer instanceof AttributeContainerInternal) {
            return attributesFactory.concat(((AttributeContainerInternal) attributeContainer).asImmutable(), attribute, value);
        }
        throw new IllegalArgumentException();
    }

    @Override
    public <T> void modifyAttributes(Configuration config, Attribute<T> key, T value) {
        if (config == null) {
            return;
        }
        if (config.getAttributes() instanceof ImmutableAttributes || config.getAttributes() instanceof ImmutableAttributeContainerWithErrorMessage) {
            try {
                attributeSetterC.invoke(config, concat(config.getAttributes(), key, value));
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        } else {
            config.getAttributes().attribute(key, value);
        }
    }

    private static final MethodHandle attributeSetterC;

    static {
        try {
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            Field f = DefaultConfiguration.class.getDeclaredField("configurationAttributes");
            f.setAccessible(true);
            attributeSetterC = lookup.unreflectSetter(f);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize helper.", e);
        }
    }

}
