package cn.itechyou.blog.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import cn.itechyou.blog.entity.System;

public class Test {

	public static void main(String[] args) throws IOException {
		
		System system = new System();
		system.setId("1");
		system.setWebsite("www.baidu.com");
		system.setTitle("百度");
		system.setKeywords("百度一下");
		system.setDescribe("网罗天下dfsa忆");
		system.setIcp("京icp12321");
		system.setCopyright("北京小米科技有限责任公司");
		system.setAppid("");
		system.setUploaddir("uploads");
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("d:/system.properties")));
        oos.writeObject(system);
        java.lang.System.out.println("FlyPig 对象序列化成功！");
        oos.close();
	}
}
