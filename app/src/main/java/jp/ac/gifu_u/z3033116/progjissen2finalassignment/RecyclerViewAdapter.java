package jp.ac.gifu_u.z3033116.progjissen2finalassignment;

import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private final String[] localDataSet;//リストのデータを保持する変数

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.text_view);
        }

        public TextView getTextView(){
            return textView;
        }

    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public RecyclerViewAdapter(String[] dataSet) {
        localDataSet = dataSet;
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
                Toast.makeText(v.getContext(), localDataSet[position], Toast.LENGTH_SHORT).show();
            }
        });

        return holder;
    }

    //onBindViewHolder()ではデータとレイアウトの紐づけを行なう
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getTextView().setText(localDataSet[position]);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.length;
    }
}