package cn.itechyou.cms.entity;

import java.util.Date;

import lombok.Data;

/**
 * 附件
 * @author 王俊南
 * Date: 2020-12-29
 */
@Data
public class Attachment {
    private String id;

    private String code;

    private String filename;

    private Integer filesize;

    private String filetype;

    private String filepath;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;
}