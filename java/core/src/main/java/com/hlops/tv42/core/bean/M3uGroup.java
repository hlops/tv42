package com.hlops.tv42.core.bean;

import org.jetbrains.annotations.NotNull;

/**
 * Created by IntelliJ IDEA.
 * User: akarnachuk
 * Date: 2/3/16
 * Time: 12:14 PM
 */
public class M3uGroup implements Identifiable<M3uGroup> {

    private final String name;
    private String order;

    public M3uGroup(String name) {
        this.name = name;
    }

    @NotNull
    @Override
    public String getId() {
        return name;
    }

    public String getName() {
        return name;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    @SuppressWarnings("unchecked")
    @Override
    public M3uGroup clone() throws CloneNotSupportedException {
        return (M3uGroup) super.clone();
    }

    @Override
    public M3uGroup combine(M3uGroup oldValue) throws CloneNotSupportedException {
        return clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof M3uGroup)) return false;

        M3uGroup m3uGroup = (M3uGroup) o;

        if (!name.equals(m3uGroup.name)) return false;
        return !(order != null ? !order.equals(m3uGroup.order) : m3uGroup.order != null);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (order != null ? order.hashCode() : 0);
        return result;
    }
}
