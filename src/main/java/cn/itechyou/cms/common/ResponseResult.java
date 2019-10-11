package cn.itechyou.cms.common;

import java.io.Serializable;
import java.util.List;

import com.github.pagehelper.PageInfo;

/**
 * 数据传输对象DTO，用于将处理后的结果对象封装
 * 
 * @author scar
 *
 */
public class ResponseResult implements Serializable {

	private static final long serialVersionUID = -7183680631393026093L;
	
	public ResponseResult() {
	}
	
	public ResponseResult(boolean success, String state, Object model, String info) {
		this.success = success;
		this.state = state;
		this.data = model;
		this.info = info;
	}

	public ResponseResult(boolean success, String state, String info) {
		this.success = success;
		this.state = state;
		this.info = info;
	}

	// 返回状态（默认返回状态为成功，初始化实体后默认状态）
	private String state = StateCodeEnum.HTTP_SUCCESS.getCode();
	
	private boolean success;
	
	// 错误信息
	private String error;
	
	// info信息
	private String info;
	
	// 结果集，主要用于查询
	private List list;
	
	// 结果长度，主要用户分页查询总结果长度
	private Long totalCount;
	
	// 方法类型：增，删，改，查
	private String methodType;
	
	// 数据Id：增，删，改返回主键信息
	private String dataId;
	
	// 结果集，单条数据查询
	private Object data;
	
	//服务器错误状态
	private String serverError;
	
	public static class Factory{
		public static ResponseResult newInstance(boolean success, String status, Object model, String info) {
			if(model instanceof List){
				ResponseResult responseResult = new ResponseResult(success, status, info);
				responseResult.setList((List)model);
				return responseResult;
			}else{
				return new ResponseResult(success, status, model, info);
			}
		}
	}
	
	public String getServerError() {
		return serverError;
	}

	public void setServerError(String serverError) {
		this.serverError = serverError;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getError() {
		return error;
	}

	/**
	 * 设置错误代码，如果存在错误代码，则State状态为failure失败
	 */
	public void setError(String error) {
		this.state = StateCodeEnum.HTTP_ERROR.getCode();
		this.error = error;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}

	public Long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}

	public String getMethodType() {
		return methodType;
	}

	public void setMethodType(String methodType) {
		this.methodType = methodType;
	}

	public String getDataId() {
		return dataId;
	}

	public void setDataId(String dataId) {
		this.dataId = dataId;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
