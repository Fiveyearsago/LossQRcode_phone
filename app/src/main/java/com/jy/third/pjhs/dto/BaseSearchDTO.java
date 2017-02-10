package com.jy.third.pjhs.dto;

import java.util.ArrayList;
import java.util.List;

public class BaseSearchDTO<T> {
	/** 记录表ID */
	private String[] ids;
	private int pageSize = 0;// 分页为10
	private int pageNo = 0;// 当前页数
	private int recTotal = 0;// 总记录数
	private int pageTotal = 0;// 总页数
	private String begin;// 搜索的起始时间
	private String end;// 搜索的截止时间
	private T searchCondition;

	public T getSearchCondition() {
		return searchCondition;
	}

	public void setSearchCondition(T searchCondition) {
		this.searchCondition = searchCondition;
	}

	/** 用来封装搜索的结果 利用泛型，一次解决 */
	private List<T> searchResultList;

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getRecTotal() {
		return recTotal;
	}

	public void setRecTotal(int recTotal) {
		this.recTotal = recTotal;
	}

	public int getPageTotal() {
		return pageTotal;
	}

	public void setPageTotal(int pageTotal) {
		this.pageTotal = pageTotal;
	}

	public String getBegin() {
		return begin;
	}

	public void setBegin(String begin) {
		this.begin = begin;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public List<T> getSearchResultList() {
		return searchResultList;
	}

	public void setSearchResultList(List<T> searchResultList) {
		this.searchResultList = searchResultList;
	}

	/** 将dao层未分页的数据分页展示 */
	public void setPageList(List<T> dataList) {
		recTotal = dataList.size();
		pageTotal = (recTotal - 1) / pageSize + 1;
		List<T> tempList = new ArrayList<T>();
		int first = pageSize * (pageNo - 1); // 每页的起始值
		int end = first + pageSize; // 每页的最后一条记录
		end = (end < recTotal) ? end : recTotal;
		for (int t = first; t < end; t++)
			tempList.add(dataList.get(t));
		searchResultList = tempList;
	}

	public String[] getIds() {
		return ids;
	}

	public void setIds(String[] ids) {
		this.ids = ids;
	}

	public int getPagesize() {
		return pageSize;
	}

	public void setPagesize(int pagesize) {
		this.pageSize = pagesize;
	}

	public int getPageno() {
		return pageNo;
	}

	public void setPageno(int pageno) {
		this.pageNo = pageno;
	}

	public int getRectotal() {
		return recTotal;
	}

	public void setRectotal(int rectotal) {
		this.recTotal = rectotal;
	}

	public int getPagetotal() {
		return pageTotal;
	}

	public void setPagetotal(int pagetotal) {
		this.pageTotal = pagetotal;
	}
}
