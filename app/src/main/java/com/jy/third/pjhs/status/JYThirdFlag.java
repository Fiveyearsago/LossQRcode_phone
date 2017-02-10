package com.jy.third.pjhs.status;
/**
 * 与第三方对接的标识
 * @author xinrongVy
 * 上午10:57:39
 */
public final class JYThirdFlag {
	/** 登录验证 GlobalKeys 中的相关参数*/
	public static final String LOGIN="0100";
	/** 登录验证 成功*/
	public static final String LOGIN_SUCCESS="0101";
	/** 登录验证 用户名错误*/
	public static final String LOGIN_ERR_NAME="0102";
	/** 登录验证 密码错误*/
	public static final String LOGIN_ERR_PWD="0105";
	/** 登录验证 没有权限*/
	public static final String LOGIN_ERR_ACCESS="0106";
	/** 登录验证 密网络或XML格式异常*/
	public static final String LOGIN_ERR_NET="0199";
	
	/** 提货人 下载 待确认列表*/
	public static final String THR_LOAD_WAIT_CONFIRM="0200";
	/** 提货人 下载 待确认列表 成功*/
	public static final String THR_LOAD_WAIT_CONFIRM_SUCCESS="0210";
	/** 提货人 下载 待确认列表 该用户没有数据*/
	public static final String THR_LOAD_WAIT_CONFIRM_ERR="0220";
	
	/** 提货人 提交 收货零件的清单*/
	public static final String THR_UPLOAD_FIT_LIST="0300";
	/** 提货人 提交 收货零件的清单 成功*/
	public static final String THR_UPLOAD_FIT_LIST_SUCCESS="0310";
	/** 提货人 提交 收货零件的清单 数据格式不正确*/
	public static final String THR_UPLOAD_FIT_LIST_ERR="0320";
	
	
}
