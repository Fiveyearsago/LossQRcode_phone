package com.example.lossqrcode.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.VolleyLog;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.example.lossqrcode.MyApplication;
import com.example.lossqrcode.entity.DataDownloadEntity;
import com.example.lossqrcode.entity.RemnantGoodsEntity;
import com.example.lossqrcode.entity.User;
import com.example.lossqrcode.utils.Constants;
import com.example.lossqrcode.utils.GsonUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.jy.third.pjhs.dto.BaseJsonDTO;
import com.jy.third.pjhs.dto.DataDownloadReq;
import com.jy.third.pjhs.dto.NLBaseJson;
import com.jy.third.pjhs.dto.NLBaseResponse;
import com.jy.third.pjhs.dto.NLDataDownloadResp;
import com.jy.third.pjhs.dto.Request;
import com.jy.third.pjhs.dto.Response;
import com.jy.third.pjhs.dto.UploadScanDataReq;
import com.jy.third.pjhs.dto.fit.RemnantGood4AppDTO;
import com.jy.third.pjhs.dto.fit.ScanResultDTO;
import com.jy.third.pjhs.dto.user.User4App;
import com.jy.third.pjhs.entity.fit.RemnantGood4App;

import org.json.JSONException;
import org.json.JSONObject;

public class ApiService {

	private static final String TAG = "ApiService";

	/**
	 * 用户登录
	 * 
	 * @param qtDTO
	 * @param listener
	 * @param errorListener
	 * @return
	 */
	public static JsonRequest<Response<User4App>> login(User4App qtDTO,
			final Listener<Response<User4App>> listener,
			ErrorListener errorListener) {
		Gson gson = GsonUtil.createGson();
		Request<User4App> request = new Request<User4App>();
		request.setRequestType(Constants.REQUEST_LOGIN);
		request.setData(qtDTO);
		String qtData = gson.toJson(request);
		return new JsonRequest<Response<User4App>>(Method.POST, Constants.URL,
				qtData, listener, errorListener) {
			@Override
			protected com.android.volley.Response<Response<User4App>> parseNetworkResponse(
					NetworkResponse response) {
				// TODO Auto-generated method stub
				try {
					String json = new String(response.data, "gbk");
					Response<User4App> rs = new Response<User4App>().fromJson(
							json, new TypeToken<Response<User4App>>() {
							});
					return com.android.volley.Response.success(rs,
							HttpHeaderParser.parseCacheHeaders(response));
				} catch (UnsupportedEncodingException e) {
					return com.android.volley.Response.error(new ParseError(e));
				} catch (JsonSyntaxException e) {
					return com.android.volley.Response.error(new ParseError(e));
				}
			}

			@Override
			public void setTag(Object tag) {
				// TODO Auto-generated method stub
				super.setTag(tag);
				setTag(listener);
			}
		};

	}

	/**
	 * 下载损余物资
	 * 
	 * @param qtDTO
	 * @param listener
	 * @param errorListener
	 * @return
	 */
	public static JsonRequest<Response<RemnantGood4AppDTO<RemnantGoodsEntity>>> loadRemnantGoods(
			String userId,
			RemnantGood4AppDTO<RemnantGoodsEntity> qtDTO,
			final Listener<Response<RemnantGood4AppDTO<RemnantGoodsEntity>>> listener,
			ErrorListener errorListener) {
		Gson gson = GsonUtil.createGson();
		Request<RemnantGood4AppDTO<RemnantGoodsEntity>> request = new Request<RemnantGood4AppDTO<RemnantGoodsEntity>>();
		request.setRequestType(Constants.REQUEST_LOAD_REMNANT_GOODS);
		request.setUserId(userId);
		request.setData(qtDTO);
		String qtData = gson.toJson(request);
		Log.i(TAG, "loadRemnantGoods json = "+qtData);
		return new JsonRequest<Response<RemnantGood4AppDTO<RemnantGoodsEntity>>>(
				Method.POST, Constants.URL, qtData, listener, errorListener) {
			@Override
			protected com.android.volley.Response<Response<RemnantGood4AppDTO<RemnantGoodsEntity>>> parseNetworkResponse(
					NetworkResponse response) {
				// TODO Auto-generated method stub
				try {
					String json = new String(response.data, "gbk");
					Log.i(TAG, "loadRemnantGoods return json = "+json);
					Response<RemnantGood4AppDTO<RemnantGoodsEntity>> rs = new Response<RemnantGood4AppDTO<RemnantGoodsEntity>>()
							.fromJson(
									json,
									new TypeToken<Response<RemnantGood4AppDTO<RemnantGoodsEntity>>>() {
									});
					return com.android.volley.Response.success(rs,
							HttpHeaderParser.parseCacheHeaders(response));
				} catch (UnsupportedEncodingException e) {
					return com.android.volley.Response.error(new ParseError(e));
				} catch (JsonSyntaxException e) {
					return com.android.volley.Response.error(new ParseError(e));
				}
			}

			@Override
			public void setTag(Object tag) {
				// TODO Auto-generated method stub
				super.setTag(tag);
				setTag(listener);
			}
		};

	}

	/**
	 * 获取图片地址
	 */
	public static JsonRequest<NLBaseJson<NLBaseResponse>> getUrl(
			Context context,String id,String username,
			final Listener<NLBaseJson<NLBaseResponse>> listener,
			ErrorListener errorListener) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("flag", Constants.REQUEST_URL_DATA);
			JSONObject jsonObject1 = new JSONObject();
			jsonObject1.put("username", username);
			jsonObject1.put("remid", id);
			jsonObject.put("data", jsonObject1);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.i("json",jsonObject.toString());



//		Gson gson = GsonUtil.createGson();
//		NLBaseJson<DataDownloadReq> request = new NLBaseJson<DataDownloadReq>();
//		request.setFlag(Constants.REQUEST_URL_DATA);
//		request.setData(data);
//		String qtData = gson.toJson(request);
		return new JsonRequest<NLBaseJson<NLBaseResponse>>(
				Method.POST, Constants.URL, jsonObject.toString(), listener, errorListener) {
			@Override
			protected com.android.volley.Response<NLBaseJson<NLBaseResponse>> parseNetworkResponse(
					NetworkResponse response) {
				// TODO Auto-generated method stub
				try {
					String json = new String(response.data, "gbk");
					Log.i(TAG, "dataDownload return json = "+json);
					NLBaseJson<NLBaseResponse> rs = new NLBaseJson<NLBaseResponse>()
							.fromJson(
									json,
									new TypeToken<NLBaseJson<NLBaseResponse>>() {
									});
					Log.i(TAG, "dataDownload code = "+rs.getData().getCode());
					return com.android.volley.Response.success(rs,
							HttpHeaderParser.parseCacheHeaders(response));
				} catch (UnsupportedEncodingException e) {
					return com.android.volley.Response.error(new ParseError(e));
				} catch (JsonSyntaxException e) {
					return com.android.volley.Response.error(new ParseError(e));
				}
			}

			@Override
			public void setTag(Object tag) {
				super.setTag(tag);
				setTag(listener);
			}
		};

	}
	/**
	 * 上传数据
	 * 
	 * @param qtDTO
	 * @param listener
	 * @param errorListener
	 * @return
	 */
	public static JsonRequest<Response<RemnantGoodsEntity>> uploadScanData(
			String userId, RemnantGoodsEntity qtDTO,
			final Listener<Response<RemnantGoodsEntity>> listener,
			ErrorListener errorListener) {
		Gson gson = GsonUtil.createGson();
		Request<RemnantGoodsEntity> request = new Request<RemnantGoodsEntity>();
		request.setRequestType(Constants.REQUEST_UPLOAD_DATA);
		request.setUserId(userId);
		request.setData(qtDTO);
		final String qtData = gson.toJson(request);
		return new JsonRequest<Response<RemnantGoodsEntity>>(Method.POST,
				Constants.URL, qtData, listener, errorListener) {
			@Override
			protected com.android.volley.Response<Response<RemnantGoodsEntity>> parseNetworkResponse(
					NetworkResponse response) {
				try {
					String json = new String(response.data, "gbk");
					Response<RemnantGoodsEntity> rs = new Response<RemnantGoodsEntity>()
							.fromJson(
									json,
									new TypeToken<Response<RemnantGoodsEntity>>() {
									});
					return com.android.volley.Response.success(rs,
							HttpHeaderParser.parseCacheHeaders(response));
				} catch (UnsupportedEncodingException e) {
					return com.android.volley.Response.error(new ParseError(e));
				} catch (JsonSyntaxException e) {
					return com.android.volley.Response.error(new ParseError(e));
				}
			}

			@Override
			public void setTag(Object tag) {
				// TODO Auto-generated method stub
				super.setTag(tag);
				setTag(listener);
			}

			@Override
			public byte[] getBody() {
				// TODO Auto-generated method stub
				try {
					return qtData.getBytes("gbk");
				} catch (UnsupportedEncodingException uee) {
					VolleyLog
							.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
									qtData, "gbk");
					return null;
				}
			}

		};

	}
	
	/************************************************************************************/
	
	public static JsonRequest<NLBaseJson<NLBaseResponse>> nllogin(User user,
			final Listener<NLBaseJson<NLBaseResponse>> listener,
			ErrorListener errorListener) {
		Gson gson = GsonUtil.createGson();
		NLBaseJson<User> request = new NLBaseJson<User>();
		request.setFlag(Constants.REQUEST_LOGIN);
		request.setData(user);
		String qtData = gson.toJson(request);
		return new JsonRequest<NLBaseJson<NLBaseResponse>>(Method.POST, Constants.URL,
				qtData, listener, errorListener) {
			@Override
			protected com.android.volley.Response<NLBaseJson<NLBaseResponse>> parseNetworkResponse(
					NetworkResponse response) {
				// TODO Auto-generated method stub
				try {
					String json = new String(response.data, "gbk");
					Log.i(TAG, "nllogin return = "+json);
					NLBaseJson<NLBaseResponse> rs = new NLBaseJson<NLBaseResponse>().fromJson(
							json, new TypeToken<NLBaseJson<NLBaseResponse>>() {
							});
					Log.i(TAG, "nllogin code = "+rs.getData().getCode());
					return com.android.volley.Response.success(rs,
							HttpHeaderParser.parseCacheHeaders(response));
				} catch (UnsupportedEncodingException e) {
					return com.android.volley.Response.error(new ParseError(e));
				} catch (JsonSyntaxException e) {
					return com.android.volley.Response.error(new ParseError(e));
				}
			}

			@Override
			public void setTag(Object tag) {
				super.setTag(tag);
				setTag(listener);
			}
		};

	}
	public static JsonRequest<NLBaseJson<NLDataDownloadResp>> searchData(
			DataDownloadReq data,
			final Listener<NLBaseJson<NLDataDownloadResp>> listener,
			ErrorListener errorListener) {
		Gson gson = GsonUtil.createGson();
		NLBaseJson<DataDownloadReq> request = new NLBaseJson<DataDownloadReq>();
		request.setFlag(Constants.SEARCH_DATA);
		request.setData(data);
		String qtData = gson.toJson(request);
		JsonRequest<NLBaseJson<NLDataDownloadResp>> jsonJsonRequest;

		jsonJsonRequest=new JsonRequest<NLBaseJson<NLDataDownloadResp>>(
				Method.POST, Constants.URL, qtData, listener, errorListener) {
			@Override
			protected com.android.volley.Response<NLBaseJson<NLDataDownloadResp>> parseNetworkResponse(NetworkResponse response) {

				// TODO Auto-generated method stub
				try {
					String json = new String(response.data, "gbk");
					Log.i(TAG, "dataDownload return json = "+json);
					NLBaseJson<NLDataDownloadResp> rs = new NLBaseJson<NLDataDownloadResp>()
							.fromJson(
									json,
									new TypeToken<NLBaseJson<NLDataDownloadResp>>() {
									});
					Log.i(TAG, "dataDownload code = "+rs.getData().getCode()+"-----  listsize = "+rs.getData().getList().size());
					return com.android.volley.Response.success(rs,
							HttpHeaderParser.parseCacheHeaders(response));
				} catch (UnsupportedEncodingException e) {
					return com.android.volley.Response.error(new ParseError(e));
				} catch (JsonSyntaxException e) {
					return com.android.volley.Response.error(new ParseError(e));
				}


			}
			@Override
			public void setTag(Object tag) {
				super.setTag(tag);
				setTag(listener);
			}
		};
		jsonJsonRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
		return jsonJsonRequest;
	}
	public static JsonRequest<NLBaseJson<NLDataDownloadResp>> dataDownload(
			DataDownloadReq data,
			final Listener<NLBaseJson<NLDataDownloadResp>> listener,
			ErrorListener errorListener) {
		Gson gson = GsonUtil.createGson();
		NLBaseJson<DataDownloadReq> request = new NLBaseJson<DataDownloadReq>();
		request.setFlag(Constants.REQUEST_LOAD_REMNANT_GOODS);
		request.setData(data);
		String qtData = gson.toJson(request);
		JsonRequest<NLBaseJson<NLDataDownloadResp>> jsonJsonRequest;

		jsonJsonRequest=new JsonRequest<NLBaseJson<NLDataDownloadResp>>(
				Method.POST, Constants.URL, qtData, listener, errorListener) {
			@Override
			protected com.android.volley.Response<NLBaseJson<NLDataDownloadResp>> parseNetworkResponse(NetworkResponse response) {

					// TODO Auto-generated method stub
					try {
						String json = new String(response.data, "gbk");
						Log.i(TAG, "dataDownload return json = "+json);
						NLBaseJson<NLDataDownloadResp> rs = new NLBaseJson<NLDataDownloadResp>()
								.fromJson(
										json,
										new TypeToken<NLBaseJson<NLDataDownloadResp>>() {
										});
						Log.i(TAG, "dataDownload code = "+rs.getData().getCode()+"-----  listsize = "+rs.getData().getList().size());
						return com.android.volley.Response.success(rs,
								HttpHeaderParser.parseCacheHeaders(response));
					} catch (UnsupportedEncodingException e) {
						return com.android.volley.Response.error(new ParseError(e));
					} catch (JsonSyntaxException e) {
						return com.android.volley.Response.error(new ParseError(e));
					}


			}
			@Override
			public void setTag(Object tag) {
				super.setTag(tag);
				setTag(listener);
			}
		};
		jsonJsonRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
		return jsonJsonRequest;
	}
//		return new JsonRequest<NLBaseJson<NLDataDownloadResp>>(
//				Method.POST, Constants.URL, qtData, listener, errorListener) {
//			@Override
//			protected com.android.volley.Response<NLBaseJson<NLDataDownloadResp>> parseNetworkResponse(
//					NetworkResponse response) {
//				// TODO Auto-generated method stub
//				try {
//					String json = new String(response.data, "gbk");
//					Log.i(TAG, "dataDownload return json = "+json);
//					NLBaseJson<NLDataDownloadResp> rs = new NLBaseJson<NLDataDownloadResp>()
//							.fromJson(
//									json,
//									new TypeToken<NLBaseJson<NLDataDownloadResp>>() {
//									});
//					Log.i(TAG, "dataDownload code = "+rs.getData().getCode()+"-----  listsize = "+rs.getData().getList().size());
//					return com.android.volley.Response.success(rs,
//							HttpHeaderParser.parseCacheHeaders(response));
//				} catch (UnsupportedEncodingException e) {
//					return com.android.volley.Response.error(new ParseError(e));
//				} catch (JsonSyntaxException e) {
//					return com.android.volley.Response.error(new ParseError(e));
//				}
//			}
//
//			@Override
//			public void setTag(Object tag) {
//				super.setTag(tag);
//				setTag(listener);
//			}
//		};


	
	public static JsonRequest<NLBaseJson<NLBaseResponse>> nlUploadScanData(UploadScanDataReq updomain,
			final Listener<NLBaseJson<NLBaseResponse>> listener,
			ErrorListener errorListener) {
		Gson gson = GsonUtil.createGson();
		NLBaseJson<UploadScanDataReq> request = new NLBaseJson<UploadScanDataReq>();
		request.setFlag(Constants.REQUEST_UPLOAD_DATA);
		request.setData(updomain);
		final String qtData = gson.toJson(request);
		return new JsonRequest<NLBaseJson<NLBaseResponse>>(Method.POST,
				Constants.URL, qtData, listener, errorListener) {
			@Override
			protected com.android.volley.Response<NLBaseJson<NLBaseResponse>> parseNetworkResponse(
					NetworkResponse response) {
				try {
					String json = new String(response.data, "gbk");
					Log.i(TAG, "uploadscandata respone = "+json);
					NLBaseJson<NLBaseResponse> rs = new NLBaseJson<NLBaseResponse>()
							.fromJson(
									json,
									new TypeToken<NLBaseJson<NLBaseResponse>>() {
									});
					return com.android.volley.Response.success(rs,
							HttpHeaderParser.parseCacheHeaders(response));
				} catch (UnsupportedEncodingException e) {
					return com.android.volley.Response.error(new ParseError(e));
				} catch (JsonSyntaxException e) {
					return com.android.volley.Response.error(new ParseError(e));
				}
			}

			@Override
			public void setTag(Object tag) {
				// TODO Auto-generated method stub
				super.setTag(tag);
				setTag(listener);
			}

			@Override
			public byte[] getBody() {
				// TODO Auto-generated method stub
				try {
					return qtData.getBytes("gbk");
				} catch (UnsupportedEncodingException uee) {
					VolleyLog
							.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
									qtData, "gbk");
					return null;
				}
			}

		};

	}
	

}
