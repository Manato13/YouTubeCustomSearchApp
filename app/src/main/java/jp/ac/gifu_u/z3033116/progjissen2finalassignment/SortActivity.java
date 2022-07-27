package jp.ac.gifu_u.z3033116.progjissen2finalassignment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SortActivity extends AppCompatActivity {
    //動画を並べ替えるクラス
    SortVideos sortVideos;
    //本画面のクラス
    MainActivity mainActivity;

    //ソートの種類を管理する変数(初期値は投稿日時順)
    private int SelectSortMode = 1;
    //ソートを表示する順番を管理する変数(初期値は降順)
    private boolean AsDes = false;

    //Spinner(ソートの選択肢に使う)
    private final String[] spinnerSortType = {"投稿日時", "再生回数", "高評価数", "コメント数", "隠れた名作", "ユーザーカスタム"};
    private final String[] spinnerSortAscendDescend = {"昇順", "降順"};

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        // layoutの.xmlを指定する
        setContentView(R.layout.activity_sort);
        //ボタン・テキストエディタなどの配置とIDの登録
        Button buttonSortVideos = findViewById(R.id.button_sortVideos);

        //ソートの種類を選択するプルダウンタブ
        // ArrayAdapter(選択肢を良い具合に並べて設定。)
        ArrayAdapter<String> adapterType = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //プルダウンメニューに要素を追加する
        for(String sortType: spinnerSortType){
            adapterType.add(sortType);
        }
        Spinner spinnerSortType = findViewById(R.id.spinnerSortType);
        spinnerSortType.setAdapter(adapterType);

        //ソートの昇順・降順を選択するプルダウンタブ
        // ArrayAdapter(選択肢を良い具合に並べて設定。)
        ArrayAdapter<String> adapterAsDes = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adapterAsDes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //プルダウンメニューに要素を追加する
        for(String sortAsDes: spinnerSortAscendDescend){
            adapterAsDes.add(sortAsDes);
        }
        Spinner spinnerSortAsDes = findViewById(R.id.spinnerSortAsDes);
        spinnerSortAsDes.setAdapter(adapterAsDes);


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
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


}
