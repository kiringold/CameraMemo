package com.topia.imagememo;

import java.util.ArrayList;

import com.topia.imagememo.db.Memo;
import com.topia.imagememo.db.MemoRow;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class DbsampleActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dbsample);


		find_all();



		Button btn_insert = (Button)findViewById(R.id.insert);

		btn_insert.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// DBに入れるデータを用意
				MemoRow test = new MemoRow();
				test.setMemo("meeeemo");
				test.setImagePath("paaaath");

				//DBに接続
				Memo memo = new Memo(DbsampleActivity.this);
				memo.open();

				// データを登録
				memo.insert(test);

				//DB切断
				memo.close();

				Log.d("test", "click on insert");
			}
		});

		Button btn_update = (Button)findViewById(R.id.update);
		btn_update.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.d("test", "click on update");
				// 更新するデータを用意
				MemoRow test = new MemoRow(1);
				test.setMemo("meeeemo");
				test.setImagePath("paaaath");

				//DBに接続
				Memo memo = new Memo(DbsampleActivity.this);
				memo.open();

				// データを変更
				memo.update(test);

				//DB切断
				memo.close();
			}
		});

		Button btn_delete = (Button)findViewById(R.id.delete);
		btn_delete.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.d("test", "click on delete");
				// 削除するデータを用意
				MemoRow test = new MemoRow(1);
				test.setMemo("meeeemo");
				test.setImagePath("paaaath");

				//DBに接続
				Memo memo = new Memo(DbsampleActivity.this);
				memo.open();

				// データ削除
				memo.delete(test);

				//DB切断
				memo.close();
			}
		});

		Button btn_list = (Button)findViewById(R.id.list);
		btn_list.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.d("test", "click on delete");
				find_all();
			}
		});

		Button btn_top = (Button)findViewById(R.id.top);
		btn_top.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(DbsampleActivity.this , ListActivity.class);
				startActivity(intent);
			}
		});
	}

	public void find_all() {
		//DBに接続
		Memo memo = new Memo(this);
		memo.open();

		// 全件取得
		ArrayList<MemoRow> memos = memo.find_all();

		// 取得したデータを表示
		for(int i=0;i<memos.size();i++){
			MemoRow row = (MemoRow)memos.get(i);
			Log.d("test", row.to_s());
		}

		//DB切断
		memo.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dbsample, menu);
		return true;
	}

}
