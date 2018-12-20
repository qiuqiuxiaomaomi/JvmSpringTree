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
	//bean��������
    private String scope;
	//�Ƿ��ǳ���
    private boolean abstractFlag;
	//bean������������
    private boolean lazyInit;
	//�Զ�ע��ģʽ
    private int autowireMode;
    private int dependencyCheck;
	//������ʾһ��bean��ʵ����������һ��bean��ʵ����
    private String[] dependsOn;
	//autowire-candidate��������Ϊfalse�����������ڲ����Զ�װ�����ʱ��
    //�������Ǹ�bean���������ᱻ������Ϊ����bean�Զ�װ��ĺ�ѡ�ߣ�
    //���Ǹ�bean�����ǿ���ʹ���Զ�װ����ע������bean��
    private boolean autowireCandidate;
	//�Զ�װ��ʱ���ֶ��bean��ѡ��ʱ������Ϊ��ѡ��
    private boolean primary;
	//���ڼ�¼Qualifier
    private final Map<String, AutowireCandidateQualifier> qualifiers;
	//������ʷǹ����Ĺ������ͷ�������������
    private boolean nonPublicAccessAllowed;
	//�Ƿ���һ�ֿ��ɵ�ģʽ�������캯����Ĭ��Ϊtrue
    private boolean lenientConstructorResolution;
    private String factoryBeanName;
    private String factoryMethodName;
	//��¼���캯��ע�����ԣ���Ӧbean����constructor-arg
    private ConstructorArgumentValues constructorArgumentValues;
    private MutablePropertyValues propertyValues;
    private MethodOverrides methodOverrides;
	//��ʼ����������Ӧbean����init-method
    private String initMethodName;
	//bean�����ٷ���
    private String destroyMethodName;
	//�Ƿ�ִ��init-method����������
    private boolean enforceInitMethod;
	//�Ƿ�ִ��destroy-method����������
    private boolean enforceDestroyMethod;
    private boolean synthetic;
    private int role;
	//bean��������Ϣ
    private String description;
	//bean�������Դ
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
