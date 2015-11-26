package com.topia.imagememo;

import java.util.List;

import com.topia.imagememo.db.Memo;
import com.topia.imagememo.db.MemoRow;
import com.topia.imagememo.layout.MemoRowAdapter;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

public class ListActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		Log.d("test", "ListActivity");
	}


	@Override
	protected void onResume() {
		super.onResume();
		setContentView(R.layout.activity_list);

		//DB�ɐڑ�
		Memo memo = new Memo(this);
		memo.open();

		// �S���擾
		List<MemoRow> objects = memo.find_all();

		//DB�ؒf
		memo.close();

		// �\��
		renderList(objects);


		//�ނ��߂��˃{�^���N���b�N���X�i�[��`
		OnClickListener buttonClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				String tag = (String)v.getTag();
				if (tag.equals("searchButton")){

					EditText editText = (EditText)findViewById(R.id.editText1);
					String text = editText.getText().toString();

					//DB�ɐڑ�
					Memo memo = new Memo(ListActivity.this);
					memo.open();
					List<MemoRow> objects;

					if(text.equals("")){
						// �S���擾
//						List<MemoRow> objects = memo.find_all();
						objects = memo.find_all();

					}else{
						// �e�L�X�g�����擾
//						List<MemoRow> objects = memo.find_search("text");
						objects = memo.find_search(text);
					}

					//DB�ؒf
					memo.close();

					// �\��
					renderList(objects);
				}
			}
		};


		// �ނ��߂��˃{�^���̃N���b�N���X�i�[�ݒ�
		ImageView searchButton = (ImageView)findViewById(R.id.imageView4);
		searchButton.setTag("searchButton");
		searchButton.setOnClickListener(buttonClickListener);

	}

	public void renderList(List<MemoRow> list) {
		MemoRowAdapter memoRowAdapter = new MemoRowAdapter(this, 0, list);

		ListView listView = (ListView)findViewById(R.id.memolist);
		listView.setAdapter(memoRowAdapter);
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		if(item.getItemId() == R.id.item1){
			Intent toRegister = new Intent(ListActivity.this, RegisterActivity.class);
			startActivity(toRegister);
		}
		return true;

	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.list, menu);
		return true;
	}


}
