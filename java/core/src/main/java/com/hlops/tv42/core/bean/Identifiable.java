package com.hlops.tv42.core.bean;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: akarnachuk
 * Date: 1/13/16
 * Time: 11:38 AM
 */
public interface Identifiable<T> extends Serializable, Cloneable {

    @NotNull
    String getId();

    T clone() throws CloneNotSupportedException;

}
