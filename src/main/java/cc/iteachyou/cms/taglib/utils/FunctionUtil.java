package cc.iteachyou.cms.taglib.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cc.iteachyou.cms.common.ExceptionEnum;
import cc.iteachyou.cms.exception.CmsException;
import cc.iteachyou.cms.exception.TemplateParseException;
import cc.iteachyou.cms.taglib.enums.FieldEnum;
import cc.iteachyou.cms.utils.StringUtil;

/**
 * 函数解析工具类
 * @author 王俊南
 * Date: 2021-1-16
 */
public class FunctionUtil {
	/**
	 * 解析函数并渲染数据
	 * @param source 源模版字符串
	 * @param regex 正则
	 * @param data 数据
	 * @return 处理完的数据
	 * @throws CmsException
	 */
	public static String replaceByFunction(String source, String regex, Object data) throws CmsException {
		String result = "";
		if(StringUtil.isBlank(data)) {
			return source.replaceAll(regex, "");
		}
		List<String> list = RegexUtil.parseAll(source, regex, 0);
		if(list != null && list.size() > 0) {
			for(int i = 0;i < list.size();i++) {
				String function = RegexUtil.parseFirst(list.get(i), FieldEnum.FUNCTION.getRegexp(), 1);
				String params = RegexUtil.parseFirst(list.get(i), FieldEnum.FUNCTION.getRegexp(), 2);
				if(function == null || params == null) {
					source = source.replaceFirst(regex, data.toString());
					continue;
				}
				
				String[] paramsArr = params.split(",");
				if("substring".equalsIgnoreCase(function)) {//是否为截取函数
					String stringVal = data.toString();
					if(paramsArr.length != 3) {
						throw new TemplateParseException(
								ExceptionEnum.TEMPLATE_PARSE_EXCEPTION.getCode(), 
								ExceptionEnum.TEMPLATE_PARSE_EXCEPTION.getMessage(), 
								"模版文件解析错误，请检查标签内的function参数是否正确。");
					}
					int start = Integer.parseInt(paramsArr[0]);
					int length = Integer.parseInt(paramsArr[1]);
					String addStr = paramsArr[2];
					if((addStr.startsWith("\"") || addStr.startsWith("\'")) && addStr.endsWith("\"") || addStr.endsWith("\'")){
						addStr = addStr.substring(1, addStr.length() - 1);
					}
					if(start >= length) {
						throw new TemplateParseException(
								ExceptionEnum.TEMPLATE_PARSE_EXCEPTION.getCode(), 
								ExceptionEnum.TEMPLATE_PARSE_EXCEPTION.getMessage(), 
								"模版文件解析错误，请检查标签内的function参数是否正确。");
					}
					if(stringVal.length() > length) {
						result = stringVal.substring(start, length);
					}else {
						result = stringVal;
					}
					result += addStr;
					source = source.replaceFirst(regex, result);
				}else if("format".equalsIgnoreCase(function)) {//是否为格式化函数，目前只支持Date类型数据
					if(data instanceof Date) {
						Date date = (Date) data;
						String formatPattern = paramsArr[0];
						if((formatPattern.startsWith("\"") || formatPattern.startsWith("\'")) && formatPattern.endsWith("\"") || formatPattern.endsWith("\'")){
							formatPattern = formatPattern.substring(1, formatPattern.length() - 1);
						}
						SimpleDateFormat sdf = new SimpleDateFormat(formatPattern);
						result = sdf.format(date);
						source = source.replaceFirst(regex, result);
					}else {
						throw new TemplateParseException(
								ExceptionEnum.TEMPLATE_PARSE_EXCEPTION.getCode(), 
								ExceptionEnum.TEMPLATE_PARSE_EXCEPTION.getMessage(), 
								"模版文件解析错误，format函数目前只支持日期类型数据进行格式化。");
					}
				}else if("steps".equalsIgnoreCase(function)) {//是否为格式化函数，目前只支持Date类型数据
					if(paramsArr.length != 2) {
						throw new TemplateParseException(
								ExceptionEnum.TEMPLATE_PARSE_EXCEPTION.getCode(), 
								ExceptionEnum.TEMPLATE_PARSE_EXCEPTION.getMessage(), 
								"模版文件解析错误，请检查标签内的function参数是否正确。");
					}
					if(data instanceof Integer) {
						Integer index = (Integer) data;
						index -= 1;
						int start = Integer.parseInt(paramsArr[0]);
						int step = Integer.parseInt(paramsArr[1]);
						result = (index * step) + start + "";
						
						source = source.replaceFirst(regex, result);
					}else {
						throw new TemplateParseException(
								ExceptionEnum.TEMPLATE_PARSE_EXCEPTION.getCode(), 
								ExceptionEnum.TEMPLATE_PARSE_EXCEPTION.getMessage(), 
								"模版文件解析错误，format函数目前只支持日期类型数据进行格式化。");
					}
				}
			}
		}else {
			return source.replaceAll(regex, data.toString());
		}
		return source;
	}
	
	/**
	 * 解析函数并渲染数据
	 * @param functionStr 函数版字符串
	 * @param data 数据
	 * @return 处理完的数据
	 * @throws CmsException
	 */
	public static String replaceByFunction(String functionStr, Object data) throws CmsException {
		String result = "";
		if(StringUtil.isBlank(data)) {
			return "";
		}
		
		String function = RegexUtil.parseFirst(functionStr, FieldEnum.FUNCTION_ARTICLE.getRegexp(), 2);
		String params = RegexUtil.parseFirst(functionStr, FieldEnum.FUNCTION_ARTICLE.getRegexp(), 3);
		
		if(function == null || params == null) {
			return data.toString();
		}
		
		String[] paramsArr = params.split(",");
		if("substring".equalsIgnoreCase(function)) {//是否为截取函数
			String stringVal = data.toString();
			if(paramsArr.length != 3) {
				throw new TemplateParseException(
						ExceptionEnum.TEMPLATE_PARSE_EXCEPTION.getCode(), 
						ExceptionEnum.TEMPLATE_PARSE_EXCEPTION.getMessage(), 
						"模版文件解析错误，请检查标签内的function参数是否正确。");
			}
			int start = Integer.parseInt(paramsArr[0]);
			int length = Integer.parseInt(paramsArr[1]);
			String addStr = paramsArr[2];
			if((addStr.startsWith("\"") || addStr.startsWith("\'")) && addStr.endsWith("\"") || addStr.endsWith("\'")){
				addStr = addStr.substring(1, addStr.length() - 1);
			}
			if(start >= length) {
				throw new TemplateParseException(
						ExceptionEnum.TEMPLATE_PARSE_EXCEPTION.getCode(), 
						ExceptionEnum.TEMPLATE_PARSE_EXCEPTION.getMessage(), 
						"模版文件解析错误，请检查标签内的function参数是否正确。");
			}
			if(stringVal.length() > length) {
				result = stringVal.substring(start, length);
				return result;
			}
			result = stringVal + addStr;
		}else if("format".equalsIgnoreCase(function)) {//是否为格式化函数，目前只支持Date类型数据
			if(data instanceof Date) {
				Date date = (Date) data;
				String formatPattern = paramsArr[0];
				if((formatPattern.startsWith("\"") || formatPattern.startsWith("\'")) && formatPattern.endsWith("\"") || formatPattern.endsWith("\'")){
					formatPattern = formatPattern.substring(1, formatPattern.length() - 1);
				}
				SimpleDateFormat sdf = new SimpleDateFormat(formatPattern);
				result = sdf.format(date);
			}else {
				throw new TemplateParseException(
						ExceptionEnum.TEMPLATE_PARSE_EXCEPTION.getCode(), 
						ExceptionEnum.TEMPLATE_PARSE_EXCEPTION.getMessage(), 
						"模版文件解析错误，format函数目前只支持日期类型数据进行格式化。");
			}
		}
		return result;
	}
}
