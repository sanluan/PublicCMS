package ${base}.${directivePack};

// Generated ${.now} by SourceMaker

import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

<#include "../include_imports/entity.ftl">

<#include "../include_imports/service.ftl">

import com.sanluan.common.base.BaseDirective;
import com.sanluan.common.handler.RenderHandler;

@Component
public class ${entityName}Directive extends BaseDirective {

	@Override
	public void execute(RenderHandler handler) throws IOException, Exception {
		Integer id = handler.getInteger("id");
		if (null != id) {
			${entityName} entity = service.getEntity(id);
			handler.put("object", entity).renderIfNotNull(entity);
		} else {
			Integer[] ids = handler.getIntegerArray("ids");
			if (isNotEmpty(ids)) {
				List<${entityName}> entityList = service.getEntitys(ids);
				Map<String, ${entityName}> map = new HashMap<String, ${entityName}>();
				for (${entityName} entity : entityList) {
					map.put(String.valueOf(entity.getId()), entity);
				}
				handler.put("map", map).render();
			}
		}
	}

	@Autowired
	private ${entityName}Service service;

}
