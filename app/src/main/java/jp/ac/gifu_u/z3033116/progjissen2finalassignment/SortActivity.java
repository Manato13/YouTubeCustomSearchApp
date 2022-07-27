package jp.ac.gifu_u.z3033116.progjissen2finalassignment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import java.util.Locale;


public class SortActivity extends Fragment {

    FragmentManager manager;
    SharedVIewModel sharedViewModel;
    private View view;

    private final int CREATE_DOCUMENT_REQUEST = 1000;

    //動画を並べ替えるクラス
    SortVideos sortVideos;
    //本画面のクラス
    StartMenu startMenu;

    //ソートの種類を管理する変数(初期値は投稿日時順)
    private int SelectSortMode = 1;
    //ソートを表示する順番を管理する変数(初期値は降順)
    private boolean AsDes = false;

    //Spinner(ソートの選択肢に使う)
    private final String[] spinnerSortType = {"投稿日時", "再生回数", "高評価数", "コメント数", "隠れた名作", "ユーザーカスタム"};
    private final String[] spinnerSortAscendDescend = {"昇順", "降順"};

    //テスト用
    private final String[] dataset = new String[20];

    //フラグメントはコンストラクタ部を空っぽにする必要がある
    public SortActivity(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedVIewModel.class);

        view = inflater.inflate(R.layout.activity_sort,container,false);
        MainActivity activity = (MainActivity) getActivity();

        assert activity != null;

        //ボタン・テキストエディタなどの配置とIDの登録
        Button buttonSortVideos = view.findViewById(R.id.button_sortVideos);

        //ソートの種類を選択するプルダウンタブ
        // ArrayAdapter(選択肢を良い具合に並べて設定。)
        ArrayAdapter<String> adapterType = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item);
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //プルダウンメニューに要素を追加する
        for(String sortType: spinnerSortType){
            adapterType.add(sortType);
        }
        Spinner spinnerSortType = view.findViewById(R.id.spinnerSortType);
        spinnerSortType.setAdapter(adapterType);

        //ソートの昇順・降順を選択するプルダウンタブ
        // ArrayAdapter(選択肢を良い具合に並べて設定。)
        ArrayAdapter<String> adapterAsDes = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item);
        adapterAsDes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //プルダウンメニューに要素を追加する
        for(String sortAsDes: spinnerSortAscendDescend){
            adapterAsDes.add(sortAsDes);
        }
        Spinner spinnerSortAsDes = view.findViewById(R.id.spinnerSortAsDes);
        spinnerSortAsDes.setAdapter(adapterAsDes);

        //リサイクルビューの設定
        RecyclerView recyclerView = view.findViewById(R.id.my_recycler_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        RecyclerView.LayoutManager rLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(rLayoutManager);
        int i = 0;
        while (i < 20) {
            dataset[i] = String.format(Locale.ENGLISH, "Data_0%d", i);
            i++;
        }

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(dataset);
        recyclerView.setAdapter(adapter);


        //ソートの種類を選択するプルタブ
        spinnerSortType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //アイテムが選択された時
            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int position, long id) {
                Spinner spinner = (Spinner)parent;
                String item = (String)spinner.getSelectedItem();
                //選ばれたソートの種類をSelectSortModeに渡す
                switch(item){
                    case "投稿日時":
                        SelectSortMode = 1;
                        break;
                    case "再生回数":
                        SelectSortMode = 3;
                        break;
                    case "高評価数":
                        SelectSortMode = 4;
                        break;
                    case "コメント数":
                        SelectSortMode = 5;
                        break;
                    case "隠れた名作":
                        SelectSortMode = 7;
                        break;
                    case "ユーザーカスタム":
                        SelectSortMode = 9;
                }
                //動画をソートする
                //sortYoutubeVideos();
            }
            //　アイテムが選択されなかった
            public void onNothingSelected(AdapterView<?> parent) {
                //特に処理なし
            }
        });


        //ソートの昇順・降順を選択するプルタブ
        spinnerSortAsDes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //アイテムが選択された時
            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int position, long id) {
                Spinner spinner = (Spinner)parent;
                String item = (String)spinner.getSelectedItem();
                //選ばれたソートの種類をSelectSortModeに渡す
                switch(item){
                    case "昇順":
                        AsDes = true;
                        break;
                    case "降順":
                        AsDes = false;
                }
                //動画をソートする
                //sortYoutubeVideos();
            }
            //　アイテムが選択されなかった
            public void onNothingSelected(AdapterView<?> parent) {
                //特に処理なし
            }
        });

        //動画を並べ替えるボタン
        buttonSortVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(StartMenu.youtubeDataArray.size() != 0){
                    //動画の並べ替えを実行する
                    sortYoutubeVideos();
                }
                else{
                    showToast("並べ替えるデータが存在しません");
                }

            }
        });
        return view;
    }

    //メソッド部
    public void sortYoutubeVideos(){
        //SortVideosクラスを呼び出して並べ替えを実行する。
        sortVideos = new SortVideos();
        //selectMode一覧：1.投稿日時、3.再生回数、4.高評価数、5.コメント数、7.隠れた名作
        //sortがtrueならば昇順、falseなら降順
        sortVideos.sortMethod(AsDes,sortVideos.preForSort(SelectSortMode,StartMenu.youtubeDataArray),StartMenu.youtubeDataArray);
    }

    //トーストはこの関数を使って表示する
    private void showToast(String message){
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }


}
