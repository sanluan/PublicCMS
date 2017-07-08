package org.publiccms.views.pojo;

import java.util.List;

import org.publiccms.entities.cms.CmsContentFile;
import org.publiccms.entities.cms.CmsContentRelated;
import org.publiccms.entities.cms.CmsTag;

/**
 *
 * CmsContentParamters
 * 
 */
public class CmsContentParamters implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<CmsContentRelated> contentRelateds;
	private List<CmsTag> tags;
	private List<CmsContentFile> files;
	private List<CmsContentFile> images;
	private List<ExtendData> modelExtendDataList;
	private List<ExtendData> categoryExtendDataList;

	/**
	 * @return
	 */
	public List<CmsContentRelated> getContentRelateds() {
		return contentRelateds;
	}

	/**
	 * @param contentRelateds
	 */
	public void setContentRelateds(List<CmsContentRelated> contentRelateds) {
		this.contentRelateds = contentRelateds;
	}

	/**
	 * @return
	 */
	public List<CmsContentFile> getFiles() {
		return files;
	}

	/**
	 * @param files
	 */
	public void setFiles(List<CmsContentFile> files) {
		this.files = files;
	}

	/**
	 * @return
	 */
	public List<ExtendData> getModelExtendDataList() {
		return modelExtendDataList;
	}

	/**
	 * @param modelExtendDataList
	 */
	public void setModelExtendDataList(List<ExtendData> modelExtendDataList) {
		this.modelExtendDataList = modelExtendDataList;
	}

	/**
	 * @return
	 */
	public List<ExtendData> getCategoryExtendDataList() {
		return categoryExtendDataList;
	}

	/**
	 * @param categoryExtendDataList
	 */
	public void setCategoryExtendDataList(List<ExtendData> categoryExtendDataList) {
		this.categoryExtendDataList = categoryExtendDataList;
	}

	/**
	 * @return
	 */
	public List<CmsContentFile> getImages() {
		return images;
	}

	/**
	 * @param images
	 */
	public void setImages(List<CmsContentFile> images) {
		this.images = images;
	}

	/**
	 * @return
	 */
	public List<CmsTag> getTags() {
		return tags;
	}

	/**
	 * @param tags
	 */
	public void setTags(List<CmsTag> tags) {
		this.tags = tags;
	}
}