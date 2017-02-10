package com.example.lossqrcode.ui.widget;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lossqrcode.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public class SegmentBar extends LinearLayout {
	private TextView textView1;
	private TextView textView2;
	private onSegmentViewClickListener listener;

	public SegmentBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public SegmentBar(Context context) {
		super(context);
		init();
	}

	private void init() {
		this.setLayoutParams(new LinearLayout.LayoutParams(dp2Px(getContext(),
				60), LinearLayout.LayoutParams.WRAP_CONTENT));
		textView1 = new TextView(getContext());
		textView2 = new TextView(getContext());
		textView1.setLayoutParams(new LayoutParams(0,
				LayoutParams.WRAP_CONTENT, 1));
		textView2.setLayoutParams(new LayoutParams(0,
				LayoutParams.WRAP_CONTENT, 1));
		textView1.setText(R.string.wait_for_receive_goods);
		textView2.setText(R.string.already_receive_goods);
//		XmlPullParser xrp = getResources().getXml(
//				R.drawable.seg_text_color_selector);
		XmlPullParser xrp=new XmlPullParser() {
			@Override
			public void setFeature(String name, boolean state) throws XmlPullParserException {

			}

			@Override
			public boolean getFeature(String name) {
				return false;
			}

			@Override
			public void setProperty(String name, Object value) throws XmlPullParserException {

			}

			@Override
			public Object getProperty(String name) {
				return null;
			}

			@Override
			public void setInput(Reader in) throws XmlPullParserException {

			}

			@Override
			public void setInput(InputStream inputStream, String inputEncoding) throws XmlPullParserException {

			}

			@Override
			public String getInputEncoding() {
				return null;
			}

			@Override
			public void defineEntityReplacementText(String entityName, String replacementText) throws XmlPullParserException {

			}

			@Override
			public int getNamespaceCount(int depth) throws XmlPullParserException {
				return 0;
			}

			@Override
			public String getNamespacePrefix(int pos) throws XmlPullParserException {
				return null;
			}

			@Override
			public String getNamespaceUri(int pos) throws XmlPullParserException {
				return null;
			}

			@Override
			public String getNamespace(String prefix) {
				return null;
			}

			@Override
			public int getDepth() {
				return 0;
			}

			@Override
			public String getPositionDescription() {
				return null;
			}

			@Override
			public int getLineNumber() {
				return 0;
			}

			@Override
			public int getColumnNumber() {
				return 0;
			}

			@Override
			public boolean isWhitespace() throws XmlPullParserException {
				return false;
			}

			@Override
			public String getText() {
				return null;
			}

			@Override
			public char[] getTextCharacters(int[] holderForStartAndLength) {
				return new char[0];
			}

			@Override
			public String getNamespace() {
				return null;
			}

			@Override
			public String getName() {
				return null;
			}

			@Override
			public String getPrefix() {
				return null;
			}

			@Override
			public boolean isEmptyElementTag() throws XmlPullParserException {
				return false;
			}

			@Override
			public int getAttributeCount() {
				return 0;
			}

			@Override
			public String getAttributeNamespace(int index) {
				return null;
			}

			@Override
			public String getAttributeName(int index) {
				return null;
			}

			@Override
			public String getAttributePrefix(int index) {
				return null;
			}

			@Override
			public String getAttributeType(int index) {
				return null;
			}

			@Override
			public boolean isAttributeDefault(int index) {
				return false;
			}

			@Override
			public String getAttributeValue(int index) {
				return null;
			}

			@Override
			public String getAttributeValue(String namespace, String name) {
				return null;
			}

			@Override
			public int getEventType() throws XmlPullParserException {
				return 0;
			}

			@Override
			public int next() throws XmlPullParserException, IOException {
				return 0;
			}

			@Override
			public int nextToken() throws XmlPullParserException, IOException {
				return 0;
			}

			@Override
			public void require(int type, String namespace, String name) throws XmlPullParserException, IOException {

			}

			@Override
			public String nextText() throws XmlPullParserException, IOException {
				return null;
			}

			@Override
			public int nextTag() throws XmlPullParserException, IOException {
				return 0;
			}
		};
		try {
			ColorStateList csl = ColorStateList.createFromXml(getResources(),
					xrp);
			textView1.setTextColor(csl);
			textView2.setTextColor(csl);
		} catch (Exception e) {
		}
		textView1.setGravity(Gravity.CENTER);
		textView2.setGravity(Gravity.CENTER);
		textView1.setPadding(3, 10, 0, 10);
		textView2.setPadding(3, 10, 0, 10);
		setSegmentTextSize(16);
		textView1.setBackgroundResource(R.drawable.seg_left);
		textView2.setBackgroundResource(R.drawable.seg_right);
		textView1.setSelected(true);
		this.removeAllViews();
		this.addView(textView1);
		this.addView(textView2);
		this.invalidate();

		textView1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (textView1.isSelected()) {
					return;
				}
				textView1.setSelected(true);
				textView2.setSelected(false);
				if (listener != null) {
					listener.onSegmentViewClick(textView1, 0);
				}
			}
		});
		textView2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (textView2.isSelected()) {
					return;
				}
				textView2.setSelected(true);
				textView1.setSelected(false);
				if (listener != null) {
					listener.onSegmentViewClick(textView2, 1);
				}
			}
		});
	}

	/**
	 * 设置字体大小 单位dip
	 * 
	 * @param dp
	 */
	public void setSegmentTextSize(int dp) {
		textView1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, dp);
		textView2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, dp);
	}

	private static int dp2Px(Context context, float dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}

	public void setOnSegmentViewClickListener(
			onSegmentViewClickListener listener) {
		this.listener = listener;
	}

	/**
	 * 设置文字
	 * @param text
	 * @param position
	 */
	public void setSegmentText(CharSequence text, int position) {
		if (position == 0) {
			textView1.setText(text);
		}
		if (position == 1) {
			textView2.setText(text);
		}
	}

	public static interface onSegmentViewClickListener {
		/**
		 * 
		 * 
		 * @param v
		 * @param position
		 *            0-左边 1-右边
		 */
		public void onSegmentViewClick(View v, int position);
	}
}