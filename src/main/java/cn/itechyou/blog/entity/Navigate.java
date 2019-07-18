package cn.itechyou.blog.entity;

import java.util.Date;

public class Navigate {
    private String id;

    private String navName;

    private String position;

    private String code;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

    private String ext01;

    private String ext02;

    private String ext03;

    private String ext04;

    private String ext05;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getNavName() {
        return navName;
    }

    public void setNavName(String navName) {
        this.navName = navName == null ? null : navName.trim();
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position == null ? null : position.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
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
}