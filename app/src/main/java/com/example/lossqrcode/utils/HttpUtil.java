package com.example.lossqrcode.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EncodingUtils;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * 
 * 工具类
 * 
 */
public class HttpUtil {
	public static String queryStringForPost(String url) throws IOException {
		HttpPost request = HttpUtil.getHttpPost(url);
		String result = null;
		
		HttpResponse response = HttpUtil.getHttpResponse(request);
		if (response!=null &&  response.getStatusLine().getStatusCode() == 200) {
			result = EntityUtils.toString(response.getEntity());
		}
		
		return result;
	}
	public static HttpResponse getHttpResponse(HttpPost request) throws IOException {
		HttpResponse response=null;
			DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
//			请求超时 
			defaultHttpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, ClaimFlag.CON_TIMEOUT);
//			读取超时
			defaultHttpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, ClaimFlag.SO_TIMEOUT);
			response=defaultHttpClient.execute(request);
			
		return response;
	}
	public static DefaultHttpClient getHttpClient(){
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters,30000);
		HttpConnectionParams.setSoTimeout(httpParameters, 30000);
		return new DefaultHttpClient(httpParameters);
	}
	/**
	 * 检查网络是否可用
	 * 
	 * @return
	 */
	public static boolean isConnected(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (manager != null) {
			NetworkInfo network = manager.getActiveNetworkInfo();
			if (network != null && network.isConnectedOrConnecting()) {
				return true;
			}
		}
		return false;
	}


	private static HttpPost getHttpPost(String url) {
		HttpPost request = null;
		try {
			request = new HttpPost(url);
		} catch (RuntimeException e) {
			Log.e("HttpUtil","getHttpPost:" , e);
		}
		return request;
	}


	public static HttpEntity getEntity(String url) {
		HttpEntity entity = null;
		try {
			HttpPost request = getHttpPost(url);
			HttpResponse response = new DefaultHttpClient().execute(request);
			entity = response.getEntity();
		} catch (Exception e) {
			Log.e("HttpUtil","getEntity:" , e);
		}
		return entity;
	}
	//以URL get方式获取
		public static String httpGetStr(String url) {
			String result = "";
	        try {   
	            URL surl = new URL(url);
	            URLConnection con = surl.openConnection();
	            con.setConnectTimeout(20000);
	            InputStream is = con.getInputStream();
	            BufferedInputStream bis = new BufferedInputStream(is);
	            ByteArrayBuffer bab = new ByteArrayBuffer(32);
	            if(((HttpURLConnection)con).getResponseCode()==200){
	                int current = 0;   
	                while ( (current = bis.read()) != -1 ){   
	                    bab.append((byte)current);   
	                }   
	                result += EncodingUtils.getString(bab.toByteArray(), HTTP.UTF_8);
	                
	            }
	            bis.close();   
	            is.close();   
	        } catch (Exception e) {
	        }   
	        result= getAddressFromJson(result);
	        return result;
	    }  
		
		private static String getAddressFromJson(String x){
			String o="";
			try {
				JSONObject jsonObj=new JSONObject(x);
				JSONObject xaa = (JSONObject) jsonObj.get("result");
				o=xaa.get("formatted_address").toString();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				o="";
			}
			return o;
		}
		//以URL get方式获取
			public static int sendUrl(String url) {
				int result = 0;
		        try {   
		            URL surl = new URL(url);
		            URLConnection con = surl.openConnection();
		            InputStream is = con.getInputStream();
		            result = ((HttpURLConnection)con).getResponseCode();
		        } catch (Exception e) {
					Log.e("HttpUtil", "sendUrl",e);
		        }   
		        return result;
		    }  

}