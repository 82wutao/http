package http.net.kernel;

import http.memories.PoolableObject;

import java.io.IOException;

public class XBuffer implements PoolableObject{
	boolean wrapped;
	byte[] data;
	int length;
	int limit;
	int index;

	int formatPos;
	int formatSize;

	public XBuffer(byte[] dat, int offset, int length) {
		wrapped = true;
		data = dat;
		this.length = dat.length;
		limit = offset + length;
		index = offset;
	}

	public XBuffer(int capacity) {
		wrapped = false;
		data = new byte[capacity];
		length = capacity;
		limit = capacity;
		index = 0;
	}

	public void readyWritingToBuffer() {
		this.index = 0;
		this.limit = this.length;
	}

	public void readyReadingFromBuffer() {
		this.limit = this.index;
		this.index = 0;
	}

	public int remain() {
		return limit - index;
	}

	public byte readbyte() throws IOException {
		if (this.index + 1 > this.limit) {
			throw newIOException(index, 1, limit);
		}

		this.index++;
		return this.data[this.index];
	}

	public byte readbyte(int pos) throws IOException {
		if (pos + 1 > this.limit) {
			throw newIOException(pos, 1, limit);
		}
		return this.data[pos];
	}

	public void writebyte(byte value) {
		moreBuffer(this,1);
		this.data[this.index] = (byte) value;
		this.index++;
	}

	public void writebyte(int pos, byte value) {
		if (pos + 1 > this.limit) {
			return;
		}
		this.data[pos] = (byte) value;
	}

	public short readshort(int is_big) throws IOException {
		if ((this.index + 2) > this.limit) {
			throw newIOException(index, 2, limit);
		}
		long out = read(data, is_big, index, 2);
		this.index += 2;
		return (short) out;
	}

	public short readshort(int pos, int is_big) throws IOException {
		if ((pos + 2) > this.limit) {
			throw newIOException(pos, 2, limit);
		}
		long out = read(data, is_big, pos, 2);
		return (short) out;
	}

	public void writeshort(short value, int is_big) {
		moreBuffer(this,2);
		
		int idx = this.index;

		if (is_big == 1) {
			byte v = (byte) (value >> 8);
			this.data[idx] = v;
			v = (byte) (value);
			this.data[idx + 1] = v;
		} else {
			this.data[idx] = (byte) value;
			this.data[idx + 1] = (byte) (value >> 8);
		}
		this.index += 2;
	}

	public void writeshort(int pos, short value, int is_big) {
		if ((pos + 2) > this.limit) {
			return;
		}

		if (is_big == 1) {
			this.data[pos] = (byte) (value >> 8);
			this.data[pos + 1] = (byte) value;
		} else {
			this.data[pos] = (byte) value;
			this.data[pos + 1] = (byte) (value >> 8);
		}
	}

	public int readint(int is_big) throws IOException {
		if ((this.index + 4) > this.limit) {
			throw newIOException(index, 4, limit);
		}
		long out = read(data, is_big, index, 4);
		this.index += 4;

		return (int) out;
	}

	public int readint(int pos, int is_big) throws IOException {
		if ((pos + 4) > this.limit) {
			throw newIOException(pos, 4, limit);
		}
		long out = read(data, is_big, pos, 4);

		return (int) out;
	}

	public void writeint(int value, int is_big) {
		moreBuffer(this,4);
		int idx = this.index;

		if (is_big == 1) {
			this.data[idx] = (byte) (value >> 24);
			this.data[idx + 1] = (byte) (value >> 16);
			this.data[idx + 2] = (byte) (value >> 8);
			this.data[idx + 3] = (byte) value;
		} else {
			this.data[idx] = (byte) value;
			this.data[idx + 1] = (byte) (value >> 8);
			this.data[idx + 2] = (byte) (value >> 16);
			this.data[idx + 3] = (byte) (value >> 24);
		}
		this.index += 4;

	}

	public void writeint(int pos, int value, int is_big) {
		if ((pos + 4) > this.limit) {
			return;
		}

		if (is_big == 1) {
			this.data[pos] = (byte) (value >> 24);
			this.data[pos + 1] = (byte) (value >> 16);
			this.data[pos + 2] = (byte) (value >> 8);
			this.data[pos + 3] = (byte) value;
		} else {
			this.data[pos] = (byte) value;
			this.data[pos + 1] = (byte) (value >> 8);
			this.data[pos + 2] = (byte) (value >> 16);
			this.data[pos + 3] = (byte) (value >> 24);
		}

	}

	public long readlong(int is_big) throws IOException {
		if ((this.index + 8) > this.limit) {
			throw newIOException(index, 8, limit);
		}
		long out = read(data, is_big, index, 8);
		this.index += 8;
		return out;
	}

	public long readlong(int pos, int is_big) throws IOException {
		if ((pos + 8) > this.limit) {
			throw newIOException(pos, 8, limit);
		}
		long out = read(data, is_big, pos, 8);
		return out;
	}

	public void writelong(long value, int is_big) {
		moreBuffer(this,8);
		int idx = this.index;

		if (is_big == 1) {
			this.data[idx] = (byte) (value >> 56);
			this.data[idx + 1] = (byte) (value >> 48);
			this.data[idx + 2] = (byte) (value >> 40);
			this.data[idx + 3] = (byte) (value >> 32);
			this.data[idx + 4] = (byte) (value >> 24);
			this.data[idx + 5] = (byte) (value >> 16);
			this.data[idx + 6] = (byte) (value >> 8);
			this.data[idx + 7] = (byte) value;
		} else {
			this.data[idx] = (byte) value;
			this.data[idx + 1] = (byte) (value >> 8);
			this.data[idx + 2] = (byte) (value >> 16);
			this.data[idx + 3] = (byte) (value >> 24);
			this.data[idx + 4] = (byte) (value >> 32);
			this.data[idx + 5] = (byte) (value >> 40);
			this.data[idx + 6] = (byte) (value >> 48);
			this.data[idx + 7] = (byte) (value >> 56);
		}
		this.index += 8;

	}

	public void writelong(int pos, long value, int is_big) {
		if ((pos + 8) > this.limit) {
			return;
		}
		int idx = pos;
		if (is_big == 1) {
			this.data[idx] = (byte) (value >> 56);
			this.data[idx + 1] = (byte) (value >> 48);
			this.data[idx + 2] = (byte) (value >> 40);
			this.data[idx + 3] = (byte) (value >> 32);
			this.data[idx + 4] = (byte) (value >> 24);
			this.data[idx + 5] = (byte) (value >> 16);
			this.data[idx + 6] = (byte) (value >> 8);
			this.data[idx + 7] = (byte) value;
		} else {
			this.data[pos] = (byte) value;
			this.data[pos + 1] = (byte) (value >> 8);
			this.data[pos + 2] = (byte) (value >> 16);
			this.data[pos + 3] = (byte) (value >> 24);
			this.data[pos + 4] = (byte) (value >> 32);
			this.data[pos + 5] = (byte) (value >> 40);
			this.data[pos + 6] = (byte) (value >> 48);
			this.data[pos + 7] = (byte) (value >> 56);
		}
	}

	public void forward(int count) throws IOException {
		int idx = this.index;

		if ((idx + count) > this.limit) {
			throw newIOException(idx, count, limit);
		}
		this.index += count;
	}

	public void begin_batch(int formatSize) throws IOException {
		if (formatSize != 1 && formatSize != 2 && formatSize != 4) {
			return;
		}

		int idx = this.index;
		if ((idx + formatSize) > this.limit) {
			throw newIOException(idx, formatSize, this.limit);
		}
		this.formatSize = formatSize;
		this.formatPos = this.index;
		forward(formatSize);
	}

	public int end_batch(int batchCount, int is_big) {

		if (this.formatSize == 1) {
			this.data[this.formatPos] = (byte) batchCount;
		} else if (this.formatSize == 2) {
			int idx = this.formatPos;
			if (is_big == 1) {
				this.data[idx] = (byte) (batchCount >> 8);
				this.data[idx + 1] = (byte) batchCount;
			} else {
				this.data[idx] = (byte) batchCount;
				this.data[idx + 1] = (byte) (batchCount >> 8);
			}
		} else if (this.formatSize == 4) {
			int idx = this.formatPos;

			if (is_big == 1) {
				this.data[idx] = (byte) (batchCount >> 24);
				this.data[idx + 1] = (byte) (batchCount >> 16);
				this.data[idx + 2] = (byte) (batchCount >> 8);
				this.data[idx + 3] = (byte) batchCount;
			} else {
				this.data[idx] = (byte) batchCount;
				this.data[idx + 1] = (byte) (batchCount >> 8);
				this.data[idx + 2] = (byte) (batchCount >> 16);
				this.data[idx + 3] = (byte) (batchCount >> 24);
			}
		} else {
			return -1;
		}
		return 0;
	}

	public void readbytes(byte[] value, int begin, int leng) throws IOException {
		if ((this.index + leng) > this.limit) {
			throw newIOException(this.index, leng, limit);
		}
		System.arraycopy(data, index, value, begin, leng);
		this.index += leng;
	}

	public void readbytes(int pos, byte[] value, int begin, int leng)
			throws IOException {
		if ((pos + leng) > this.limit) {
			throw newIOException(pos, leng, limit);
		}
		System.arraycopy(data, pos, value, begin, leng);
	}

	public void writebytes(byte[] value, int offset, int arr_len) {
		if ((this.index + arr_len) > this.limit) {
			return;
		}
		System.arraycopy(value,offset,data, index, arr_len);
		this.index += arr_len;
	}

	public void writebytes(int pos, byte[] value, int offer, int arr_len) {
		if ((pos + arr_len) > this.limit) {
			return ;
		}
		System.arraycopy(value,offer,data, pos, arr_len);
	}

	public byte[] getData() {
		return data;
	}

	public void compact() {
		int offset = this.index;
		int length = this.limit - offset;
		for (int i = 0; i < length; i++) {
			this.data[i] = this.data[offset + i];
		}

		this.index = length;
		this.limit = this.length;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getLimit() {
		return this.limit;
	}

	public void setPosition(int pos) {
		this.index = pos;
	}

	public int getPosition() {
		return this.index;
	}

	public String toString() {
		return "\ndebug :[" + this.data + "]";
	}

	private static IOException newIOException(int index, int leng, int limit) {
		return new IOException("index at " + index + ",read " + leng
				+ " bytes, but limit is " + limit);
	}

	public static long read(byte[] input, int is_big, int idx, int len) {
		long out = 0x0000000000000000;
		if (is_big == 1) {
			for (int i = 0; i < len; i++) {
				out = out << 8;
				out = (input[idx + i] & 0xff) | out;

			}
		} else {
			for (int i = 0; i < len; i++) {
				out = out << 8;
				out = (input[idx + 1 - i] & 0xff) | out;
			}
		}
		return out;
	}
	public static void write(byte[] output, int is_big, int idx,int len, long value){
		if (is_big == 1) {
			for (int i = 0; i < len; i++) {
				output[idx+i] = (byte) (value >> (len * 8 - (i+1)*8));
			}
		} else {
			for (int i = 0; i < len; i++) {
				output[idx+i] = (byte) (value >> (i*8));
			}
		}
	}

	private static void moreBuffer(XBuffer msg, int bytes) {
		if (msg.getPosition() + bytes <= msg.getLimit()) {
			return;
		}
		int pos = msg.getPosition();

		int times = (msg.getPosition() + bytes) / msg.getLimit();

		msg.length = msg.getLimit() * (times + 1);
		byte[] newBlock = new byte[msg.length];
		System.arraycopy(msg.data, 0, newBlock, 0, pos);
		msg.data = newBlock;
		msg.limit=msg.length;
		msg.index=pos;
	}
	
	/**
	 * 
	 * @return
	 */
	public XBuffer cloneSelf(){
		
		XBuffer another =new XBuffer(this.data,0,length);
		return another;
	}

	@Override
	public void free() {
	}
}
