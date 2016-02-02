package com.hlops.tv42.core.services.impl;

import com.hlops.tv42.core.bean.Link;
import com.hlops.tv42.core.services.DbService;
import com.hlops.tv42.core.services.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by tom on 2/2/16.
 */
@Service
public class LinkServiceImpl implements LinkService {

    @Autowired
    DbService dbService;

    @Override
    public Link getLink(String channelName) {
        return null;
    }
}
