/*
 * Copyright (C) 2013 Charon Chui <charon.chui@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.lossqrcode.ui.widget;

import com.example.lossqrcode.R;

import android.content.Context;
import android.provider.Telephony.Mms.Part;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.TextView;

/**
 * Android load more ListView when scroll down.
 * 
 */
public class LoadMoreListView extends ListView {
	protected static final String TAG = "LoadMoreListView";
	private View mFooterView;
	private OnScrollListener mOnScrollListener;
	private OnLoadMoreListener mOnLoadMoreListener;
	private TextView tvLoadMoreText;
	/**
	 * If is loading now.
	 */
	private boolean mIsLoading;
	private boolean mHasNoMore = false;

	private int lastItemIndex;

	public LoadMoreListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public LoadMoreListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public LoadMoreListView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		mFooterView = View.inflate(context, R.layout.load_more_footer, null);
		tvLoadMoreText=(TextView)mFooterView.findViewById(R.id.load_more_footer_text);
		FrameLayout footerLayoutHolder = new FrameLayout(getContext());
		footerLayoutHolder.addView(mFooterView, 0,
				new FrameLayout.LayoutParams(
						FrameLayout.LayoutParams.MATCH_PARENT,
						FrameLayout.LayoutParams.WRAP_CONTENT));
		addFooterView(footerLayoutHolder);
		//hideFooterView();
		/*
		 * Must use super.setOnScrollListener() here to avoid override when call
		 * this view's setOnScrollListener method
		 */
		super.setOnScrollListener(superOnScrollListener);
	}

	/**
	 * Hide the load more view(footer view)
	 */
	private void hideFooterView() {
        mFooterView.setVisibility(View.GONE);
        tvLoadMoreText.setText("更多");
    }

	/**
	 * Show load more view
	 */
	private void showFooterView() {
		mFooterView.setVisibility(View.VISIBLE);
		tvLoadMoreText.setText("加载中...");
	}

	@Override
	public void setOnScrollListener(OnScrollListener l) {
		mOnScrollListener = l;
	}

	/**
	 * Set load more listener, usually you should get more data here.
	 * 
	 * @param listener
	 *            OnLoadMoreListener
	 * @see OnLoadMoreListener
	 */
	public void setOnLoadMoreListener(OnLoadMoreListener listener) {
		mOnLoadMoreListener = listener;
	}

	/**
	 * When complete load more data, you must use this method to hide the footer
	 * view, if not the footer view will be shown all the time.
	 */
	public void onLoadMoreComplete() {
		mIsLoading = false;
		hideFooterView();
		// removeFooterView(mFooterView);
	}

	public void setHasNoMore(boolean no) {
		mHasNoMore = no;
		if(no==false){
			showFooterView();
		}
		mIsLoading = false;
		hideFooterView();
	}

	private OnScrollListener superOnScrollListener = new OnScrollListener() {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			// Avoid override when use setOnScrollListener
			int itemsLastIndex = getAdapter().getCount() - 1; // 数据集最后一项的索引
			if (mHasNoMore == false && !mIsLoading
					&& scrollState == OnScrollListener.SCROLL_STATE_IDLE
					&& itemsLastIndex == lastItemIndex) {
				showFooterView();
				mIsLoading = true;
				if (mOnLoadMoreListener != null) {
					mOnLoadMoreListener.onLoadMore();
					System.out.println("load more");
				}
			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			lastItemIndex = firstVisibleItem + visibleItemCount - 1;
			// The count of footer view will be add to visibleItemCount also are
			// added to totalItemCount
			if (visibleItemCount == totalItemCount) {
				// If all the item can not fill screen, we should make the
				// footer view invisible.
				// hideFooterView();
			}
		}
	};

	/**
	 * Interface for load more
	 */
	public interface OnLoadMoreListener {
		/**
		 * Load more data.
		 */
		void onLoadMore();
	}

}
