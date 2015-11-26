package com.topia.imagememo.layout;

import java.io.IOException;
import java.util.List;

import com.topia.imagememo.ShowActivity;
import com.topia.imagememo.R;
import com.topia.imagememo.db.MemoRow;
import com.topia.imagememo.image.BitmapResizer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MemoRowAdapter extends ArrayAdapter<MemoRow> {
	private LayoutInflater inflater;

	private Context mcontext;
	private MemoRow item;

	public MemoRowAdapter(Context context, int textViewResourceId,
			List<MemoRow> objects) {
		super(context, textViewResourceId, objects);
		mcontext = context;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	public View getView(int position, View convertView, ViewGroup parent) {
		Log.d("test", "getView " + position);
		// 特定の行(position)のデータを得る
		item = (MemoRow) getItem(position);

		// convertViewは使い回しされている可能性があるのでnullの時だけ新しく作る
		if (null == convertView) {
			// convertView = inflater.inflate(R.layout.row_layout, null);
			convertView = inflater.inflate(R.layout.list_row, parent, false);
		}

		// CustomDataのデータをViewの各Widgetにセットする
		// 写真表示
		ImageView imageView = (ImageView)convertView.findViewById(R.id.imageView1);

		if(item.getImagePath()!=null){
			//imageView.setImageURI(Uri.parse(item.getImagePath()));
			BitmapResizer bitmapResizer = new BitmapResizer(mcontext);
			Bitmap bitmap;
			try {
				bitmap = bitmapResizer.resize(Uri.parse(item.getImagePath()),80,80);
				imageView.setImageBitmap(bitmap);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			//デバッグ用に仮のデータが入っていた場合の処理。　仮のデータを入れなくなったタイミングでifの中の処理のみ残して削除して大丈夫
			imageView.setImageResource(R.drawable.black_frame);
		}

		TextView memo = (TextView) convertView.findViewById(R.id.memonaiyou1);
		memo.setText(truncate(item.getMemo(), 10, " ...") );

		// TextView created_at =
		// (TextView)convertView.findViewById(R.id.created_at);
		// created_at.setText(item.getCreatedAt());

		TextView created_at = (TextView) convertView
				.findViewById(R.id.memodata);
		created_at.setText(item.getCreatedAt());

		convertView.findViewById(R.id.row).setTag(item.getId());

		// 　タップ時に詳細ページに遷移
		convertView.findViewById(R.id.row).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(mcontext, ShowActivity.class);
						int id = (Integer) v.getTag();
						intent.putExtra("id", id);
						v.getContext().startActivity(intent);
					}
				});
		return convertView;
	}

	public String truncate(String base, int limit, String end) {
		base = base.replaceAll("\n", " ");
		if(base.length() <= limit) return base;
		return base.substring(0, limit) + end;
	}
}
