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
				// DB�ɓ����f�[�^��p��
				MemoRow test = new MemoRow();
				test.setMemo("meeeemo");
				test.setImagePath("paaaath");

				//DB�ɐڑ�
				Memo memo = new Memo(DbsampleActivity.this);
				memo.open();

				// �f�[�^��o�^
				memo.insert(test);

				//DB�ؒf
				memo.close();

				Log.d("test", "click on insert");
			}
		});

		Button btn_update = (Button)findViewById(R.id.update);
		btn_update.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.d("test", "click on update");
				// �X�V����f�[�^��p��
				MemoRow test = new MemoRow(1);
				test.setMemo("meeeemo");
				test.setImagePath("paaaath");

				//DB�ɐڑ�
				Memo memo = new Memo(DbsampleActivity.this);
				memo.open();

				// �f�[�^��ύX
				memo.update(test);

				//DB�ؒf
				memo.close();
			}
		});

		Button btn_delete = (Button)findViewById(R.id.delete);
		btn_delete.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.d("test", "click on delete");
				// �폜����f�[�^��p��
				MemoRow test = new MemoRow(1);
				test.setMemo("meeeemo");
				test.setImagePath("paaaath");

				//DB�ɐڑ�
				Memo memo = new Memo(DbsampleActivity.this);
				memo.open();

				// �f�[�^�폜
				memo.delete(test);

				//DB�ؒf
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
		//DB�ɐڑ�
		Memo memo = new Memo(this);
		memo.open();

		// �S���擾
		ArrayList<MemoRow> memos = memo.find_all();

		// �擾�����f�[�^��\��
		for(int i=0;i<memos.size();i++){
			MemoRow row = (MemoRow)memos.get(i);
			Log.d("test", row.to_s());
		}

		//DB�ؒf
		memo.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dbsample, menu);
		return true;
	}

}
