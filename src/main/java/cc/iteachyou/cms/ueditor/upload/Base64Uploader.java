package cc.iteachyou.cms.ueditor.upload;

import java.util.Map;

import org.apache.commons.codec.binary.Base64;

import cc.iteachyou.cms.common.Constant;
import cc.iteachyou.cms.ueditor.PathFormat;
import cc.iteachyou.cms.ueditor.define.AppInfo;
import cc.iteachyou.cms.ueditor.define.BaseState;
import cc.iteachyou.cms.ueditor.define.FileType;
import cc.iteachyou.cms.ueditor.define.State;

public final class Base64Uploader {

	public static State save(String content, Map<String, Object> conf) {
		byte[] data = decode(content);

		long maxSize = ((Long) conf.get("maxSize")).longValue();

		if (!validSize(data, maxSize)) {
			return new BaseState(false, AppInfo.MAX_SIZE);
		}

		String suffix = FileType.getSuffix("JPG");

		String savePath = PathFormat.parse((String) conf.get("savePath"),
				(String) conf.get("filename"));
		
		savePath = savePath + suffix;
		String physicalPath = (String) conf.get("rootPath") + savePath;

		State storageState = StorageManager.saveBinaryFile(data, physicalPath);

		if (storageState.isSuccess()) {
			storageState.putInfo("url", "/" + Constant.UPLOAD_PREFIX + PathFormat.format(savePath));
			storageState.putInfo("type", suffix);
			storageState.putInfo("original", "");
		}

		return storageState;
	}

	private static byte[] decode(String content) {
		return Base64.decodeBase64(content);
	}

	private static boolean validSize(byte[] data, long length) {
		return data.length <= length;
	}
	
}