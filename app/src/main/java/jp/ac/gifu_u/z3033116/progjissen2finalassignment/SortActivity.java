package jp.ac.gifu_u.z3033116.progjissen2finalassignment;
import android.content.Intent;
import android.net.Uri;
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

import java.util.ArrayList;
import java.util.Locale;

//遷移先の並べ替え画面での処理を管理するクラス
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
        //コンテンツが変更されてもRecyclerViewのレイアウトサイズが変更されないことが分かっている場合、この設定を使用
        recyclerView.setHasFixedSize(true);
        //リニアレイアウトマネージャーを使用する
        RecyclerView.LayoutManager rLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(rLayoutManager);


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
                //もしデータ配列が空っぽでなかったら
                if(StartMenu.youtubeDataArray.size() != 0){
                    //リサイクルビューに渡す動画のタイトル
                    ArrayList<String> title = new ArrayList<String>();
                    //リサイクルビューに渡す動画の再生回数
                    ArrayList<String> viewCount = new ArrayList<String>();
                    //リサイクルビューに渡す動画の高評価数
                    ArrayList<String> likeCount = new ArrayList<String>();
                    //リサイクルビューに渡す動画のコメント数
                    ArrayList<String> commentCount = new ArrayList<String>();
                    //リサイクルビューに渡す動画のID
                    ArrayList<String> videoID = new ArrayList<String>();
                    //リサイクルビューにセットしたいデータが入っている配列を作成
                    for(int i = 0; i<StartMenu.youtubeDataArray.size(); i++){
                        //動画の並べ替えを実行し、その結果をアダプターに渡す
                        title.add(sortYoutubeVideos().get(i).get(0));
                        viewCount.add(sortYoutubeVideos().get(i).get(1));
                        likeCount.add(sortYoutubeVideos().get(i).get(2));
                        commentCount.add(sortYoutubeVideos().get(i).get(3));
                        videoID.add(sortYoutubeVideos().get(i).get(4));
                    }
                    //アダプターを使って要素を配置する
                    RecyclerViewAdapter adapter = new RecyclerViewAdapter(title,viewCount,likeCount,commentCount,videoID,getContext());
                    recyclerView.setAdapter(adapter);

                }
                else{
                    showToast("並べ替えるデータが存在しません");
                }

            }
        });
        return view;
    }

    //メソッド部
    //並べ替えを行いその結果を格納した二次元配列を返す関数
    public ArrayList<ArrayList<String>> sortYoutubeVideos(){
        //SortVideosクラスを呼び出して並べ替えを実行する。
        sortVideos = new SortVideos();
        //selectMode一覧：1.投稿日時、3.再生回数、4.高評価数、5.コメント数、7.隠れた名作
        //sortがtrueならば昇順、falseなら降順
        //sortedData.addAll(sortVideos.sortMethod(AsDes,sortVideos.preForSort(SelectSortMode,StartMenu.youtubeDataArray),StartMenu.youtubeDataArray));
        return sortVideos.sortMethod(AsDes,sortVideos.preForSort(SelectSortMode,StartMenu.youtubeDataArray),StartMenu.youtubeDataArray);
    }



    //トーストはこの関数を使って表示する
    private void showToast(String message){
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }


}
