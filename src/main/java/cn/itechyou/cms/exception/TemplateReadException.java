package cn.itechyou.cms.exception;

import cn.itechyou.cms.common.ExceptionEnum;

public class TemplateReadException extends CmsException {

    private static final long serialVersionUID = 3969404221975913175L;

    public TemplateReadException(String code, String message, String reason) {
        super(code, message, reason);
    }

    public TemplateReadException(ExceptionEnum exceptionEnum, String reason) {
        super(exceptionEnum.getCode(), exceptionEnum.getMessage(), reason);
    }
}
