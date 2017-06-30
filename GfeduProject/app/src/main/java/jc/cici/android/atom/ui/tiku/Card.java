package jc.cici.android.atom.ui.tiku;

import java.io.Serializable;

public class Card implements Serializable {
	private static final long serialVersionUID = 1L;
	// 答题标记
	private boolean flag;
	// 作答状态
	private boolean status;
	// 按钮个数
	private int size;
	// 当前位置
	private int position;

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

}
