package jp.ac.gifu_u.z3033116.progjissen2finalassignment;

import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import jp.ac.gifu_u.z3033116.progjissen2finalassignment.databinding.ActivityMainBinding;

//リサイクルビューでの処理を管理するクラス
//参考：https://akira-watson.com/android/recyclerview.html
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    //リストのデータを保持する変数
    private final ArrayList<String> iTitle;// = new ArrayList<String>();
    private final ArrayList<String> iViewC;// = new ArrayList<String>();
    private final ArrayList<String> iLikeC;// = new ArrayList<String>();
    private final ArrayList<String> iCommentC;// = new ArrayList<String>();
    private final ArrayList<String> iVideoID;// = new ArrayList<String>();
    //private final ArrayList<String> iImages;
    Context context;
    //URL先の画像をimageviewにはめ込むのに使う
    ActivityMainBinding binding;


    //ViewHolderを作成する
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleView;
        TextView viewCView;
        TextView likeCView;
        TextView commentCView;
        ImageView imageView;

        public ViewHolder(View view) {
            super(view);

            titleView = view.findViewById(R.id.title_view);
            viewCView = view.findViewById(R.id.viewC_view);
            //likeCView = view.findViewById(R.id.likeC_view);
            //commentCView = view.findViewById(R.id.commentC_view);
            imageView = view.findViewById(R.id.image_view);
        }
        public TextView getTextView() {
            return titleView;
        }
    }

    //リサイクルビューに値や画像をセットしてくれるアダプター
    public RecyclerViewAdapter(ArrayList<String> itemTitle, ArrayList<String> itemViewCount, ArrayList<String> itemLikeCount, ArrayList<String> itemCommentCount, ArrayList<String> itemVideoID,Context context) {
        this.iTitle = itemTitle;
        this.iViewC = itemViewCount;
        this.iLikeC = itemLikeCount;
        this.iCommentC = itemCommentCount;
        this.iVideoID = itemVideoID;
        this.context = context;
    }

    //ここで新しいview(Fragmentと同等の扱いとなる)を作成する
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        //onCreateViewHolder()ではリスト一行分のレイアウトViewを生成する
        //作成したViewはViewHolderに格納してViewHolderをreturnで返す
        //レイアウトXMLからViewを生成
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cell_view, viewGroup, false);

        RecyclerViewAdapter.ViewHolder holder = new RecyclerViewAdapter.ViewHolder(view);

//        LinearLayout my_recycler_view = R.findViewById(R.id.my_recycler_view);
//        binding = ActivityMainBinding.inflate(view.layout.cell_view, my_recycler_view);

        //クリックイベントを登録
        //参考：http://www.fineblue206.net/archives/602
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cellがクリックされた時の処理を書く
                int position = holder.getAdapterPosition();
                goToYoutube(iVideoID.get(position));
            }
        });

        return holder;
    }

    //onBindViewHolder()ではデータとレイアウトの紐づけを行なう
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        //この位置でデータセットから要素を取得し、ビューの内容をその要素に置き換える。
        //ビューの内容をその要素で置き換える。
        //viewHolder.imageView.setImageResource(iImages.get(position));
        viewHolder.titleView.setText(iTitle.get(position));
        viewHolder.viewCView.setText("再生回数："+iViewC.get(position)+",  高評価数："+iLikeC.get(position)+",  コメント数："+iCommentC.get(position));
        //viewHolder.likeCView.setText("高評価数："+iLikeC.get(position)+",");
        //viewHolder.commentCView.setText("コメント数："+iCommentC.get(position));
    }

    //データセットのサイズを返す (レイアウトマネージャによって呼び出される)
    @Override
    public int getItemCount() {
        return iTitle.size();
    }

    //cellをタップしたらURL先の動画に飛ぶ関数
    public void goToYoutube(String id) {
        Uri uri = Uri.parse("https://youtu.be/"+id);
        //標準で用意されているintentを使用する
        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
        context.startActivity(intent);
    }

}






