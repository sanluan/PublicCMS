package com.publiccms.views.pojo;

import java.util.List;

import com.publiccms.entities.cms.CmsContentFile;
import com.publiccms.entities.cms.CmsContentRelated;
import com.publiccms.entities.cms.CmsTag;

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

	public List<CmsContentRelated> getContentRelateds() {
		return contentRelateds;
	}

	public void setContentRelateds(List<CmsContentRelated> contentRelateds) {
		this.contentRelateds = contentRelateds;
	}

	public List<CmsContentFile> getFiles() {
		return files;
	}

	public void setFiles(List<CmsContentFile> files) {
		this.files = files;
	}

	public List<ExtendData> getModelExtendDataList() {
		return modelExtendDataList;
	}

	public void setModelExtendDataList(List<ExtendData> modelExtendDataList) {
		this.modelExtendDataList = modelExtendDataList;
	}

	public List<ExtendData> getCategoryExtendDataList() {
		return categoryExtendDataList;
	}

	public void setCategoryExtendDataList(List<ExtendData> categoryExtendDataList) {
		this.categoryExtendDataList = categoryExtendDataList;
	}

	public List<CmsContentFile> getImages() {
		return images;
	}

	public void setImages(List<CmsContentFile> images) {
		this.images = images;
	}

	public List<CmsTag> getTags() {
		return tags;
	}

	public void setTags(List<CmsTag> tags) {
		this.tags = tags;
	}
}