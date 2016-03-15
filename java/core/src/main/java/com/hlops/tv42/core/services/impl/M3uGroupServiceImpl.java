package com.hlops.tv42.core.services.impl;

import com.hlops.tv42.core.bean.M3uChannel;
import com.hlops.tv42.core.bean.M3uGroup;
import com.hlops.tv42.core.services.DbService;
import com.hlops.tv42.core.services.M3uGroupService;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: akarnachuk
 * Date: 3/10/16
 * Time: 1:33 PM
 */
@Service
public class M3uGroupServiceImpl extends GenericServiceImpl<M3uGroup> implements M3uGroupService {

    @Override
    protected DbService.Entity getEntity() {
        return DbService.Entity.m3uGroups;
    }

    @Override
    public Collection<M3uGroup> getGroups() {
        return values();
    }

    @Override
    public void actualizeGroups(Collection<M3uChannel> channels) {
        for (M3uChannel channel : channels) {
            String groupName = channel.getGroup();
            if (groupName != null) {
                map().computeIfAbsent(groupName, p -> new M3uGroup(groupName));
            }
        }
    }
}
