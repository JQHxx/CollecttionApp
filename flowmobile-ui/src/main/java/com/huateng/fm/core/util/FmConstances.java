package com.huateng.fm.core.util;

public interface FmConstances {
	//严重错误异常处理类
	String PROPERTIES_uncaught_exception_handler_falatprocess="uncaught.exception.handler.falatprocess";
	//严重错误日志存储路径
	String PROPERTIES_uncaught_exception_handler_falatfilepath="uncaught.exception.handler.falatfilepath";
	//严重错误日志存储名称
	String PROPERTIES_uncaught_exception_handler_falatfilename="uncaught.exception.handler.falatfilename";
	
	//系统默认配置文件，加密存储
	String SYS_PROPERTIES_FILE = "flowmobile.properties";
	
	String DEVICE_versionName = "versionName";
	String DEVICE_versionCode = "versionCode";
	String DEVICE_OS_SDK = "OS_SDK";
	String DEVICE_OS_MODEL = "OS_MODEL";
	String DEVICE_OS_VERSION = "OS_VERSION";
	byte[] CRLF = new byte[] { 0x0D, 0x0A };
	
	final String LOGTYPE_FILE = "FILE";
	final String LOGTYPE_LOGCAT = "LOGCAT";
	final String LOGTYPE_ALL = "ALL";
	final String LOGTYPE_NONE = "NONE";
	
	final String CONTENT_DISPOSITION = "Content-Disposition";
	final String CONTENT_TYPE = "Content-type";
	final String CONTENT_TYPE_FORM = "Content-Type";
	final String APP_XML_TYPE = "application/xml";
	final String APP_JSON_TYPE = "application/json";
	final String APP_DATA_TYPE = "multipart/form-data";
	final String APP_DEF_TYPE ="application/plain";
	final String APP_FILE_TYPE="application/octet-stream";
	final int BUFFER_SIZE = 1024;
	
}
