package com.publiccms.logic.component;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.spi.Cacheable;
import com.publiccms.logic.service.tools.HqlService;
import com.sanluan.common.base.Base;

@Component
public class CacheComponent extends Base {
    @Autowired
    private List<Cacheable> cacheableList;
    @Autowired
    private HqlService hqlService;

    public void clear() {
        for (Cacheable cache : cacheableList) {
            cache.clear();
        }
        hqlService.clear();
    }
}
