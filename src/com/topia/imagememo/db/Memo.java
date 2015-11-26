package com.topia.imagememo.db;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Memo {

	static final String DATABASE_NAME = "image_memo.db";
	static final int DATABASE_VERSION = 1;

	public static final String TABLE_NAME = "memo";
	public static final String COL_ID = "id";
	public static final String COL_MEMO = "memo";
	public static final String COL_PATH = "image_path";
	public static final String COL_CREATED_AT = "created_at";
	public static final String COL_UPDATED_AT = "updated_at";

	protected final Context context;
	protected DatabaseHelper dbHelper;
	protected SQLiteDatabase db;

	public Memo(Context context){
		this.context = context;
		dbHelper = new DatabaseHelper(this.context);
	}

	//
	// SQLiteOpenHelper
	//

	private static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(
					"CREATE TABLE " + TABLE_NAME + " ("
							+ COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
							+ COL_MEMO + " TEXT NOT NULL,"
							+ COL_PATH + " TEXT NOT NULL,"
							+ COL_CREATED_AT + " TEXT NOT NULL,"
							+ COL_UPDATED_AT + " TEXT NOT NULL);");
		}

		@Override
		public void onUpgrade(
				SQLiteDatabase db,
				int oldVersion,
				int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
			onCreate(db);
		}

	}

	//
	// Adapter Methods
	//

	public Memo open() {
		db = dbHelper.getWritableDatabase();
		return this;
	}


	public void close(){
		dbHelper.close();
	}

	private String getDateTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		Date date = new Date();
		return dateFormat.format(date);
	}

	//
	// App Methods
	//
	public int insert(MemoRow row){
		if(row == null) throw new IllegalArgumentException("MemoRowがnull");

		String memo = row.getMemo();
		String path = row.getImagePath();
		if(memo == null || memo.equals("")) {
			throw new IllegalArgumentException("メモは必須");
		}
		if(path == null || path.equals("")) {
			throw new IllegalArgumentException("画像は必須");
		}

		ContentValues values = new ContentValues();
		values.put(COL_MEMO, memo);
		values.put(COL_PATH, path);

		values.put(COL_CREATED_AT, getDateTime());
		values.put(COL_UPDATED_AT, getDateTime());

		int id = (int)db.insert(TABLE_NAME, null, values);

		Log.d("test", id + "");
		Log.d("test", getDateTime());

		return id;
	}

	public boolean update(MemoRow row){
		if(row == null) throw new IllegalArgumentException("MemoRowがnull");

		String memo = row.getMemo();
		String path = row.getImagePath();
		if(memo == null || memo.equals("")) {
			throw new IllegalArgumentException("メモは必須");
		}
		if(path == null || path.equals("")) {
			throw new IllegalArgumentException("画像は必須");
		}

		ContentValues values = new ContentValues();
		values.put(COL_MEMO, memo);
		values.put(COL_PATH, path);
		values.put(COL_UPDATED_AT, getDateTime());

		long id = db.update(TABLE_NAME, values, COL_ID + " = ?", new String[]{ Integer.toString(row.getId()) });

		Log.d("test", id + "");
		Log.d("test", getDateTime());

		return true;
	}

	public boolean delete(MemoRow row){
		return db.delete(TABLE_NAME, COL_ID + "= ?", new String[]{ Integer.toString(row.getId()) }) > 0;
	}


	public MemoRow find(int id){
		if(id == 0) throw new IllegalArgumentException("idは0不可。");

		Cursor c =db.query(TABLE_NAME, null, "id = ?", new String[]{ Integer.toString(id) }, null, null, null);
		ArrayList<MemoRow> memos = new ArrayList<MemoRow>();
		if(!c.moveToFirst()){
			// エラー処理
			throw new IllegalArgumentException("存在しないレコード");
		}
		MemoRow memo = new MemoRow(
				c.getInt(c.getColumnIndex(Memo.COL_ID)),
				c.getString(c.getColumnIndex(Memo.COL_MEMO)),
				c.getString(c.getColumnIndex(Memo.COL_PATH)),
				c.getString(c.getColumnIndex(Memo.COL_CREATED_AT)),
				c.getString(c.getColumnIndex(Memo.COL_UPDATED_AT))
				);

		return memo;
	}

	public ArrayList<MemoRow> find_all(){
		Cursor c =db.query(TABLE_NAME, null, null, null, null, null, "id desc");
		ArrayList<MemoRow> memos = new ArrayList<MemoRow>();
		if(c.moveToFirst()){
			do {
				MemoRow memo = new MemoRow(
						c.getInt(c.getColumnIndex(Memo.COL_ID)),
						c.getString(c.getColumnIndex(Memo.COL_MEMO)),
						c.getString(c.getColumnIndex(Memo.COL_PATH)),
						c.getString(c.getColumnIndex(Memo.COL_CREATED_AT)),
						c.getString(c.getColumnIndex(Memo.COL_UPDATED_AT))
					);
				memos.add(memo);
			} while(c.moveToNext());
		}
		return memos;
	}

	public ArrayList<MemoRow> find_search(String word){
		if(word == null || word.equals("")) throw new IllegalArgumentException("wordが空");

		Cursor c =db.query(TABLE_NAME, null, "memo like ?", new String[]{ "%"+ word +"%" }, null, null, "id desc");
		ArrayList<MemoRow> memos = new ArrayList<MemoRow>();
		if(c.moveToFirst()){
			do {
				MemoRow memo = new MemoRow(
						c.getInt(c.getColumnIndex(Memo.COL_ID)),
						c.getString(c.getColumnIndex(Memo.COL_MEMO)),
						c.getString(c.getColumnIndex(Memo.COL_PATH)),
						c.getString(c.getColumnIndex(Memo.COL_CREATED_AT)),
						c.getString(c.getColumnIndex(Memo.COL_UPDATED_AT))
					);
				memos.add(memo);
			} while(c.moveToNext());
		}
		return memos;
	}
}
