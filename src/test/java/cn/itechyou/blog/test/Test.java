package cn.itechyou.blog.test;

import java.io.IOException;


public class Test {

	public static void main(String[] args) throws IOException {
		String sb = "<link type=\"text/css\"rel=\"stylesheet\"href=\"{dreamer-cms:template /}css/base.css\"/>\r\n" + 
				"<link type=\"text/css\"rel=\"stylesheet\"href=\"{dreamer-cms:template /}css/home.css\"/>\r\n" + 
				"<script type=\"text/javascript\" src=\"{dreamer-cms:template /}js/common.js\"></script>\r\n" + 
				"<script type=\"text/javascript\" src=\"{dreamer-cms:template /}js/jquery-1.8.2.min.js\"></script>\r\n" + 
				"<script type=\"text/javascript\" src=\"{dreamer-cms:template /}js/jquery.superslide.js\"></script>\r\n" + 
				"<script type=\"text/javascript\" src=\"{dreamer-cms:template /}js/adver.js\"></script>";
		String replaceAll = sb.replaceAll("\\{dreamer-cms:template[ \\t]*[/]{1}\\}", "aaa");
		System.out.println(replaceAll);
        
        
	}
}
