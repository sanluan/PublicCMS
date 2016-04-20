package com.publiccms.views.pojo;

import java.util.List;

import com.publiccms.entities.sys.SysExtendField;
import com.sanluan.common.base.Base;

public class CmsModelParamters extends Base {
    List<SysExtendField> contentExtends;

    public List<SysExtendField> getContentExtends() {
        return contentExtends;
    }

    public void setContentExtends(List<SysExtendField> contentExtends) {
        this.contentExtends = contentExtends;
    }
}
