package com.example.lossqrcode.utils;


public class ClaimFlag {
	public static final String APKNAME="ds.apk"; //应用程序名称
	
	public static int PAGE_SIZE = 9;   	//每页显示数量	任务
	public static  int PART_PAGE_SIZE = 20;   	//每页显示数量	 零件显示数量 大于此数字之后才显示下一页
	public static  int REPAIR_RATE = 10;   //默认工时费率
	public static  int GPS_MOVE_DISTANCE=20;		//GPS设置移动距离 （单位:米）
	public static  int GPS_FREQUENCY= 60*1000;	//GPS更新频率 （单位:毫秒） 
	public static  int ACC_DATA= 2*30*1000;	//间隔时间接收案件 （单位:毫秒）
	public static  int UPDATE_DATA= 24*3600*1000;	//间隔时间检查更新 （单位:毫秒）
	public static  int REMOVE_DATA= 1*3600*1000;	//间隔时间清除数据 （单位:毫秒）
	public static  int PIC_WIDTH= 640;//	480	//调用系统拍照后的宽度（单位:PX）
	public static  int PIC_HEIGHT=480;//	800	//调用系统拍照后的宽度（单位:PX）
	public static  int LIST_HEIGHT= 28;		//定损首页上每条listView的高度（单位:PX）
	public static  int CON_TIMEOUT= 20000;			//请求连接超时（单位:毫秒）
	public static  int SO_TIMEOUT= 20000;			//读取超时超时（单位:毫秒）
	public static  int REMOVE_DAY=30;				//清除多少天之前的数据（单位:天）
	public static  int REMIND_TIME=10*60*1000;				//预约扫描时间（单位:毫秒）
	

	public static final String REPAIR_TYPE_BANJ = "B00";   //钣金
	public static final String REPAIR_TYPE_YOUQ = "Q00";   //油漆
	public static final String REPAIR_TYPE_JIX = "X00";   //机修
	public static final String REPAIR_TYPE_DIANG = "D00";   //电工
	public static final String REPAIR_TYPE_CHAIZ = "C00";   //拆装
	public static final String REPAIR_TYPE_QIT = "QT";   //其它
	public static final String REPAIR_TYPE_FUL = "Z";   //辅料
	
	public static int GPS_FREQUENCY_EVERY_SECOND = 2000; 
	
	public final static String YES = "1" ; //是
	public final static String NO = "0" ;//否
	
	public final static String REQUEST_REBACK="002";
	
}
