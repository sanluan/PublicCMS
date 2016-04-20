package com.publiccms.views.pojo;

import java.util.List;

import com.publiccms.entities.sys.SysExtendField;
import com.sanluan.common.base.Base;

public class CmsCategoryTypeParamters extends Base {
    private List<SysExtendField> categoryExtends;

    public List<SysExtendField> getCategoryExtends() {
        return categoryExtends;
    }

    public void setCategoryExtends(List<SysExtendField> categoryExtends) {
        this.categoryExtends = categoryExtends;
    }
}