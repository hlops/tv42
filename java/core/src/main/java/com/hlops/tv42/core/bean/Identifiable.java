package com.hlops.tv42.core.bean;

import java.io.Serializable;

import org.jetbrains.annotations.NotNull;

/**
 * Created by IntelliJ IDEA.
 * User: akarnachuk
 * Date: 1/13/16
 * Time: 11:38 AM
 */
public interface Identifiable extends Serializable, Cloneable {

    @NotNull
    String getId();

    <T extends Identifiable> T clone() throws CloneNotSupportedException;
}
