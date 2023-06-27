package nl.elec332.gradle.util.abstraction;

import org.gradle.api.artifacts.Configuration;
import org.gradle.api.attributes.Attribute;
import org.gradle.api.attributes.AttributeContainer;
import org.gradle.api.internal.attributes.ImmutableAttributesFactory;

/**
 * Created by Elec332 on 2/7/2021
 */
public interface IProjectObjects  {

    <T> AttributeContainer concat(AttributeContainer attributeContainer, Attribute<T> attribute, T value);

    <T> void modifyAttributes(Configuration config, Attribute<T> key, T value);

    ImmutableAttributesFactory getAttributesFactory();

}
