package cn.itechyou.cms.entity;

import java.util.Date;
import java.util.List;

public class Category {
    private String id;

    private String cnname;

    private String enname;

    private String code;
    
    private String catSeq;

    private String formId;

    private String imagePath;

    private String description;

    private String linkTarget;

    private Integer pageSize;

    private Integer catModel;

    private String visitUrl;

    private String coverTemp;

    private String listTemp;

    private String articleTemp;

    private String linkUrl;

    private String defaultEditor;

    private String parentId;
    
    private String parentName;

    private Integer isShow;

    private String level;
    
    private Integer sort;
    
    private Integer isInput;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

    private String ext01;

    private String ext02;

    private String ext03;

    private String ext04;

    private String ext05;

    private List<CategoryWithBLOBs> nodes;
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getCnname() {
        return cnname;
    }

    public void setCnname(String cnname) {
        this.cnname = cnname == null ? null : cnname.trim();
    }

    public String getEnname() {
        return enname;
    }

    public void setEnname(String enname) {
        this.enname = enname == null ? null : enname.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }
    
    public String getCatSeq() {
        return catSeq;
    }

    public void setCatSeq(String catSeq) {
        this.catSeq = catSeq == null ? null : catSeq.trim();
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId == null ? null : formId.trim();
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath == null ? null : imagePath.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getLinkTarget() {
        return linkTarget;
    }

    public void setLinkTarget(String linkTarget) {
        this.linkTarget = linkTarget == null ? null : linkTarget.trim();
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getCatModel() {
        return catModel;
    }

    public void setCatModel(Integer catModel) {
        this.catModel = catModel;
    }

    public String getVisitUrl() {
        return visitUrl;
    }

    public void setVisitUrl(String visitUrl) {
        this.visitUrl = visitUrl == null ? null : visitUrl.trim();
    }

    public String getCoverTemp() {
        return coverTemp;
    }

    public void setCoverTemp(String coverTemp) {
        this.coverTemp = coverTemp == null ? null : coverTemp.trim();
    }

    public String getListTemp() {
        return listTemp;
    }

    public void setListTemp(String listTemp) {
        this.listTemp = listTemp == null ? null : listTemp.trim();
    }

    public String getArticleTemp() {
        return articleTemp;
    }

    public void setArticleTemp(String articleTemp) {
        this.articleTemp = articleTemp == null ? null : articleTemp.trim();
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl == null ? null : linkUrl.trim();
    }

    public String getDefaultEditor() {
		return defaultEditor;
	}

	public void setDefaultEditor(String defaultEditor) {
		this.defaultEditor = defaultEditor;
	}

	public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId == null ? null : parentId.trim();
    }

    public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public Integer getIsShow() {
        return isShow;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level == null ? null : level.trim();
    }
    
    public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
	public Integer getIsInput() {
		return isInput;
	}

	public void setIsInput(Integer isInput) {
		this.isInput = isInput;
	}

	public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy == null ? null : updateBy.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getExt01() {
        return ext01;
    }

    public void setExt01(String ext01) {
        this.ext01 = ext01 == null ? null : ext01.trim();
    }

    public String getExt02() {
        return ext02;
    }

    public void setExt02(String ext02) {
        this.ext02 = ext02 == null ? null : ext02.trim();
    }

    public String getExt03() {
        return ext03;
    }

    public void setExt03(String ext03) {
        this.ext03 = ext03 == null ? null : ext03.trim();
    }

    public String getExt04() {
        return ext04;
    }

    public void setExt04(String ext04) {
        this.ext04 = ext04 == null ? null : ext04.trim();
    }

    public String getExt05() {
        return ext05;
    }

    public void setExt05(String ext05) {
        this.ext05 = ext05 == null ? null : ext05.trim();
    }

	public List<CategoryWithBLOBs> getNodes() {
		return nodes;
	}

	public void setNodes(List<CategoryWithBLOBs> nodes) {
		this.nodes = nodes;
	}
    
}