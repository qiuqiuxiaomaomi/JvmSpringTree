//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.springframework.beans.factory.support;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.BeanMetadataAttributeAccessor;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.core.io.DescriptiveResource;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

public abstract class AbstractBeanDefinition extends BeanMetadataAttributeAccessor implements BeanDefinition, Cloneable {
    public static final String SCOPE_DEFAULT = "";
    public static final int AUTOWIRE_NO = 0;
    public static final int AUTOWIRE_BY_NAME = 1;
    public static final int AUTOWIRE_BY_TYPE = 2;
    public static final int AUTOWIRE_CONSTRUCTOR = 3;
    /** @deprecated */
    @Deprecated
    public static final int AUTOWIRE_AUTODETECT = 4;
    public static final int DEPENDENCY_CHECK_NONE = 0;
    public static final int DEPENDENCY_CHECK_OBJECTS = 1;
    public static final int DEPENDENCY_CHECK_SIMPLE = 2;
    public static final int DEPENDENCY_CHECK_ALL = 3;
    public static final String INFER_METHOD = "(inferred)";
    private volatile Object beanClass;
	//bean的作用域
    private String scope;
	//是否是抽象
    private boolean abstractFlag;
	//bean的懒加载属性
    private boolean lazyInit;
	//自动注入模式
    private int autowireMode;
    private int dependencyCheck;
	//用来表示一个bean的实例化依靠另一个bean先实例化
    private String[] dependsOn;
	//autowire-candidate属性设置为false，这样容器在查找自动装配对象时，
    //将不考虑该bean，即它不会被考虑作为其他bean自动装配的候选者，
    //但是该bean本身还是可以使用自动装配来注入其他bean的
    private boolean autowireCandidate;
	//自动装配时出现多个bean候选者时，将作为首选者
    private boolean primary;
	//用于记录Qualifier
    private final Map<String, AutowireCandidateQualifier> qualifiers;
	//允许访问非公开的构造器和方法，程序设置
    private boolean nonPublicAccessAllowed;
	//是否以一种宽松的模式解析构造函数，默认为true
    private boolean lenientConstructorResolution;
    private String factoryBeanName;
    private String factoryMethodName;
	//记录构造函数注入属性，对应bean属性constructor-arg
    private ConstructorArgumentValues constructorArgumentValues;
    private MutablePropertyValues propertyValues;
    private MethodOverrides methodOverrides;
	//初始化方法，对应bean属性init-method
    private String initMethodName;
	//bean的销毁方法
    private String destroyMethodName;
	//是否执行init-method，程序设置
    private boolean enforceInitMethod;
	//是否执行destroy-method，程序设置
    private boolean enforceDestroyMethod;
    private boolean synthetic;
    private int role;
	//bean的描述信息
    private String description;
	//bean定义的资源
    private Resource resource;

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else if (!(other instanceof AbstractBeanDefinition)) {
            return false;
        } else {
            AbstractBeanDefinition that = (AbstractBeanDefinition)other;
            if (!ObjectUtils.nullSafeEquals(this.getBeanClassName(), that.getBeanClassName())) {
                return false;
            } else if (!ObjectUtils.nullSafeEquals(this.scope, that.scope)) {
                return false;
            } else if (this.abstractFlag != that.abstractFlag) {
                return false;
            } else if (this.lazyInit != that.lazyInit) {
                return false;
            } else if (this.autowireMode != that.autowireMode) {
                return false;
            } else if (this.dependencyCheck != that.dependencyCheck) {
                return false;
            } else if (!Arrays.equals(this.dependsOn, that.dependsOn)) {
                return false;
            } else if (this.autowireCandidate != that.autowireCandidate) {
                return false;
            } else if (!ObjectUtils.nullSafeEquals(this.qualifiers, that.qualifiers)) {
                return false;
            } else if (this.primary != that.primary) {
                return false;
            } else if (this.nonPublicAccessAllowed != that.nonPublicAccessAllowed) {
                return false;
            } else if (this.lenientConstructorResolution != that.lenientConstructorResolution) {
                return false;
            } else if (!ObjectUtils.nullSafeEquals(this.constructorArgumentValues, that.constructorArgumentValues)) {
                return false;
            } else if (!ObjectUtils.nullSafeEquals(this.propertyValues, that.propertyValues)) {
                return false;
            } else if (!ObjectUtils.nullSafeEquals(this.methodOverrides, that.methodOverrides)) {
                return false;
            } else if (!ObjectUtils.nullSafeEquals(this.factoryBeanName, that.factoryBeanName)) {
                return false;
            } else if (!ObjectUtils.nullSafeEquals(this.factoryMethodName, that.factoryMethodName)) {
                return false;
            } else if (!ObjectUtils.nullSafeEquals(this.initMethodName, that.initMethodName)) {
                return false;
            } else if (this.enforceInitMethod != that.enforceInitMethod) {
                return false;
            } else if (!ObjectUtils.nullSafeEquals(this.destroyMethodName, that.destroyMethodName)) {
                return false;
            } else if (this.enforceDestroyMethod != that.enforceDestroyMethod) {
                return false;
            } else if (this.synthetic != that.synthetic) {
                return false;
            } else {
                return this.role != that.role ? false : super.equals(other);
            }
        }
    }

    public int hashCode() {
        int hashCode = ObjectUtils.nullSafeHashCode(this.getBeanClassName());
        hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(this.scope);
        hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(this.constructorArgumentValues);
        hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(this.propertyValues);
        hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(this.factoryBeanName);
        hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(this.factoryMethodName);
        hashCode = 29 * hashCode + super.hashCode();
        return hashCode;
    }
}
