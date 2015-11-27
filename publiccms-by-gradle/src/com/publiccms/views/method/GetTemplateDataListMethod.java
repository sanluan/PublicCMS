package com.publiccms.views.method;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.logic.component.FileComponent;
import com.sanluan.common.base.BaseMethod;

import freemarker.template.TemplateModelException;

@Component
public class GetTemplateDataListMethod extends BaseMethod {

    /*
     * (non-Javadoc)
     * 
     * @see freemarker.template.TemplateMethodModelEx#exec(java.util.List)
     */
    @SuppressWarnings("unchecked")
    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        String path = getString(0, arguments);
        Long createDate = getLong(1, arguments);
        if (isNotEmpty(path) && null != createDate) {
            List<Map<String, Object>> dataList = fileComponent.getListData(path);
            for (Map<String, Object> map : dataList) {
                if (createDate.equals(map.get("createDate"))) {
                    return map;
                }
            }
            return null;
        }
        return null;
    }

    @Autowired
    private FileComponent fileComponent;
}
