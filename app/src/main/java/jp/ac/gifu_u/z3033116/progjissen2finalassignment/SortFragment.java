package jp.ac.gifu_u.z3033116.progjissen2finalassignment;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SortFragment extends Fragment {

    SortFragment(){}

    public static SortFragment newInstance(String str){
        // Fragemnt01 インスタンス生成
        SortFragment fragment = new SortFragment ();
        // Bundle にパラメータを設定
        Bundle barg = new Bundle();
        barg.putString("Message", str);
        fragment.setArguments(barg);

        return fragment;
    }

    // FragmentのViewを生成して返す
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_sort,
                container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        if(args != null ){
            String str = args.getString("Message");
            TextView textView = view.findViewById(R.id.text_view_title);
            textView.setText(str);
        }
    }
}