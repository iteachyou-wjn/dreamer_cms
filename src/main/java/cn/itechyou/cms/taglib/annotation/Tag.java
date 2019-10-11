package cn.itechyou.cms.taglib.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Tag {
	String beginTag();
	
	String endTag();
	
	String regexp();
	
	Attribute[] attributes();
}
