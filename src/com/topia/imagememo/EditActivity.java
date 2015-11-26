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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class EditActivity extends Activity {
	private MemoRow row;

	//registerActivity����R�s�y
	private ImageView imageView;
	private static final int CAPTURE_IMAGE_CAPTURE_CODE = 0;

	protected static final String TAG = EditActivity.class.getSimpleName();;
	private ImageButton ib;
	private File send_from; // �J�����C���e���g�ۑ�����(�L���b�V��)
	private File send_to;   // �J�����C���e���g�ۑ�����(����)
	private File set_from; // �J�����C���e���g�ۑ���(�L���b�V��)
	private File set_to;   // �J�����C���e���g�ۑ���(����)

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit);

		//registerActivity����قڃR�s�y
		//�ʐ^�\���p
		imageView = (ImageView) findViewById(R.id.imageView1);
		//�ʐ^�B�e�p�{�^���N���b�N���X�i�[
		ib = (ImageButton) findViewById(R.id.imageButton1);
		ib.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				//�ۑ����ɓ������g�����߂̏���
				final Date date = new Date(System.currentTimeMillis());
				final SimpleDateFormat dataFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
				final String filename = dataFormat.format(date) + ".jpg";
				//�ʐ^�ۑ���̎w��@�ۑ����mnt->sdcard->DCIM->Camera->

				send_from   = new File(EditActivity.this.getExternalCacheDir().toString()
						+ "/", filename);
				send_to = new File(Environment.getExternalStorageDirectory().toString()
						+ "/DCIM/Camera", filename);
				Log.d(TAG, "from => " + send_from.getPath() );
				Log.d(TAG, "  to => " + send_to.getPath() );
				i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(send_from));
				startActivityForResult(i, CAPTURE_IMAGE_CAPTURE_CODE);
			}
		});

		Intent i = getIntent();
		int id = i.getIntExtra("id", 0);

		Memo memo = new Memo(this);
		memo.open();

		// �擾
		row = memo.find(id);

		//DB�ؒf
		memo.close();

		//�ύX�O�̃f�[�^�\���p

		((EditText)findViewById(R.id.memo)).setText(row.getMemo());
		((TextView)findViewById(R.id.created_at)).setText(row.getCreatedAt());

		ImageView imageView = (ImageView)findViewById(R.id.imageView1);
		BitmapResizer bitmapResizer = new BitmapResizer(EditActivity.this);
		try {
			Bitmap bitmap = bitmapResizer.resize(Uri.parse(row.getImagePath()), 300, 300);
			imageView.setImageBitmap(null);
			imageView.setImageBitmap(bitmap);
	        imageView.setVisibility(View.VISIBLE);
		} catch (IOException e) {
			e.printStackTrace();
		}

		//�ۑ��{�^���̃N���b�N���X�i�[�̒�`
		Button btnDisp = (Button)findViewById(R.id.bt_01);
		btnDisp.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				EditText input = (EditText)findViewById(R.id.memo);
				String text = input.getText().toString();

				if(text.equals("")){
					Toast.makeText(EditActivity.this, "�e�L�X�g�����͂���Ă��܂���",
							Toast.LENGTH_LONG).show();
				}else{
					Log.d("test", "click on update");
					// �X�V����f�[�^��p��
					row.setMemo(text);

					if(set_to != null) {
						Uri mySaveUri  = Uri.fromFile(set_to);
						row.setImagePath(mySaveUri.toString());
						if(set_from != null) {
							Log.d(TAG, "from => " + set_from.getPath() );
							set_from.renameTo(set_to); // �t�@�C���ړ�
						}
						Log.d(TAG, "  to => " + set_to.getPath() );
					}

					//DB�ɐڑ�
					Memo memo = new Memo(EditActivity.this);
					memo.open();

					// �f�[�^��ύX
					memo.update(row);

					//DB�ؒf
					memo.close();

					Intent intent = new Intent(EditActivity.this , ShowActivity.class);
					intent.putExtra("id", row.getId());
					startActivity(intent);

					Toast.makeText(EditActivity.this,"�ύX����܂���",Toast.LENGTH_SHORT * 10).show();

					finish();

				}
			}
		});
	}

	//register����R�s�y
	//�J�������B�e������̏���
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK  && requestCode == CAPTURE_IMAGE_CAPTURE_CODE) {

			set_from = send_from;
			set_to = send_to;
			BitmapResizer bitmapResizer = new BitmapResizer(EditActivity.this);
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



	//	public boolean onCreateOptionsMenu(Menu menu) {
	//		// Inflate the menu; this adds items to the action bar if it is present.
	//		getMenuInflater().inflate(R.menu.edit, menu);
	//		return true;
	//	}

}
