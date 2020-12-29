package cn.itechyou.cms.entity;

import lombok.Data;

/**
 * 栏目扩展
 * @author 王俊南
 * Date: 2020-12-29
 */
@Data
public class CategoryWithBLOBs extends Category {
    private String mdContent;

    private String htmlContent;
}