package com.example.lossqrcode.ui;

import org.apache.http.entity.InputStreamEntity;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ContinueUploadInputStreamEntity extends InputStreamEntity {

	private ProgressListener listener;

	public ContinueUploadInputStreamEntity(InputStream instream, long length,
										   ProgressListener listener) {
		super(instream, length);
		this.listener = listener;
	}

	@Override
	public void writeTo(OutputStream outstream) throws IOException {
		super.writeTo(new CountingOutputStream(outstream, listener));
	}

	public static interface ProgressListener {
		void transferred(long num);

		void onUpload(InputStream in, OutputStream out);
	}

	public class CountingOutputStream extends FilterOutputStream {

		private final ProgressListener listener;
		private long transferred;

		public CountingOutputStream(final OutputStream out,
				final ProgressListener listener) {
			super(out);
			this.listener = listener;
			this.transferred = 0;
		}

		public void write(byte[] b, int off, int len) throws IOException {
			out.write(b, off, len);
			this.transferred += len;
			this.listener.transferred(this.transferred);
			this.listener.onUpload(getContent(), out);
		}

		public void write(int b) throws IOException {
			out.write(b);
			this.transferred++;
			this.listener.transferred(this.transferred);
			this.listener.onUpload(getContent(), out);
		}
	}

}