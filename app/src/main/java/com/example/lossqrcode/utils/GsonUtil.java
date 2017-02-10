package com.example.lossqrcode.utils;

import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.jy.aicloud.dto.JyAttachmentDTO;

public class GsonUtil {

	private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

	public static <T> String toJson(T t) {
		if (t != null)
			return gson.toJson(t);
		else
			return null;
	}
	
	public static <T> T toEntity(String json, Class<T> clazz){
		return gson.fromJson(json, clazz);
	}
	
	public static <T> List<T> toListEntity(String json, Class<T> clazz){
		return gson.fromJson(json, new com.google.gson.reflect.TypeToken<List<T>>(){}.getType());
	}
	
	public static <T> T toEntity(String json, TypeToken<T> typeToken){
		return gson.fromJson(json, typeToken.getType());
	}
	
	public static void main(String[] args) {
		
//		List<JyAttachmentDTO> list = new ArrayList<JyAttachmentDTO>();
//		
//		for (int i = 0; i < 3; i++) {
//			JyAttachmentDTO dto = new JyAttachmentDTO();
//			dto.setId(i + 1 + "");
//			dto.setExtName("JyAttachmentDTO" + 1);
//			System.out.println(toJson(dto));
//		}
//		
//		String json = "[{\"id\":\"1\",\"extName\":\"JyAttachmentDTO1\"},{\"id\":\"2\",\"extName\":\"JyAttachmentDTO1\"}]";
//		List<JyAttachmentDTO> listEntity = toListEntity(json, JyAttachmentDTO.class);
//		System.out.println(listEntity);
		
		System.out.println(GsonUtil.toJson(new Date()));
	}
	
	public final static Gson createGson(){
		GsonBuilder builder = new GsonBuilder();
		builder.setDateFormat("yyyy-MM-dd HH:mm:ss");
		Gson gson = builder.create();
		return gson ; 
	}

}
