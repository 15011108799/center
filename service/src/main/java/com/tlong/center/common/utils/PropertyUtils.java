package com.tlong.center.common.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Objects;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.Assert;


public class PropertyUtils {
    public PropertyUtils() {
    }

    public static void copyPropertiesOfNotNull(Object source, Object target, String... excludeProperties) {
        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");
        Class<?> actualEditable = target.getClass();
        PropertyDescriptor[] targetPds = BeanUtils.getPropertyDescriptors(actualEditable);
        PropertyDescriptor[] var5 = targetPds;
        int var6 = targetPds.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            PropertyDescriptor targetPd = var5[var7];
            String propertyName = targetPd.getName();
            if (!ArrayUtils.contains(excludeProperties, propertyName)) {
                Method writeMethod = targetPd.getWriteMethod();
                if (writeMethod != null) {
                    PropertyDescriptor sourcePd = BeanUtils.getPropertyDescriptor(source.getClass(), targetPd.getName());
                    if (sourcePd != null) {
                        Method readMethod = sourcePd.getReadMethod();
                        if (readMethod != null && Objects.equals(writeMethod.getGenericParameterTypes()[0], readMethod.getGenericReturnType())) {
                            try {
                                if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                                    readMethod.setAccessible(true);
                                }

                                Object value = readMethod.invoke(source);
                                if (value != null) {
                                    if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                        writeMethod.setAccessible(true);
                                    }

                                    writeMethod.invoke(target, value);
                                }
                            } catch (Throwable var14) {
                                throw new FatalBeanException("Could not copy property '" + targetPd.getName() + "' from source to target", var14);
                            }
                        }
                    }
                }
            }
        }

    }

    public static Object getPropertyValue(Object obj, String propertyName) {
        try {
            return BeanUtils.getPropertyDescriptor(obj.getClass(), propertyName).getReadMethod().invoke(obj);
        } catch (Exception var3) {
            throw new FatalBeanException("Could not read property " + propertyName, var3);
        }
    }
}
