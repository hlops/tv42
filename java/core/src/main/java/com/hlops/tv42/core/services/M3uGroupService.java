package com.hlops.tv42.core.services;

import com.hlops.tv42.core.bean.M3uChannel;
import com.hlops.tv42.core.bean.M3uGroup;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: akarnachuk
 * Date: 3/10/16
 * Time: 1:33 PM
 */
public interface M3uGroupService {

    Collection<M3uGroup> getGroups();

    void actualizeGroups(Collection<M3uChannel> channels);
}
