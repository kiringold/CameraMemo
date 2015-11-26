package com.topia.imagememo;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;

import com.topia.imagememo.db.Memo;
import com.topia.imagememo.db.MemoRow;
import com.topia.imagememo.image.BitmapResizer;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


public class RegisterActivity extends Activity {
	private static final String TAG = RegisterActivity.class.getSimpleName();

	//写真撮影用
	private ImageView imageView;
	private static final int CAPTURE_IMAGE_CAPTURE_CODE = 0;
	private ImageButton ib;
	//registerやeditに画像保存先を渡す用
	private File send_from; // カメラインテント保存先候補(キャッシュ)
	private File send_to;   // カメラインテント保存先候補(正式)

	private File set_from; // カメラインテント保存先(キャッシュ)
	private File set_to;   // カメラインテント保存先(正式)

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		//写真表示用
		imageView = (ImageView) findViewById(R.id.imageView1);
		imageView.setVisibility(View.GONE);

		//写真撮影用ボタンクリックリスナー
		ib = (ImageButton) findViewById(R.id.imageButton1);
		ib.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				//Intentの取得
				Intent cameraIntent = getIntent();
				Uri uri = null;
				if (Intent.ACTION_SEND.equals(cameraIntent.getAction())) {
					uri = cameraIntent.getParcelableExtra(Intent.EXTRA_STREAM);
				}


				//保存名に日時を使うための処理
				final Date date = new Date(System.currentTimeMillis());
				final SimpleDateFormat dataFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
				final String filename = dataFormat.format(date) + ".jpg";
				//写真保存先の指定　保存先はmnt->sdcard->DCIM->Camera->
				send_from   = new File(RegisterActivity.this.getExternalCacheDir().toString()
						+ "/", filename);

				send_to = new File(Environment.getExternalStorageDirectory().toString()
						+ "/DCIM/Camera", filename);

				Log.d(TAG, "from => " + send_from.getPath() );
				Log.d(TAG, "  to => " + send_to.getPath() );
				i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(send_from));
				startActivityForResult(i, CAPTURE_IMAGE_CAPTURE_CODE);

			}
		});

		//登録ボタンクリックリスナー
		Button button = (Button)findViewById(R.id.bt_01);
		button.setOnClickListener(new ButtonClickListener());
	}

	//クリックリスナー定義
	class ButtonClickListener implements OnClickListener{
		//onClickメソッド（ボタンクリック時のイベントハンドラ）
		public void onClick(View view){
			//EditTextの入力情報の取得
			EditText input = (EditText)findViewById(R.id.editText01);
			String text = input.getText().toString();
			String error = "";

//			if(mySaveUri==null) error += "写真が入力されていません\n";
//			if(text.equals("")) error += "テキストが入力されていません\n";

			String karapic = "写真が入力されていません";
			String karamoji = "テキストが入力されていません";
			if(set_from==null){
				if(text.equals("")){
					error += karapic + "\n" + karamoji;
				}else{
				error += karapic;
				}
			}else{
				if(text.equals("")){
					error += karamoji;
				}
			}


			if(!error.equals("")) {
				Toast.makeText(RegisterActivity.this, error,
					Toast.LENGTH_LONG).show();
			} else{
				// ファイル移動
				set_from.renameTo(set_to);
				Log.d(TAG, "from => " + set_from.getPath() );
				Log.d(TAG, "  to => " + set_to.getPath() );

				// DBに入れるデータを用意
            	MemoRow test = new MemoRow();
            	test.setMemo(text);

            	Uri mySaveUri  = Uri.fromFile(set_to);
            	test.setImagePath(mySaveUri.toString());

            	//DBに接続
            	Memo memo = new Memo(RegisterActivity.this);
            	memo.open();

            	// データを登録
            	int id =memo.insert(test);

            	//DB切断
            	memo.close();

//            	データベースにあるか確認するコード
//            	Log.d(TAG, "click on insert");
//            	Log.d(TAG, mySaveUri.toString());
//            	Log.d(TAG, test.getImagePath());

				Intent intent = new Intent(RegisterActivity.this , ShowActivity.class);
				intent.putExtra("id", id);
				Log.d(TAG, ""+ id);
				Log.d(TAG, ""+ set_to.toString());

				startActivity(intent);

				finish();

				Toast.makeText(RegisterActivity.this, "登録しました", Toast.LENGTH_SHORT * 10).show();

			}
		}
	}

	//カメラが撮影した後の処理
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (resultCode == Activity.RESULT_OK  && requestCode == CAPTURE_IMAGE_CAPTURE_CODE) {
	    	set_from = send_from;
			set_to = send_to;
	    	BitmapResizer bitmapResizer = new BitmapResizer(RegisterActivity.this);
			try {
				Bitmap bitmap = bitmapResizer.resize(Uri.fromFile(set_from), 300, 300);
				imageView.setImageBitmap(null);
				imageView.setImageBitmap(bitmap);
		        imageView.setVisibility(View.VISIBLE);
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
	}
}
