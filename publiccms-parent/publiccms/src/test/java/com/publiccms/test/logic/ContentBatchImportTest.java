package com.publiccms.test.logic;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.drew.imaging.FileType;
import com.drew.imaging.FileTypeDetector;
import com.publiccms.common.constants.CmsVersion;
import com.publiccms.common.constants.Constants;
import com.publiccms.common.tools.CmsFileUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.cms.CmsContentAttribute;
import com.publiccms.entities.cms.CmsContentFile;
import com.publiccms.logic.service.cms.CmsContentAttributeService;
import com.publiccms.logic.service.cms.CmsContentFileService;
import com.publiccms.logic.service.cms.CmsContentService;

import config.spring.ApplicationConfig;

@DisplayName("Content batch import")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ApplicationConfig.class)
class ContentBatchImportTest {
    protected static final Log log = LogFactory.getLog(ContentBatchImportTest.class);
    @Resource
    private CmsContentService contentService;
    @Resource
    private CmsContentAttributeService contentAttributeService;
    @Resource
    private CmsContentFileService contentFileService;

    @BeforeAll
    public static void init() {
        // 不进入安装程序 数据目录有 database.properties才能进行测试
        CmsVersion.setInitialized(true);
        CmsVersion.setScheduled(false);
    }

    /**
     * 
     */
    @Test
    @DisplayName("insert test case")
    void insertTest() {
        try {
            List<Company> list = Constants.objectMapper.readValue(new File("D://a.txt"),
                    Constants.objectMapper.getTypeFactory().constructCollectionLikeType(List.class, Company.class));
            for (Company c : list) {
                CmsContent entity = new CmsContent((short) 1, c.getName(), 1, 18, "company", false, true, false, false, 0,
                        new Date(), new Date(), 0, 1);
                entity.setDescription(c.getDescription());
                if (CommonUtils.notEmpty(c.getImages())) {
                    entity.setCover(getUrl(c.getImages().get(0)));
                }
                contentService.save(entity);
                CmsContentAttribute attribute = new CmsContentAttribute(entity.getId(), 0);
                contentAttributeService.save(attribute);
                if (CommonUtils.notEmpty(c.getCers())) {
                    for (String filePath : c.getCers()) {
                        CmsContentFile file = new CmsContentFile(entity.getId(), 1, getUrl(filePath),
                                CmsFileUtils.FILE_TYPE_IMAGE, 0, 0);
                        contentFileService.save(file);
                    }
                }
                if (CommonUtils.notEmpty(c.getProducts())) {
                    for (Product p : c.getProducts()) {
                        CmsContent product = new CmsContent((short) 1, p.getName(), 1, 18, "product", false, true, false, false,
                                0, new Date(), new Date(), 0, 1);
                        product.setParentId(entity.getId());
                        if (CommonUtils.notEmpty(p.getImages())) {
                            product.setCover(getUrl(p.getImages().iterator().next()));
                        }
                        contentService.save(product);
                        CmsContentAttribute pattribute = new CmsContentAttribute(product.getId(), 0);
                        Map<String, String> data = new HashMap<>();
                        if (null != p.getCodes()) {
                            data.put("code", p.getCodes());
                        }
                        if (null != p.getBases()) {
                            data.put("base", p.getBases());
                        }
                        if (null != p.getBaseAddress()) {
                            data.put("address", p.getBaseAddress());
                        }
                        if (!data.isEmpty()) {
                            pattribute.setData(Constants.objectMapper.writeValueAsString(data));
                        }
                        contentAttributeService.save(pattribute);
                        if (CommonUtils.notEmpty(p.getBaseImages())) {
                            for (String filePath : p.getBaseImages()) {
                                CmsContentFile file = new CmsContentFile(product.getId(), 1, getUrl(filePath),
                                        CmsFileUtils.FILE_TYPE_IMAGE, 0, 0);
                                contentFileService.save(file);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
        }
    }

    public static String getUrl(String url) {
        String fileName = null;
        try (CloseableHttpClient httpclient = HttpClients.custom().setDefaultRequestConfig(Constants.defaultRequestConfig)
                .build()) {
            HttpGet httpget = new HttpGet(url);
            CloseableHttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                BufferedInputStream inputStream = new BufferedInputStream(entity.getContent());
                FileType fileType = FileTypeDetector.detectFileType(inputStream);
                String suffix = fileType.getCommonExtension();
                if (null != fileType.getMimeType() && fileType.getMimeType().startsWith("image/")
                        && CommonUtils.notEmpty(suffix)) {
                    fileName = CmsFileUtils.getUploadFileName(suffix);
                    String filePath = "D:/aaa/" + fileName;
                    CmsFileUtils.copyInputStreamToFile(inputStream, filePath);
                }
            }
            EntityUtils.consume(entity);
        } catch (IOException e) {
            log.info(e.getMessage());
            return getUrl(url);
        }
        return fileName;

    }

    public static void main(String[] args) {
        try (Workbook wb = WorkbookFactory.create(new FileInputStream(new File("D:/1.xlsx")))) {
            Sheet sheet = wb.getSheetAt(0);
            Row row = null;
            int lastRowNum = sheet.getLastRowNum();
            List<Company> list = new ArrayList<>();
            Company lastCompany = null;
            Product lastProduct = null;
            for (int rowNum = sheet.getFirstRowNum() + 1; rowNum <= lastRowNum; rowNum++) {
                row = sheet.getRow(rowNum);
                if (row == null) {
                    continue;
                }
                Company c = null;
                if (null != lastCompany && getCellValue(row.getCell(0)).equalsIgnoreCase(lastCompany.getName())) {
                } else {
                    c = new Company();
                    c.setName(getCellValue(row.getCell(0)));
                    c.setDescription(getCellValue(row.getCell(1)));
                    String images = getCellValue(row.getCell(2));
                    if (CommonUtils.notEmpty(images)) {
                        for (String temp : images.split(",")) {
                            c.getImages().add(temp.replace("[", "").replace("]", "").replace("\"", ""));
                        }
                    }
                    String cers = getCellValue(row.getCell(3));
                    if (CommonUtils.notEmpty(cers)) {
                        for (String temp : cers.split(",")) {
                            c.getCers().add(temp.replace("[", "").replace("]", "").replace("\"", ""));
                        }
                    }
                    lastCompany = c;
                    lastProduct = null;
                }
                if (null != lastProduct && getCellValue(row.getCell(5)).equalsIgnoreCase(lastProduct.getName())) {
                    if (null == lastProduct.getCodes()) {
                        lastProduct.setCodes(getCellValue(row.getCell(8)));
                    } else if (!lastProduct.getCodes().contains(getCellValue(row.getCell(8)))) {
                        lastProduct.setCodes(lastProduct.getCodes() + "," + getCellValue(row.getCell(8)));
                    }
                    if (null == lastProduct.getBases()) {
                        lastProduct.setBases(getCellValue(row.getCell(10)));
                    } else if (!lastProduct.getBases().contains(getCellValue(row.getCell(10)))) {
                        lastProduct.setBases(lastProduct.getBases() + "," + getCellValue(row.getCell(10)));
                    }
                    if (null == lastProduct.getBaseAddress()) {
                        lastProduct.setBaseAddress(getCellValue(row.getCell(17)));
                    } else if (!lastProduct.getBaseAddress().contains(getCellValue(row.getCell(17)))) {
                        lastProduct.setBaseAddress(lastProduct.getBaseAddress() + "," + getCellValue(row.getCell(17)));
                    }
                    String images = getCellValue(row.getCell(9));
                    if (CommonUtils.notEmpty(images)) {
                        for (String temp : images.split(",")) {
                            lastProduct.getImages().add(temp.replace("[", "").replace("]", "").replace("\"", ""));
                        }
                    }
                    String baseImages = getCellValue(row.getCell(13));
                    if (CommonUtils.notEmpty(baseImages)) {
                        for (String temp : baseImages.split(",")) {
                            lastProduct.getBaseImages().add(temp.replace("[", "").replace("]", "").replace("\"", ""));
                        }
                    }
                } else {
                    Product p = new Product();
                    p.setName(getCellValue(row.getCell(5)));
                    if (null == p.getCodes()) {
                        p.setCodes(getCellValue(row.getCell(8)));
                    } else {
                        p.setCodes(p.getCodes() + "," + getCellValue(row.getCell(8)));
                    }
                    if (null == p.getBases()) {
                        p.setBases(getCellValue(row.getCell(10)));
                    } else if (!p.getBases().contains(getCellValue(row.getCell(10)))) {
                        p.setBases(p.getBases() + "," + getCellValue(row.getCell(10)));
                    }
                    if (null == p.getBaseAddress()) {
                        p.setBaseAddress(getCellValue(row.getCell(17)));
                    } else if (!p.getBaseAddress().contains(getCellValue(row.getCell(17)))) {
                        p.setBaseAddress(p.getBaseAddress() + "," + getCellValue(row.getCell(17)));
                    }
                    String images = getCellValue(row.getCell(9));
                    if (p.getName().equals("西林红茶")) {
                        log.info(images);
                    }
                    if (CommonUtils.notEmpty(images)) {
                        for (String temp : images.split(",")) {
                            p.getImages().add(temp.replace("[", "").replace("]", "").replace("\"", ""));
                        }
                    }
                    String baseImages = getCellValue(row.getCell(13));
                    if (CommonUtils.notEmpty(baseImages)) {
                        for (String temp : baseImages.split(",")) {
                            p.getBaseImages().add(temp.replace("[", "").replace("]", "").replace("\"", ""));
                        }
                    }
                    lastCompany.getProducts().add(p);
                    lastProduct = p;
                }
                if (null != c) {
                    list.add(c);
                }
                Constants.objectMapper.writeValue(new File("D://a.txt"), list);
            }
        } catch (EncryptedDocumentException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取表格单元格Cell内容
     */
    private static String getCellValue(Cell cell) {
        String result = new String();
        if (null != cell) {
            switch (cell.getCellType()) {
            case NUMERIC:// 数字类型
                if (DateUtil.isCellDateFormatted(cell)) {// 处理日期格式、时间格式
                    SimpleDateFormat sdf = null;
                    if (cell.getCellStyle().getDataFormat() == HSSFDataFormat.getBuiltinFormat("h:mm")) {
                        sdf = new SimpleDateFormat("HH:mm");
                    } else {// 日期
                        sdf = new SimpleDateFormat("yyyy-MM-dd");
                    }
                    Date date = cell.getDateCellValue();
                    result = sdf.format(date);
                } else if (cell.getCellStyle().getDataFormat() == 58) {
                    // 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    double value = cell.getNumericCellValue();
                    Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
                    result = sdf.format(date);
                } else {
                    double value = cell.getNumericCellValue();
                    CellStyle style = cell.getCellStyle();
                    DecimalFormat format = new DecimalFormat();
                    String temp = style.getDataFormatString();
                    // 单元格设置成常规
                    if (temp.equals("General")) {
                        format.applyPattern("#");
                    }
                    result = format.format(value);
                }
                break;
            case STRING:// String类型
                result = cell.getRichStringCellValue().toString();
                break;
            case BLANK:
                result = "";
                break;
            default:
                result = "";
                break;
            }
        }
        return result;
    }

}

class Company {
    private String name;
    private String description;
    private List<String> images = new ArrayList<>();
    private List<String> cers = new ArrayList<>();;
    private List<Product> products = new ArrayList<>();

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the images
     */
    public List<String> getImages() {
        return images;
    }

    /**
     * @param images
     *            the images to set
     */
    public void setImages(List<String> images) {
        this.images = images;
    }

    /**
     * @return the cers
     */
    public List<String> getCers() {
        return cers;
    }

    /**
     * @param cers
     *            the cers to set
     */
    public void setCers(List<String> cers) {
        this.cers = cers;
    }

    /**
     * @return the products
     */
    public List<Product> getProducts() {
        return products;
    }

    /**
     * @param products
     *            the products to set
     */
    public void setProducts(List<Product> products) {
        this.products = products;
    }
}

class Product {
    private String name;
    private Set<String> images = new HashSet<>();
    private String codes;
    private String bases;
    private String baseAddress;
    private List<String> baseImages = new ArrayList<>();

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the images
     */
    public Set<String> getImages() {
        return images;
    }

    /**
     * @param images
     *            the images to set
     */
    public void setImages(Set<String> images) {
        this.images = images;
    }

    /**
     * @return the codes
     */
    public String getCodes() {
        return codes;
    }

    /**
     * @param codes
     *            the codes to set
     */
    public void setCodes(String codes) {
        this.codes = codes;
    }

    /**
     * @return the bases
     */
    public String getBases() {
        return bases;
    }

    /**
     * @param bases
     *            the basess to set
     */
    public void setBases(String bases) {
        this.bases = bases;
    }

    /**
     * @return the baseImages
     */
    public List<String> getBaseImages() {
        return baseImages;
    }

    /**
     * @param baseImages
     *            the baseImages to set
     */
    public void setBaseImages(List<String> baseImages) {
        this.baseImages = baseImages;
    }

    /**
     * @return the baseAddress
     */
    public String getBaseAddress() {
        return baseAddress;
    }

    /**
     * @param baseAddress
     *            the baseAddress to set
     */
    public void setBaseAddress(String baseAddress) {
        this.baseAddress = baseAddress;
    }
}
