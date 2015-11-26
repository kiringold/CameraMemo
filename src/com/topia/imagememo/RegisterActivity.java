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

	//�ʐ^�B�e�p
	private ImageView imageView;
	private static final int CAPTURE_IMAGE_CAPTURE_CODE = 0;
	private ImageButton ib;
	//register��edit�ɉ摜�ۑ����n���p
	private File send_from; // �J�����C���e���g�ۑ�����(�L���b�V��)
	private File send_to;   // �J�����C���e���g�ۑ�����(����)

	private File set_from; // �J�����C���e���g�ۑ���(�L���b�V��)
	private File set_to;   // �J�����C���e���g�ۑ���(����)

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		//�ʐ^�\���p
		imageView = (ImageView) findViewById(R.id.imageView1);
		imageView.setVisibility(View.GONE);

		//�ʐ^�B�e�p�{�^���N���b�N���X�i�[
		ib = (ImageButton) findViewById(R.id.imageButton1);
		ib.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				//Intent�̎擾
				Intent cameraIntent = getIntent();
				Uri uri = null;
				if (Intent.ACTION_SEND.equals(cameraIntent.getAction())) {
					uri = cameraIntent.getParcelableExtra(Intent.EXTRA_STREAM);
				}


				//�ۑ����ɓ������g�����߂̏���
				final Date date = new Date(System.currentTimeMillis());
				final SimpleDateFormat dataFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
				final String filename = dataFormat.format(date) + ".jpg";
				//�ʐ^�ۑ���̎w��@�ۑ����mnt->sdcard->DCIM->Camera->
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

		//�o�^�{�^���N���b�N���X�i�[
		Button button = (Button)findViewById(R.id.bt_01);
		button.setOnClickListener(new ButtonClickListener());
	}

	//�N���b�N���X�i�[��`
	class ButtonClickListener implements OnClickListener{
		//onClick���\�b�h�i�{�^���N���b�N���̃C�x���g�n���h���j
		public void onClick(View view){
			//EditText�̓��͏��̎擾
			EditText input = (EditText)findViewById(R.id.editText01);
			String text = input.getText().toString();
			String error = "";

//			if(mySaveUri==null) error += "�ʐ^�����͂���Ă��܂���\n";
//			if(text.equals("")) error += "�e�L�X�g�����͂���Ă��܂���\n";

			String karapic = "�ʐ^�����͂���Ă��܂���";
			String karamoji = "�e�L�X�g�����͂���Ă��܂���";
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
				// �t�@�C���ړ�
				set_from.renameTo(set_to);
				Log.d(TAG, "from => " + set_from.getPath() );
				Log.d(TAG, "  to => " + set_to.getPath() );

				// DB�ɓ����f�[�^��p��
            	MemoRow test = new MemoRow();
            	test.setMemo(text);

            	Uri mySaveUri  = Uri.fromFile(set_to);
            	test.setImagePath(mySaveUri.toString());

            	//DB�ɐڑ�
            	Memo memo = new Memo(RegisterActivity.this);
            	memo.open();

            	// �f�[�^��o�^
            	int id =memo.insert(test);

            	//DB�ؒf
            	memo.close();

//            	�f�[�^�x�[�X�ɂ��邩�m�F����R�[�h
//            	Log.d(TAG, "click on insert");
//            	Log.d(TAG, mySaveUri.toString());
//            	Log.d(TAG, test.getImagePath());

				Intent intent = new Intent(RegisterActivity.this , ShowActivity.class);
				intent.putExtra("id", id);
				Log.d(TAG, ""+ id);
				Log.d(TAG, ""+ set_to.toString());

				startActivity(intent);

				finish();

				Toast.makeText(RegisterActivity.this, "�o�^���܂���", Toast.LENGTH_SHORT * 10).show();

			}
		}
	}

	//�J�������B�e������̏���
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
