package com.publiccms.logic.component;

import static java.util.Collections.synchronizedList;
import static java.util.Collections.synchronizedMap;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.publiccms.entities.sys.SysExtendField;
import com.publiccms.views.pojo.CmsPageMetadata;
import com.publiccms.views.pojo.CmsPlaceMetadata;
import com.publiccms.views.pojo.ExtendData;
import com.sanluan.common.base.Base;
import com.sanluan.common.base.Cacheable;

@Component
public class MetadataComponent extends Base implements Cacheable {
    private ObjectMapper objectMapper = new ObjectMapper();
    public static String METADATA_FILE = "metadata.data";
    private static List<String> cachedPagelist = synchronizedList(new ArrayList<String>());
    private static Map<String, Map<String, CmsPageMetadata>> cachedPageMap = synchronizedMap(new HashMap<String, Map<String, CmsPageMetadata>>());
    private static List<String> cachedPlacelist = synchronizedList(new ArrayList<String>());
    private static Map<String, Map<String, CmsPlaceMetadata>> cachedPlaceMap = synchronizedMap(new HashMap<String, Map<String, CmsPlaceMetadata>>());

    private void clearPageCache(int size) {
        if (size < cachedPagelist.size()) {
            for (int i = 0; i < size / 10; i++) {
                cachedPageMap.remove(cachedPagelist.remove(0));
            }
        }
    }

    private void clearPlaceCache(int size) {
        if (size < cachedPlacelist.size()) {
            for (int i = 0; i < size / 10; i++) {
                cachedPlaceMap.remove(cachedPlacelist.remove(0));
            }
        }
    }

    /**
     * 获取推荐位元数据
     * 
     * @param filePath
     * @return
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonParseException
     */
    public CmsPlaceMetadata getPlaceMetadata(String filePath) {
        File file = new File(filePath);
        CmsPlaceMetadata pageMetadata = getPlaceMetadataMap(file.getParent()).get(file.getName());
        if (notEmpty(pageMetadata)) {
            return pageMetadata;
        }
        return new CmsPlaceMetadata();
    }

    /**
     * 获取模板元数据
     * 
     * @param filePath
     * @return
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonParseException
     */
    public CmsPageMetadata getTemplateMetadata(String filePath) {
        File file = new File(filePath);
        CmsPageMetadata pageMetadata = getTemplateMetadataMap(file.getParent()).get(file.getName());
        if (notEmpty(pageMetadata)) {
            return pageMetadata;
        }
        return new CmsPageMetadata();
    }

    /**
     * 获取推荐位扩展数据
     * 
     * @param filePath
     * @param extendDataList
     * @return
     */
    public Map<String, String> getPlaceExtendDataMap(String filePath, List<ExtendData> extendDataList) {
        Map<String, String> extendFieldMap = new HashMap<String, String>();
        Map<String, String> map = new HashMap<String, String>();
        List<SysExtendField> extendList = getPlaceMetadata(filePath).getExtendList();
        if (notEmpty(extendList)) {
            for (ExtendData extendData : extendDataList) {
                extendFieldMap.put(extendData.getName(), extendData.getValue());
            }
            for (SysExtendField extend : extendList) {
                String value = extendFieldMap.get(extend.getCode());
                if (notEmpty(value)) {
                    map.put(extend.getCode(), value);
                } else {
                    map.put(extend.getCode(), extend.getDefaultValue());
                }
            }
        }
        return map;
    }

    /**
     * 更新模板元数据
     * 
     * @param filePath
     * @param map
     * @return
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonGenerationException
     */
    public boolean updateTemplateMetadata(String filePath, CmsPageMetadata metadata) {
        File file = new File(filePath);
        String dirPath = file.getParent();
        Map<String, CmsPageMetadata> metadataMap = getTemplateMetadataMap(dirPath);
        metadataMap.put(file.getName(), metadata);
        try {
            saveTemplateMetadata(dirPath, metadataMap);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 更新推荐位元数据
     * 
     * @param filePath
     * @param map
     * @return
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonGenerationException
     */
    public boolean updatePlaceMetadata(String filePath, CmsPlaceMetadata metadata) {
        File file = new File(filePath);
        String dirPath = file.getParent();
        Map<String, CmsPlaceMetadata> metadataMap = getPlaceMetadataMap(dirPath);
        metadataMap.put(file.getName(), metadata);
        try {
            savePlaceMetadata(dirPath, metadataMap);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 删除模板元数据
     * 
     * @param filePath
     * @return
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    public boolean deleteTemplateMetadata(String filePath) {
        File file = new File(filePath);
        String dirPath = file.getParent();
        Map<String, CmsPageMetadata> metadataMap = getTemplateMetadataMap(dirPath);
        metadataMap.remove(file.getName());
        try {
            saveTemplateMetadata(dirPath, metadataMap);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 删除推荐位元数据
     * 
     * @param filePath
     * @return
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    public boolean deletePlaceMetadata(String filePath) {
        File file = new File(filePath);
        String dirPath = file.getParent();
        Map<String, CmsPlaceMetadata> metadataMap = getPlaceMetadataMap(dirPath);
        metadataMap.remove(file.getName());
        try {
            savePlaceMetadata(dirPath, metadataMap);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 获取目录元数据
     * 
     * @param dirPath
     * @return
     */
    public Map<String, CmsPlaceMetadata> getPlaceMetadataMap(String dirPath) {
        Map<String, CmsPlaceMetadata> medatadaMap = cachedPlaceMap.get(dirPath);
        if (empty(medatadaMap)) {
            File file = new File(dirPath + SEPARATOR + METADATA_FILE);
            if (notEmpty(file)) {
                try {
                    medatadaMap = objectMapper.readValue(file, new TypeReference<Map<String, CmsPlaceMetadata>>() {
                    });
                } catch (IOException | ClassCastException e) {
                    medatadaMap = new HashMap<String, CmsPlaceMetadata>();
                }
            } else {
                medatadaMap = new HashMap<String, CmsPlaceMetadata>();
            }
            clearPlaceCache(100);
            cachedPlacelist.add(dirPath);
            cachedPlaceMap.put(dirPath, medatadaMap);
        }
        return medatadaMap;
    }

    /**
     * 获取目录元数据
     * 
     * @param dirPath
     * @return
     */
    public Map<String, CmsPageMetadata> getTemplateMetadataMap(String dirPath) {
        Map<String, CmsPageMetadata> medatadaMap = cachedPageMap.get(dirPath);
        if (empty(medatadaMap)) {
            File file = new File(dirPath + SEPARATOR + METADATA_FILE);
            if (notEmpty(file)) {
                try {
                    medatadaMap = objectMapper.readValue(file, new TypeReference<Map<String, CmsPageMetadata>>() {
                    });
                } catch (IOException | ClassCastException e) {
                    medatadaMap = new HashMap<String, CmsPageMetadata>();
                }
            } else {
                medatadaMap = new HashMap<String, CmsPageMetadata>();
            }
            clearPageCache(100);
            cachedPagelist.add(dirPath);
            cachedPageMap.put(dirPath, medatadaMap);
        }
        return medatadaMap;
    }

    /**
     * 保存模板元数据
     * 
     * @param dirPath
     * @param metadataMap
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    private void saveTemplateMetadata(String dirPath, Map<String, CmsPageMetadata> metadataMap) throws JsonGenerationException,
            JsonMappingException, IOException {
        File file = new File(dirPath + SEPARATOR + METADATA_FILE);
        if (empty(file)) {
            file.getParentFile().mkdirs();
        }
        objectMapper.writeValue(file, metadataMap);
        cachedPagelist.clear();
        cachedPageMap.clear();
    }

    /**
     * 保存推荐位元数据
     * 
     * @param dirPath
     * @param metadataMap
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    private void savePlaceMetadata(String dirPath, Map<String, CmsPlaceMetadata> metadataMap) throws JsonGenerationException,
            JsonMappingException, IOException {
        File file = new File(dirPath + SEPARATOR + METADATA_FILE);
        if (empty(file)) {
            file.getParentFile().mkdirs();
        }
        objectMapper.writeValue(file, metadataMap);
        cachedPlacelist.clear();
        cachedPlaceMap.clear();
    }

    @Override
    public void clear() {
        cachedPagelist.clear();
        cachedPageMap.clear();
        cachedPlacelist.clear();
        cachedPlaceMap.clear();
    }
}
