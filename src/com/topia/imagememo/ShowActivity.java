package com.topia.imagememo;

import java.io.File;
import java.io.IOException;

import com.topia.imagememo.db.Memo;
import com.topia.imagememo.db.MemoRow;
import com.topia.imagememo.image.BitmapResizer;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ShowActivity extends Activity {

	MemoRow row;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show);

		Intent i = getIntent();
		int id = i.getIntExtra("id", 0);

		Memo memo = new Memo(this);
		memo.open();

		// 取得
		row = memo.find(id);

		//DB切断
		memo.close();

		// 取得した情報の取得
		Log.d("test", "ShowActivity" + row.getId());
		Log.d("test", "ShowActivity" + row.getMemo());
		Log.d("test", "ShowActivity" + row.getImagePath());
		Log.d("test", "ShowActivity" + row.getCreatedAt());

		//該当するテキスト、画像、登録日時を画面表示に渡す
		((TextView)findViewById(R.id.memo)).setText(row.getMemo());
		((TextView)findViewById(R.id.created_at)).setText(row.getCreatedAt());

		ImageView img = (ImageView)findViewById(R.id.imageView1);
		BitmapResizer bitmapResizer = new BitmapResizer(this);
		try {
			Bitmap bitmap = bitmapResizer.resize(Uri.parse(row.getImagePath()), 300, 300);
			img.setImageBitmap(null);
			img.setImageBitmap(bitmap);
			img.setVisibility(View.VISIBLE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show, menu);

		return true;
	}

	// onOptionsItemSelectedメソッド(メニューアイテム選択処理)
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.item1:
			return true;
		case R.id.subitem1:
			Intent toEdit = new Intent(ShowActivity.this, EditActivity.class);
			toEdit.putExtra("id", row.getId());
			startActivity(toEdit);
			finish();
			return true;

		case R.id.subitem2:
			// データベースから削除する処理　を追加する必要あり

			Log.d("test", "click on delete");

			//DBに接続
			Memo memo = new Memo(ShowActivity.this);
			memo.open();

			//端末からデータを削除する
			File file = new File(Uri.parse(row.getImagePath()).getPath());
//			boolean fa = file.canWrite();
//			boolean imageDeleteSuccess = file.delete();
//			Log.d("pasutes", fa ? "true":"false");
//			Log.d("pasutes", row.getImagePath());
//			Log.d("pasutes", file.toString());

			// データ削除
			memo.delete(row);
			if(!file.exists()) {
				Toast.makeText(this, "削除されました\n既にファイルが存在しませんでした。", Toast.LENGTH_SHORT * 10).show();
			} else if(file.delete()){
				Toast.makeText(this, "削除されました", Toast.LENGTH_SHORT * 10).show();
			}else{
				Toast.makeText(this, "削除されました\n画像のみ削除に失敗しました\nギャラリーから削除してください", Toast.LENGTH_SHORT * 10).show();
			}

			//DB切断
			memo.close();

			finish();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
