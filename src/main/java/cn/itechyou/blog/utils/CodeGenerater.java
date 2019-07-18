package cn.itechyou.blog.utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by pc on 2017/4/18.
 */
public class CodeGenerater {
	private static CodeGenerater generater = null;

	private CodeGenerater() {
	}

	/**
	 * 取得PrimaryGenerater的单例实现
	 *
	 * @return
	 */
	public static CodeGenerater getInstance() {
		if (generater == null) {
			synchronized (CodeGenerater.class) {
				if (generater == null) {
					generater = new CodeGenerater();
				}
			}
		}
		return generater;
	}

	/**
	 * 生成下一个编号
	 * 
	 * @param sno    传最大的编号，才能返回累加后的!
	 * @param prefix 生成的编号前缀
	 * @return
	 */
	public synchronized String generater(String sno, String prefix) {
		String id = null;
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
		if (StringUtils.isEmpty(sno)) {
			sno = prefix + "2018000000";// 第一次初始化编号
		}
		DecimalFormat df = new DecimalFormat("000000");
		id = prefix + formatter.format(date) + df.format(1 + Integer.parseInt(sno.substring(7, sno.length())));

		return id;
	}

	public static void main(String[] args) {
		System.out.println(getInstance().generater("", "GHZ"));
	}
}