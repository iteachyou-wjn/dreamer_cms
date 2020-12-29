package cn.itechyou.cms.entity;

import lombok.Data;

/**
 * 页面片段扩展
 * @author 王俊南
 * Date: 2020-12-29
 */
@Data
public class PagesWithBLOBs extends Pages {
    private String mdContent;

    private String htmlContent;
}