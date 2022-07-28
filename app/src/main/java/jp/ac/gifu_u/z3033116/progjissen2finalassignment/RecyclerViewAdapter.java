package jp.ac.gifu_u.z3033116.progjissen2finalassignment;

import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    //リストのデータを保持する変数

    private final ArrayList<String> iTitle;// = new ArrayList<String>();
    private final ArrayList<String> iViewC;// = new ArrayList<String>();
    private final ArrayList<String> iLikeC;// = new ArrayList<String>();
    private final ArrayList<String> iCommentC;// = new ArrayList<String>();

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    static class ViewHolder extends RecyclerView.ViewHolder {

        //ImageView imageView;
        TextView titleView;
        TextView viewCView;
        TextView likeCView;
        TextView commentCView;

        public ViewHolder(View view) {
            super(view);

            titleView = view.findViewById(R.id.title_view);
            viewCView = view.findViewById(R.id.viewC_view);
            likeCView = view.findViewById(R.id.likeC_view);
            commentCView = view.findViewById(R.id.commentC_view);

            //imageView = view.findViewById(R.id.image_view);

        }

        public TextView getTextView(){
            return titleView;
        }

    }


//    public RecyclerViewAdapter(ArrayList<Integer> itemImages, ArrayList<String> itemNames, ArrayList<String> itemEmails) {
//        this.iImages = itemImages;
//        this.iNames = itemNames;
//        this.iEmails = itemEmails;
//    }

    public RecyclerViewAdapter(ArrayList<String> itemTitle,ArrayList<String> itemViewCount,ArrayList<String> itemLikeCount,ArrayList<String> itemCommentCount ) {

        this.iTitle = itemTitle;
        this.iViewC = itemViewCount;
        this.iLikeC = itemLikeCount;
        this.iCommentC = itemCommentCount;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        //onCreateViewHolder()ではリスト一行分のレイアウトViewを生成する
        //作成したViewはViewHolderに格納してViewHolderをreturnで返す
        //レイアウトXMLからViewを生成
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cell_view, viewGroup, false);

        RecyclerViewAdapter.ViewHolder holder = new RecyclerViewAdapter.ViewHolder(view);

        //クリックイベントを登録
        //参考：http://www.fineblue206.net/archives/602
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cellがクリックされた時の処理を書く
                int position = holder.getAdapterPosition();
                //Toast.makeText(v.getContext(), localDataSet[position], Toast.LENGTH_SHORT).show();
            }
        });

        return holder;
    }

    //onBindViewHolder()ではデータとレイアウトの紐づけを行なう
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        //viewHolder.imageView.setImageResource(iImages.get(position));
        viewHolder.titleView.setText(iTitle.get(position));
        viewHolder.viewCView.setText(iViewC.get(position));
        viewHolder.likeCView.setText(iLikeC.get(position));
        viewHolder.commentCView.setText(iCommentC.get(position));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return iTitle.size();
    }
}