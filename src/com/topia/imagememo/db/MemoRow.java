package com.topia.imagememo.db;

public class MemoRow {
	private int id;
	private String memo;
	private String image_path;
	private String created_at;
	private String updated_at;

	public MemoRow() {
		super();
	}

	public MemoRow(int id) {
		super();
		this.id = id;
	}

	public MemoRow(int id, String memo, String image_path, String created_at, String updated_at) {
		this.id = id;
		this.memo = memo;
		this.image_path = image_path;
		this.created_at = created_at;
		this.updated_at = updated_at;
	}

	public int getId() {
		return this.id;
	}

	public void setMemo(String s) {
		this.memo = s;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setImagePath(String s) {
		this.image_path = s;
	}

	public String getImagePath() {
		return this.image_path;
	}


	public String getCreatedAt() {
		return this.created_at;
	}

	public String getUpdatedAt() {
		return this.updated_at;
	}

	public String to_s() {
		String s =
				"id: " + (int) id +
				" memo: " + memo +
				" image_path: " + image_path +
				" created_at: " + created_at +
				" updated_at: " + updated_at;
		return s;

	}
}
