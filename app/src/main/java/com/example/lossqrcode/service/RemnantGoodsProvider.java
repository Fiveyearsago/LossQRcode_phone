package com.example.lossqrcode.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

import com.example.lossqrcode.entity.DataDownloadEntity;
import com.example.lossqrcode.entity.PicAddress;
import com.example.lossqrcode.entity.RemnantGoodsEntity;
import com.example.lossqrcode.entity.SearchCondition;
import com.example.lossqrcode.utils.Constants;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.DbModelSelector;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.db.table.DbModel;
import com.lidroid.xutils.exception.DbException;

import android.R.bool;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;

public class RemnantGoodsProvider {
	private static RemnantGoodsProvider instance = null;
	private DbUtils db;
	private Context context;

	/**
	 * 未提货
	 */
	private static final String STATE_TO_TAKE_GOODS = "01";

	/**
	 * 已扫描
	 */
	private static final String STATE_HAVE_SCANNED = "02";
	/**
	 * 已提货
	 */
	private static final String STATE_ALREADY_TAKE_GOODS = "03";

	private static final String TAG = "RemnantGoodsProvider";

	public synchronized static RemnantGoodsProvider getInstance(Context ctx) {
		if (null == instance) {
			instance = new RemnantGoodsProvider(ctx);
		}
		return instance;
	}

	private String getUsername() {
		return context.getSharedPreferences("sp", Activity.MODE_PRIVATE)
				.getString("username", "");
	}

	public RemnantGoodsProvider(Context ctx) {
		this.context = ctx;
		db = DbUtils.create(ctx);
	}

	/*
	 * public void saveRemnantGoods(List<RemnantGoodsEntity> rgEntityList) { try
	 * { if (rgEntityList != null && rgEntityList.size() != 0) { for (int i = 0;
	 * i < rgEntityList.size(); i++) { RemnantGoodsEntity rgEntity =
	 * rgEntityList.get(i); rgEntity.setUsername(getUsername()); Calendar
	 * calendar=Calendar.getInstance(); Random random=new Random();
	 * 
	 * int month=random.nextInt(12); if(month==0){ month++; }
	 * calendar.set(Calendar.MONTH, month); int day=random.nextInt(30);
	 * if(day==0){ day++; } calendar.set(Calendar.DAY_OF_MONTH, day);
	 * 
	 * calendar.set(Calendar.HOUR_OF_DAY, 0); calendar.set(Calendar.MINUTE, 0);
	 * calendar.set(Calendar.SECOND, 0);
	 * 
	 * rgEntity.setCreateDate(calendar.getTime());
	 * 
	 * List<RemnantGoodsEntity> rgList = db.findAll(Selector
	 * .from(RemnantGoodsEntity.class) .where("username", "=", getUsername())
	 * .and("id", "=", rgEntity.getId())); if (rgList == null || rgList.size()
	 * == 0) { db.save(rgEntity); } else { String newState =
	 * rgEntity.getState(); if (!TextUtils.isEmpty(newState)) { if
	 * ("03".equals(newState)) { db.save(rgEntity); } } }
	 * 
	 * // RemnantGoodsEntity // rg=db.findById(RemnantGoodsEntity.class, //
	 * rgEntity.getId());
	 * 
	 * if(rg==null){ db.save(rgEntity); }else{ String
	 * newState=rgEntity.getState(); if(!TextUtils.isEmpty(newState)){
	 * if("03".equals(newState)){ db.save(rgEntity); } } }
	 * 
	 * } }
	 * 
	 * } catch (DbException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); }
	 * 
	 * }
	 */

	public void saveRemnantGoods(List<RemnantGoodsEntity> rgEntityList) {
		try {
			if (rgEntityList != null && rgEntityList.size() != 0) {
				for (int i = 0; i < rgEntityList.size(); i++) {
					RemnantGoodsEntity rgEntity = rgEntityList.get(i);
					rgEntity.setUsername(getUsername());
					Calendar calendar = Calendar.getInstance();

					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

					Date date = new Date();
					String str1 = format.format(date);
					Date parse = null;
					try {
						parse = format.parse(str1);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					rgEntity.setCreateDate(parse);

					List<RemnantGoodsEntity> rgList = db.findAll(Selector
							.from(RemnantGoodsEntity.class)
							.where("username", "=", getUsername())
							.and("id", "=", rgEntity.getId()));
					if (rgList == null || rgList.size() == 0) {
						db.save(rgEntity);
					} else {
						String newState = rgEntity.getState();
						if (!TextUtils.isEmpty(newState)) {
							if ("03".equals(newState)) {
								db.save(rgEntity);
							}
						}
					}

					// RemnantGoodsEntity
					// rg=db.findById(RemnantGoodsEntity.class,
					// rgEntity.getId());
					/*
					 * if(rg==null){ db.save(rgEntity); }else{ String
					 * newState=rgEntity.getState();
					 * if(!TextUtils.isEmpty(newState)){
					 * if("03".equals(newState)){ db.save(rgEntity); } } }
					 */
				}
			}

		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void saveRemnantGoods1(List<DataDownloadEntity> rgEntityList) {
		List<DataDownloadEntity> listWSH = getGoodsNotDelivery();
		List<DataDownloadEntity> listWZD = getHaveMissedGoods();
		List<DataDownloadEntity> listYSM = getHaveScannedGoods();
		try {
			db.deleteAll(listWSH);// 删除未收货数据
			db.deleteAll(listWZD);// 删除未找到数据
			db.deleteAll(listYSM);// 删除已扫描数据
			List<String> listSStrings = getHaveScannedIDS();// 已扫描
			List<String> listWStrings = getHaveMissedIDS();
			// 拿已扫描的配件和刚下载数据比较 若存在则把这条存在的数据在待收货中删除 若不存在则把这条存在的数据在已扫描中删除
			for (int j = 0; j < listSStrings.size(); j++) {
				if (rgEntityList != null && rgEntityList.size() != 0) {
					boolean flag = false;
					for (int i = 0; i < rgEntityList.size(); i++) {
						DataDownloadEntity rgEntity = rgEntityList.get(i);
						if (rgEntity.getId().equals(listSStrings.get(j))) {
							flag = true;// 找到了
							rgEntityList.remove(i);
							break;
						}
						// 找不到 则在已扫描中删除
						if (!flag) {
							listYSM.remove(j);
						}
					}
				}

			}
			// 拿未找到的配件和刚下载数据比较 若存在则把这条存在的数据在待收货中删除 若不存在则把这条存在的数据在未找到中删除
			for (int j = 0; j < listWStrings.size(); j++) {
				if (rgEntityList != null && rgEntityList.size() != 0) {
					boolean flag = false;
					for (int i = 0; i < rgEntityList.size(); i++) {
						DataDownloadEntity rgEntity = rgEntityList.get(i);
						if (rgEntity.getId().equals(listWStrings.get(j))) {
							flag = true;// 找到了
							rgEntityList.remove(i);
							break;
						}
						// 找不到 则在未找到中删除
						if (!flag) {
							listWZD.remove(j);
						}
					}
				}

			}
			if (rgEntityList != null && rgEntityList.size() != 0) {
				for (int i = 0; i < rgEntityList.size(); i++) {
					DataDownloadEntity rgEntity = rgEntityList.get(i);
					rgEntity.setUsername(getUsername());
					Calendar calendar = Calendar.getInstance();
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					Date date = new Date();
					String str1 = format.format(date);
					Date parse = null;
					try {
						parse = format.parse(str1);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					rgEntity.setCreateDate(parse);
					db.save(rgEntity);
				}
			}
			db.saveAll(listWZD);
			db.saveAll(listYSM);
		} catch (DbException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	/**
	 * 获取延迟收货零件数量
	 *
	 * @return
	 */
	public long getDelayCount() {
		long count = 0;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		Date date = new Date();
		String str1 = format.format(date);
		System.out.println(str1);
		Date parse = null;
		try {
			parse = format.parse(str1);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(parse.getTime());
		try {
			count = db.count(Selector
					.from(RemnantGoodsEntity.class)
					.where("username", "=", getUsername())
					.and("createDate", "<", parse)
					.and(WhereBuilder.b("state", "=", STATE_TO_TAKE_GOODS).or(
							"state", " is ", null)));
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * try { Cursor cursor=db.execQuery(
		 * "select * from com_example_lossqrcode_entity_RemnantGoodsEntity where createDate=1423670400000"
		 * ); count=cursor.getCount();
		 * System.out.println("cursor:"+cursor.getCount()); } catch (DbException
		 * e) { // TODO Auto-generated catch block e.printStackTrace(); }
		 */
		return count;
	}

	/**
	 * 获取已提货数据
	 *
	 * @return
	 */
	public List<RemnantGoodsEntity> getRemnantGoodsHaveTaken() {
		List<RemnantGoodsEntity> rgList = null;
		try {
			rgList = db.findAll(Selector.from(RemnantGoodsEntity.class)
					.where("state", "=", STATE_ALREADY_TAKE_GOODS)
					.and("username", "=", getUsername()));
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rgList;
	}

	/**
	 * 获取未提货数据
	 *
	 * @return
	 */
	public LinkedHashMap<String, List<RemnantGoodsEntity>> getRemnantGoodsToTake() {
		LinkedHashMap<String, List<RemnantGoodsEntity>> map = new LinkedHashMap<String, List<RemnantGoodsEntity>>();
		try {
			// db.findAll(Selector.from(RemnantGoodsEntity.class).where("username",
			// "=", getUsername())
			// .and(WhereBuilder.b("state", "=", "01").or("state",
			// "is", null)).groupBy(""));

			List<DbModel> usernameList = db.findDbModelAll(DbModelSelector
					.from(RemnantGoodsEntity.class)
					.where("username", "=", getUsername())
					.and(WhereBuilder.b("state", "=", "01").or("state", "is",
							null)).groupBy("goodNo").orderBy("goodNo", true));
			if (usernameList != null && usernameList.size() > 0) {
				for (int i = 0; i < usernameList.size(); i++) {
					String goodNo = usernameList.get(i).getString("goodNo");
					List<RemnantGoodsEntity> subList = db.findAll(Selector
							.from(RemnantGoodsEntity.class)
							.where("username", "=", getUsername())
							.and(WhereBuilder.b("state", "=", "01").or("state",
									"is", null)).and("goodNo", "=", goodNo)
							.orderBy("appNo", true));
					map.put(goodNo, subList);
				}
			}

		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 获取未找到零件ID集合
	 *
	 */
	public List<String> getHaveMissedIDS() {
		List<String> listStrings = new ArrayList<String>();
		List<DataDownloadEntity> rgList = null;
		try {
			rgList = db.findAll(Selector.from(DataDownloadEntity.class)
					.where("state", "=", Constants.STATE_ALREADY_MISSED)
					.and("username", "=", getUsername()).orderBy("id", true));
		} catch (DbException e) {
			e.printStackTrace();
		}
		if (rgList != null && rgList.size() > 0) {
			for (int i = 0; i < rgList.size(); i++) {
				listStrings.add(rgList.get(i).getId());
			}
		}
		return listStrings;
	}

	/**
	 * 获取未找到零件集合
	 *
	 */
	public List<DataDownloadEntity> getHaveMissedGoods() {
		List<DataDownloadEntity> rgList = null;
		try {
			rgList = db.findAll(Selector.from(DataDownloadEntity.class)
					.where("state", "=", Constants.STATE_ALREADY_MISSED)
					.and("username", "=", getUsername()).orderBy("id", true));
//			rgList = db.findAll(Selector.from(DataDownloadEntity.class)
//					.where("state", "=", Constants.STATE_ALREADY_MISSED)
//					.and("username", "=", getUsername()));
		} catch (DbException e) {
			e.printStackTrace();
		}
		return rgList;
	}

	/**
	 * 获取未找到零件
	 *
	 * @return
	 */
	public LinkedHashMap<String, List<RemnantGoodsEntity>> getRemnantGoodsHaveMissed() {
		LinkedHashMap<String, List<RemnantGoodsEntity>> map = new LinkedHashMap<String, List<RemnantGoodsEntity>>();
		try {
			List<DbModel> usernameList = db.findDbModelAll(DbModelSelector
					.from(RemnantGoodsEntity.class)
					.where("username", "=", getUsername())
					.and("state", "=", Constants.STATE_ALREADY_MISSED)
					.groupBy("goodNo").orderBy("goodNo", true));
			if (usernameList != null && usernameList.size() > 0) {
				for (int i = 0; i < usernameList.size(); i++) {
					String goodNo = usernameList.get(i).getString("goodNo");
					System.out.println(goodNo);
					List<RemnantGoodsEntity> subList = db.findAll(Selector
							.from(RemnantGoodsEntity.class)
							.where("username", "=", getUsername())
							.and("state", "=", Constants.STATE_ALREADY_MISSED)
							.and("goodNo", "=", goodNo).orderBy("appNo", true));
					System.out.println("goodno:" + subList.size());
					map.put(goodNo, subList);
				}
			}
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 分页加载未提货数据
	 *
	 * @param pageSize
	 * @param pageIndex
	 * @return
	 */
	public List<RemnantGoodsEntity> getRemnantGoodsToTake(int pageSize,
			int pageIndex) {
		List<RemnantGoodsEntity> rgList = null;
		try {
			rgList = db.findAll(Selector
					.from(RemnantGoodsEntity.class)
					.where("username", "=", getUsername())
					.and(WhereBuilder.b("state", "=", "01").or("state", "is",
							null)).limit(pageSize).offset(pageSize * pageIndex)
					.orderBy("createDate", false));
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rgList;
	}

	/**
	 * 根据条件查询未提货数据
	 *
	 * @param condition
	 * @return
	 */
	public LinkedHashMap<String, List<RemnantGoodsEntity>> getRemnantGoodsToTake(
			SearchCondition condition) {
		List<RemnantGoodsEntity> rgList = null;
		LinkedHashMap<String, List<RemnantGoodsEntity>> map = new LinkedHashMap<String, List<RemnantGoodsEntity>>();
		try {
			rgList = db.findAll(Selector
					.from(RemnantGoodsEntity.class)
					.where("username", "=", getUsername())
					.and("bah",
							"like",
							"%"
									+ (condition.reportNo == null ? ""
											: condition.reportNo) + "%")
					.and("cxmc",
							"like",
							"%"
									+ (condition.vehicleModel == null ? ""
											: condition.vehicleModel) + "%")
					.and("ljmc",
							"like",
							"%"
									+ (condition.partName == null ? ""
											: condition.partName) + "%")
					.and("createDate", "<=", condition.endDate)
					.and("createDate", ">=", condition.startDate)
					.and(WhereBuilder.b("state", "=", STATE_TO_TAKE_GOODS).or(
							"state", "is", null)));
			for (int i = 0; i < rgList.size(); i++) {
				map.put(rgList.get(i).getGoodNo(), null);
				for (String key : map.keySet()) {
					List<RemnantGoodsEntity> subList = new ArrayList<RemnantGoodsEntity>();
					for (int j = 0; j < rgList.size(); j++) {
						if (key.equals(rgList.get(j).getGoodNo())) {
							subList.add(rgList.get(j));
						}
					}
					map.put(key, subList);
				}
			}

		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 分页加载已提货数据
	 *
	 * @param pageSize
	 * @param pageIndex
	 * @return
	 */
	public List<RemnantGoodsEntity> getRemnantGoodsHaveTaken(int pageSize,
			int pageIndex) {
		List<RemnantGoodsEntity> rgList = null;
		try {
			rgList = db.findAll(Selector.from(RemnantGoodsEntity.class)
					.where("state", "=", STATE_ALREADY_TAKE_GOODS)
					.limit(pageSize).offset(pageSize * pageIndex)
					.and("username", "=", getUsername()));
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rgList;
	}

	/**
	 * 根据条件查询已提货数据
	 *
	 * @param condition
	 * @return
	 */
	public List<RemnantGoodsEntity> getRemnantGoodsHaveTaken(
			SearchCondition condition) {
		List<RemnantGoodsEntity> rgList = null;
		try {
			rgList = db.findAll(Selector
					.from(RemnantGoodsEntity.class)
					.where("state", "=", STATE_ALREADY_TAKE_GOODS)
					.and("bah",
							"like",
							"%"
									+ (condition.reportNo == null ? ""
											: condition.reportNo) + "%")
					.and("cxmc",
							"like",
							"%"
									+ (condition.vehicleModel == null ? ""
											: condition.vehicleModel) + "%")
					.and("ljmc",
							"like",
							"%"
									+ (condition.partName == null ? ""
											: condition.partName) + "%")
					.and("scanDate", "<=", condition.endDate)
					.and("scanDate", ">=", condition.startDate)
					.and("username", "=", getUsername()));
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rgList;
	}

	/**
	 * 标记为已扫描零件
	 *
	 * @param rgEntity
	 */
	public void updateHaveScannedPart(RemnantGoodsEntity rgEntity) {
		if (rgEntity != null) {
			try {
				db.update(rgEntity, WhereBuilder.b("id", "=", rgEntity.getId()));
			} catch (DbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/**
	 * 标记为待收货零件
	 *
	 * @param rgEntity
	 */
	public void updateWaitReceivePart(RemnantGoodsEntity rgEntity) {
		if (rgEntity != null) {
			try {
				db.update(rgEntity, WhereBuilder.b("id", "=", rgEntity.getId()));
			} catch (DbException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 标记为已丢失零件
	 *
	 * @param rgEntity
	 *
	 */
	public void updateHaveMissedPart(RemnantGoodsEntity rgEntity) {
		if (rgEntity != null) {
			try {
				db.update(rgEntity, WhereBuilder.b("id", "=", rgEntity.getId()));
			} catch (DbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/**
	 * 判断已扫描零件是否存在待确认零件库中
	 *
	 * @param rgEntity
	 * @return
	 */
	public List<RemnantGoodsEntity> isExistedInDB(RemnantGoodsEntity rgEntity) {
		List<RemnantGoodsEntity> rgList = null;
		if (rgEntity != null) {
			String appNo = rgEntity.getAppNo();
			try {
				rgList = db.findAll(Selector.from(RemnantGoodsEntity.class)
						.where("appNo", "=", appNo)
						.and("username", "=", getUsername()));
			} catch (DbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return rgList;
	}

	/**
	 * 判断已扫描零件是否存在已扫描零件库中
	 *
	 * @param rgEntity
	 * @return
	 */
	public List<RemnantGoodsEntity> isExistedInHaveScanned(
			RemnantGoodsEntity rgEntity) {
		List<RemnantGoodsEntity> rgList = null;
		if (rgEntity != null) {
			String appNo = rgEntity.getAppNo();
			try {
				rgList = db.findAll(Selector.from(RemnantGoodsEntity.class)
						.where("appNo", "=", appNo).and("state", "=", "02")
						.and("username", "=", getUsername()));
			} catch (DbException e) {
				e.printStackTrace();
			}
		}
		return rgList;
	}

	/**
	 * 获取所有已扫描零件
	 *
	 * @return
	 */
	public List<RemnantGoodsEntity> getAllHaveScannedPart() {
		List<RemnantGoodsEntity> rgList = null;
		try {
			rgList = db.findAll(Selector.from(RemnantGoodsEntity.class)
					.where("state", "=", STATE_HAVE_SCANNED)
					.and("username", "=", getUsername())
					.orderBy("scanDate", true));
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rgList;
	}

	/**
	 * 将已扫描零件标记为已收货零件
	 *
	 * @param rgList
	 */
	public void updateToAlreadyToken(List<RemnantGoodsEntity> rgList) {
		if (rgList != null) {
			for (int i = 0; i < rgList.size(); i++) {
				rgList.get(i).setState("03");
			}
			try {
				for (int i = 0; i < rgList.size(); i++) {
					db.update(rgList.get(i));
				}
			} catch (DbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/**
	 * 获取未找到的零件数量
	 *
	 * @return
	 */
	public long getRemnantGoodsHaveMissedCount() {
		long count = 0;
		try {
			count = db.count(Selector.from(DataDownloadEntity.class)
					.where("username", "=", getUsername())
					.and("state", "=", Constants.STATE_ALREADY_MISSED));
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
	}

	/**
	 * 获取未收货的零件数量
	 *
	 * @return
	 */
	public long getRemnantGoodsToTakeCount() {
		long count = 0;
		try {
			count = db.count(Selector
					.from(RemnantGoodsEntity.class)
					.where("username", "=", getUsername())
					.and(WhereBuilder.b("state", "=", STATE_TO_TAKE_GOODS).or(
							"state", " is ", null)));
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
	}

	/**
	 * 获取未收货的零件
	 *
	 * @return
	 */
	public List<DataDownloadEntity> getGoodsNotDelivery() {
		List<DataDownloadEntity> list = null;
		try {
			list = db.findAll(Selector
					.from(DataDownloadEntity.class)
					.where("username", "=", getUsername())
					.and(WhereBuilder.b("state", "=", STATE_TO_TAKE_GOODS).or(
							"state", " is ", null)));
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	public List<RemnantGoodsEntity> getAllData() {
		try {
			return db.findAll(RemnantGoodsEntity.class);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取已收货零件的数量
	 *
	 * @return
	 */
	public long getRemnantGoodsHaveTokenCount() {
		long count = 0;
		try {
			count = db.count(Selector.from(RemnantGoodsEntity.class)
					.where("state", "=", STATE_ALREADY_TAKE_GOODS)
					.and("username", "=", getUsername()));
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
	}

	/**
	 * 获取已扫描零件的数量
	 *
	 * @return
	 */
	public long getRemnantGoodsHaveScannedCount() {
		long count = 0;
		try {
			count = db.count(Selector.from(RemnantGoodsEntity.class)
					.where("state", "=", STATE_HAVE_SCANNED)
					.and("username", "=", getUsername()));
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
	}

	/**
	 * 清空已提交数据
	 */
	public void clearHaveSubmitData(List<RemnantGoodsEntity> rgList) {
		try {
			// for (int i = 0; i < rgList.size(); i++) {
			// db.delete(rgList.get(i));
			// }
			// db.
			db.deleteAll(rgList);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/********************************************************************/

	public void saveDataDownload(List<DataDownloadEntity> downloadList) {
		try {
			if (downloadList != null && downloadList.size() != 0) {
				for (int i = 0; i < downloadList.size(); i++) {
					DataDownloadEntity entity = downloadList.get(i);
					entity.setUsername(getUsername());
					Calendar calendar = Calendar.getInstance();

					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

					Date date = new Date();
					String str1 = format.format(date);
					Date parse = null;
					try {
						parse = format.parse(str1);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					String barcode = entity.getBarCode();
					if (!TextUtils.isEmpty(barcode)) {
						String[] barcodeArr = barcode.split(",");
						if (barcodeArr.length == 5) {
							entity.setBarzch(barcodeArr[0]);
							entity.setBarbah(barcodeArr[1]);
							entity.setBarghdh(barcodeArr[2]);
							entity.setBarcph(barcodeArr[3]);
							entity.setBarysth(barcodeArr[4]);
						}
					}
					entity.setCreateDate(parse);
					entity.setState(STATE_TO_TAKE_GOODS);
					List<DataDownloadEntity> dataList = db.findAll(Selector
							.from(DataDownloadEntity.class)
							.where("username", "=", getUsername())
							.and("id", "=", entity.getId()));
					if (dataList == null || dataList.size() == 0) {
						db.save(entity);
					} else {
						String newState = entity.getState();
						if (!TextUtils.isEmpty(newState)) {
							if ("03".equals(newState)) {
								db.save(entity);
							}
						}
					}
				}
			}

		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void saveDataDownload1(List<DataDownloadEntity> rgEntityList) {
//		List<String> listSStrings = getHaveScannedIDS();// 已扫描
//		List<String> listWStrings = getHaveMissedIDS();
		List<DataDownloadEntity> listWSH = getGoodsNotDelivery();
		List<DataDownloadEntity> listWZD = getHaveMissedGoods();
		List<DataDownloadEntity> listYSM = getHaveScannedGoods();
		try {
			db.deleteAll(listWSH);// 删除未收货数据
			db.deleteAll(listWZD);// 删除未找到数据
			db.deleteAll(listYSM);// 删除已扫描数据
			// 拿已扫描的配件和刚下载数据比较 若存在则把这条存在的数据在待收货中删除 若不存在则把这条存在的数据在已扫描中删除
			if (listYSM !=null) {

			if (listYSM.size() > 0) {
				for (int j = listYSM.size()-1; j >=0; j--) {
					if (rgEntityList != null && rgEntityList.size() != 0) {
						boolean flag = false;
						for (int i = 0; i < rgEntityList.size(); i++) {
							DataDownloadEntity rgEntity = rgEntityList.get(i);
							if (rgEntity.getId().equals(listYSM.get(j).getId())) {
								flag = true;// 找到了
								rgEntityList.remove(i);
//								break;
							}
						}
						// 找不到 则在已扫描中删除
						//remove时会因为集合变小导致下标越界
						if (!flag) {
							listYSM.remove(j);
						}
					}
				}
			}

			}
			// 拿未找到的配件和刚下载数据比较 若存在则把这条存在的数据在待收货中删除 若不存在则把这条存在的数据在未找到中删除
			if (listWZD!=null) {

			if (listWZD.size() > 0) {
				for (int j = listWZD.size()-1; j >=0; j--) {
					if (rgEntityList != null && rgEntityList.size() != 0) {
						boolean flag = false;
						for (int i = 0; i < rgEntityList.size(); i++) {
							DataDownloadEntity rgEntity = rgEntityList.get(i);
							if (rgEntity.getId().equals(listWZD.get(j).getId())) {
								flag = true;// 找到了
								rgEntityList.remove(i);
//								break;
							}
						}
						// 找不到 则在未找到中删除
						if (!flag) {
							listWZD.remove(j);
						}
					}
				}
			}

			}
			if (rgEntityList != null && rgEntityList.size() != 0) {
				for (int i = 0; i < rgEntityList.size(); i++) {
					DataDownloadEntity rgEntity = rgEntityList.get(i);
					rgEntity.setUsername(getUsername());
					Calendar calendar = Calendar.getInstance();
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					Date date = new Date();
					String str1 = format.format(date);
					Date parse = null;
					String barcode = rgEntity.getBarCode();
					if (!TextUtils.isEmpty(barcode)) {
						String[] barcodeArr = barcode.split(",");
//						if (barcodeArr.length == 5) {
						try {
							rgEntity.setBarzch(barcodeArr[0]);
							rgEntity.setBarbah(barcodeArr[1]);
							rgEntity.setBarghdh(barcodeArr[2]);
							rgEntity.setBarcph(barcodeArr[3]);
							rgEntity.setBarysth(barcodeArr[4]);
						}catch (Exception e){

						}
//						}
					}
					try {
						parse = format.parse(str1);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					rgEntity.setCreateDate(parse);
					rgEntity.setState(STATE_TO_TAKE_GOODS);
					db.save(rgEntity);
				}
			}
			// listYSM.notifyAll();
			// listWZD.notifyAll();
			if (listYSM !=null) {

			if (listYSM.size()>0) {
				db.saveAll(listYSM);
			}
			}

//			for (DataDownloadEntity dataDownloadEntity : listYSM) {
//				db.save(dataDownloadEntity);
//			} 
			if (listWZD !=null) {

			if (listWZD.size()>0) {
				db.saveAll(listWZD);
			}

			}
		} catch (DbException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// try {
		// if (downloadList != null && downloadList.size() != 0) {
		// for (int i = 0; i < downloadList.size(); i++) {
		// DataDownloadEntity entity = downloadList.get(i);
		// entity.setUsername(getUsername());
		// Calendar calendar = Calendar.getInstance();
		//
		// SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		//
		// Date date = new Date();
		// String str1 = format.format(date);
		// Date parse = null;
		// try {
		// parse = format.parse(str1);
		// } catch (ParseException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		// String barcode = entity.getBarCode();
		// if (!TextUtils.isEmpty(barcode)) {
		// String[] barcodeArr = barcode.split(",");
		// if (barcodeArr.length == 5) {
		// entity.setBarzch(barcodeArr[0]);
		// entity.setBarbah(barcodeArr[1]);
		// entity.setBarghdh(barcodeArr[2]);
		// entity.setBarcph(barcodeArr[3]);
		// entity.setBarysth(barcodeArr[4]);
		// }
		// }
		// entity.setCreateDate(parse);
		// entity.setState(STATE_TO_TAKE_GOODS);
		//
		// List<DataDownloadEntity> dataList = db.findAll(Selector
		// .from(DataDownloadEntity.class)
		// .where("username", "=", getUsername())
		// .and("id", "=", entity.getId()));
		// if (dataList == null || dataList.size() == 0) {
		// db.save(entity);
		// } else {
		// String newState = entity.getState();
		// if (!TextUtils.isEmpty(newState)) {
		// if ("03".equals(newState)) {
		// db.save(entity);
		// }
		// }
		// }
		// }
		// }
		//
		// } catch (DbException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

	}

	/**
	 * 获取未收货的零件数量
	 *
	 * @return
	 */
	public long getUntakeCount() {
		long count = 0;
		try {
			count = db.count(Selector
					.from(DataDownloadEntity.class)
					.where("username", "=", getUsername())
					.and(WhereBuilder.b("state", "=", STATE_TO_TAKE_GOODS).or(
							"state", " is ", null)));
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
	}

	/**
	 * 获取延迟收货零件数量
	 *
	 * @return
	 */
	public long getDelayCount2() {
		long count = 0;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		Date date = new Date();
		String str1 = format.format(date);
		Date parse = null;
		try {
			parse = format.parse(str1);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		try {
			count = db.count(Selector
					.from(DataDownloadEntity.class)
					.where("username", "=", getUsername())
					.and("createDate", "<", parse)
					.and(WhereBuilder.b("state", "=", STATE_TO_TAKE_GOODS).or(
							"state", " is ", null)));
		} catch (DbException e) {
			e.printStackTrace();
		}
		return count;
	}

	/**
	 * 获取未提货数据
	 *
	 * @return
	 */
	public LinkedHashMap<String, List<DataDownloadEntity>> getUntake() {
		LinkedHashMap<String, List<DataDownloadEntity>> map = new LinkedHashMap<String, List<DataDownloadEntity>>();
		try {

			List<DbModel> usernameList = db.findDbModelAll(DbModelSelector
					.from(DataDownloadEntity.class)
					.where("username", "=", getUsername())
					.and(WhereBuilder.b("state", "=", "01").or("state", "is",
							null)).groupBy("barghdh").orderBy("barghdh", true));
			if (usernameList != null && usernameList.size() > 0) {
				for (int i = 0; i < usernameList.size(); i++) {
					String goodNo = usernameList.get(i).getString("barghdh");
					List<DataDownloadEntity> subList = db.findAll(Selector
							.from(DataDownloadEntity.class)
							.where("username", "=", getUsername())
							.and(WhereBuilder.b("state", "=", "01").or("state",
									"is", null)).and("barghdh", "=", goodNo)
							.orderBy("id", true));
					map.put(goodNo, subList);
				}
			}

		} catch (DbException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 获取已扫描零件ID集合
	 */
	public List<String> getHaveScannedIDS() {
		List<String> listStrings = new ArrayList<String>();
		List<DataDownloadEntity> rgList = null;
		try {
			rgList = db.findAll(Selector.from(DataDownloadEntity.class)
					.where("state", "=", STATE_HAVE_SCANNED)
					.and("username", "=", getUsername()).orderBy("id", true));
		} catch (DbException e) {
			e.printStackTrace();
		}
		if (rgList != null && rgList.size() > 0) {
			for (int i = 0; i < rgList.size(); i++) {
				listStrings.add(rgList.get(i).getId());
			}
		}
		return listStrings;
	}

	/**
	 * 获取已扫描零件集合
	 */
	public List<DataDownloadEntity> getHaveScannedGoods() {
		List<DataDownloadEntity> rgList = null;
		try {
			rgList = db.findAll(Selector.from(DataDownloadEntity.class)
					.where("state", "=", STATE_HAVE_SCANNED)
					.and("username", "=", getUsername()).orderBy("id", true));
		} catch (DbException e) {
			e.printStackTrace();
		}

		return rgList;
	}

	/**
	 * 标记为已丢失零件
	 *
	 * @param rgEntity
	 */
	public void updateHaveMissedPart2(DataDownloadEntity rgEntity) {
		if (rgEntity != null) {
			try {
				db.update(rgEntity, WhereBuilder.b("id", "=", rgEntity.getId()));
			} catch (DbException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 根据条件查询未提货数据
	 *
	 * @param condition
	 * @return
	 */
	public LinkedHashMap<String, List<DataDownloadEntity>> getUntake(
			SearchCondition condition) {
		//查询时零件名称修改为车牌号
		List<DataDownloadEntity> rgList = null;
		LinkedHashMap<String, List<DataDownloadEntity>> map = new LinkedHashMap<String, List<DataDownloadEntity>>();
		try {
			rgList = db.findAll(Selector
					.from(DataDownloadEntity.class)
					.where("username", "=", getUsername())
					.and("bah",
							"like",
							"%"
									+ (condition.reportNo == null ? ""
											: condition.reportNo) + "%")
					.and("cxmc",
							"like",
							"%"
									+ (condition.vehicleModel == null ? ""
											: condition.vehicleModel) + "%")
					.and("barcph",
							"like",
							"%"
									+ (condition.partName == null ? ""
											: condition.partName) + "%")
					.and("createDate", "<=", condition.endDate)
					.and("createDate", ">=", condition.startDate)
					.and(WhereBuilder.b("state", "=", STATE_TO_TAKE_GOODS).or(
							"state", "is", null)));
			for (int i = 0; i < rgList.size(); i++) {
				if (rgList.get(i).getBarghdh() != null
						&& !"".equals(rgList.get(i).getBarghdh())) {
					map.put(rgList.get(i).getBarghdh(), null);
				}
			}
			for (String key : map.keySet()) {
				List<DataDownloadEntity> subList = new ArrayList<DataDownloadEntity>();
				for (int j = 0; j < rgList.size(); j++) {
					if (key.equals(rgList.get(j).getBarghdh())) {
						subList.add(rgList.get(j));
					}
				}
				map.put(key, subList);
			}

		} catch (DbException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 标记为待收货零件
	 *
	 * @param rgEntity
	 */
	public void updateWaitReceivePart2(DataDownloadEntity rgEntity) {
		if (rgEntity != null) {
			try {
				db.update(rgEntity, WhereBuilder.b("id", "=", rgEntity.getId()));
			} catch (DbException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 获取未找到零件
	 *
	 * @return
	 */
	public LinkedHashMap<String, List<DataDownloadEntity>> getHaveMissed() {
		LinkedHashMap<String, List<DataDownloadEntity>> map = new LinkedHashMap<String, List<DataDownloadEntity>>();
		try {
			List<DbModel> usernameList = db.findDbModelAll(DbModelSelector
					.from(DataDownloadEntity.class)
					.where("username", "=", getUsername())
					.and("state", "=", Constants.STATE_ALREADY_MISSED)
					.groupBy("barghdh").orderBy("barghdh", true));
			if (usernameList != null && usernameList.size() > 0) {
				for (int i = 0; i < usernameList.size(); i++) {
					String goodNo = usernameList.get(i).getString("barghdh");
					List<DataDownloadEntity> subList = db.findAll(Selector
							.from(DataDownloadEntity.class)
							.where("username", "=", getUsername())
							.and("state", "=", Constants.STATE_ALREADY_MISSED)
							.and("barghdh", "=", goodNo).orderBy("id", true));
					map.put(goodNo, subList);
				}
			}
		} catch (DbException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 获取所有已扫描零件
	 *
	 * @return
	 */
	public List<DataDownloadEntity> getAllHaveScannedPart2() {
		List<DataDownloadEntity> rgList = null;
		try {
			rgList = db.findAll(Selector.from(DataDownloadEntity.class)
					.where("state", "=", STATE_HAVE_SCANNED)
					.and("username", "=", getUsername())
					.orderBy("scanDate", true));
		} catch (DbException e) {
			e.printStackTrace();
		}
		return rgList;
	}

	/**
	 * 判断已扫描零件是否存在待确认零件库中
	 *
	 * @param rgEntity
	 * @return
	 */
	public List<DataDownloadEntity> isExistedInDB(DataDownloadEntity rgEntity) {
		List<DataDownloadEntity> rgList = null;
		if (rgEntity != null) {
			String barcode = rgEntity.getBarCode();
			String ysth = rgEntity.getBarysth();
			Log.i(TAG, "barcode = " + barcode);
			Log.i(TAG, "ysth = " + ysth);
			if (!TextUtils.isEmpty(barcode)) {
				try {
					rgList = db.findAll(Selector.from(DataDownloadEntity.class)
							.where("barCode", "=", barcode)
							.and("username", "=", getUsername()));
					Log.i(TAG, "barcode rgList size = " + rgList.size());
				} catch (DbException e) {
					e.printStackTrace();
					Log.i(TAG, "find by barcode exception = " + e.getMessage());
				}
			}

			if (!TextUtils.isEmpty(ysth)) {
				try {
					rgList = db.findAll(Selector.from(DataDownloadEntity.class)
							.where("barysth", "=", ysth)
							.and("username", "=", getUsername()));
					Log.i(TAG, "barysth rgList size = " + rgList.size());
				} catch (DbException e) {
					e.printStackTrace();
				}
			}

		}
		return rgList;
	}

	/**
	 * 标记为已扫描零件
	 *
	 * @param rgEntity
	 */
	public void updateHaveScannedPart2(DataDownloadEntity rgEntity) {
		if (rgEntity != null) {
			try {
				db.update(rgEntity, WhereBuilder.b("id", "=", rgEntity.getId()));
			} catch (DbException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 清空已提交数据
	 */
	public void clearHaveSubmitData2(List<DataDownloadEntity> rgList) {
		try {
			db.deleteAll(rgList);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 清空待收货数据
	 */
	public void clearDeliveryData(List<DataDownloadEntity> rgList) {
		try {
			db.deleteAll(rgList);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
}
