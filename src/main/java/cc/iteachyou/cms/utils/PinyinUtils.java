package cc.iteachyou.cms.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;

public class PinyinUtils {
	/**
	 * 获取字符串拼音的第一个字母
	 * 
	 * @param chinese
	 * @return
	 */
	public static String toFirstChar(String chinese) {
		String pinyinStr = "";
		char[] newChar = chinese.toCharArray(); // 转为单个字符
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		defaultFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
		try {
			for (int i = 0; i < newChar.length; i++) {
				if (newChar[i] > 128) {
					pinyinStr += PinyinHelper.toHanyuPinyinStringArray(newChar[i], defaultFormat)[0].charAt(0);
				} else {
					pinyinStr += newChar[i];
				}
			}
		} catch (Exception e) {
			pinyinStr = "#";
		}
		return pinyinStr;
	}

	/**
	 * 汉字转为拼音
	 * 
	 * @param chinese
	 * @return
	 */
	public static String toPinyin(String chinese) {
		String pinyinStr = "";
		char[] newChar = chinese.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		defaultFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
		try {
			for (int i = 0; i < newChar.length; i++) {
				if (newChar[i] > 128) {
					pinyinStr += PinyinHelper.toHanyuPinyinStringArray(newChar[i], defaultFormat)[0];
				} else {
					pinyinStr += newChar[i];
				}
			}
		} catch (Exception e) {
			pinyinStr = "#";
		}
		return pinyinStr;
	}
}
