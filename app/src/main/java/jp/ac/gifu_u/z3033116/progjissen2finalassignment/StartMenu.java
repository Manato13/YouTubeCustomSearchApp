package jp.ac.gifu_u.z3033116.progjissen2finalassignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Objects;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.Button;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.EditText;

import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;

public class StartMenu extends Fragment {
    //フィールド変数部
    private TextView textView;
    private EditText editText;
    //Spinner(ソートの選択肢に使う)
    private final String[] spinnerSortType = {"投稿日時", "再生回数", "高評価数", "コメント数", "隠れた名作", "ユーザーカスタム"};
    private final String[] spinnerSortAscendDescend = {"昇順", "降順"};
    private RequestQueue requestQueue;
    //APIKey(ここで確認　https://console.cloud.google.com/apis/dashboard?project=red-function-354800)
    //static private final String API_KEY = "AIzaSyAP-mnDKDFJLJ8hxPkJzIQN5hvTgctBne8";
    static private final String API_KEY = "AIzaSyBIF6ehSgidGb4Q9Md64N4dfwp779dQpiI"; //sub
    //チャンネルのURL、カスタムされていないID、名前を格納する
    public String Channel_URL, tmpChannel_URL, Channel_ID, Channel_Name;
    public static String Max_Results = "8";
    //取得した動画のIDをまとめて格納する
    private String Video_ID;
    //取得した動画の情報を保存する二次元ArrayList配列
    public static ArrayList<ArrayList<String>> youtubeDataArray = new ArrayList<ArrayList<String>>();
    //取得した動画のサムネイル情報を保存するArrayList配列
    public static ArrayList<String> thumbnailArray = new ArrayList<String>();
    //取得した動画の合計数
    public int videoSum = 0;
    //csvファイルを出力するクラス
    ExportCsv exportCsv;
    //csvファイルに書き込むための文字列を保存する変数
    public String csvToShare;
    //動画を並べ替えるクラス
    //SortVideos sortVideos;
    //ループした回数
    private int loop = 0;
    //出力するファイルの名前
    private String fileName;
    //ソートの種類を管理する変数(初期値は投稿日時順)
    private int SelectSortMode = 1;
    //ソートを表示する順番を管理する変数(初期値は降順)
    private boolean AsDes = false;
    FragmentManager manager;
    SharedVIewModel sharedViewModel;
    private View view;
    //ロード画面の表示
    ProgressDialog progressDialog;

    private final int CREATE_DOCUMENT_REQUEST = 1000;

    //フラグメントはコンストラクタ部を空っぽにする必要がある
    public StartMenu(){}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedVIewModel.class);

        view = inflater.inflate(R.layout.start_menu,container,false);
        MainActivity activity = (MainActivity) getActivity();

        assert activity != null;


        textView = view.findViewById(R.id.text_view_result);
        textView.setTextColor(Color.RED);
        //ボタン・テキストエディタなどの配置とIDの登録
        Button buttonSearch = view.findViewById(R.id.button_search);
        Button buttonSort = view.findViewById(R.id.button_sort);
        Toolbar toolbar = view.findViewById(R.id.id_toolbar);
        editText = view.findViewById(R.id.editText_channelId);
        Button csvOutputBtn = (Button) view.findViewById(R.id.button_csv);
        Button buttonTrans = view.findViewById(R.id.button_trans);



        /*
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

         */



        requestQueue = Volley.newRequestQueue(requireContext());

/*
        //ソートの種類を選択するプルタブ
        spinnerSortType.setOnItemSelectedListener(new OnItemSelectedListener() {
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
                //textView.setText(item);
            }
            //　アイテムが選択されなかった
            public void onNothingSelected(AdapterView<?> parent) {
                //特に処理なし
            }
        });


        //ソートの昇順・降順を選択するプルタブ
        spinnerSortAsDes.setOnItemSelectedListener(new OnItemSelectedListener() {
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
        */

        //並べ替えのフラグメントへ遷移する
        buttonTrans.setOnClickListener(v -> {
            manager = getParentFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.frame_layout_activity_layout, new SortActivity());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        if(savedInstanceState == null){
            // lambda
            //動画を並べ替える画面遷移を行う
            buttonSort.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //配列が空っぽでなかったら
                    if(youtubeDataArray.size() != 0){
                        //フラグメントを遷移させる
                        FragmentManager manager = getParentFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.replace(R.id.frame_layout_activity_layout, new SortFragment());
                        transaction.addToBackStack(null);
                        transaction.commit();

                    }
                    //配列に何もデータが無かったら
                    else{
                        showToast("URLを入力しsearchを行った後、再度実行してください。");
                    }
                }
            });

        }



        //APIを使用を開始するボタン
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 入力したURLを使ってAPIを使用しカスタムされていないチャンネルIDを取得
                Channel_URL = editText.getText().toString();
                if(Channel_URL != "" && Channel_URL != tmpChannel_URL){
                    //処理が終わるまでロード画面を表示する
                    progressDialog = new ProgressDialog(getContext());
                    progressDialog.setTitle("Search");
                    progressDialog.setMessage("チャンネルの動画情報を検索しています...");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.show();

                    tmpChannel_URL = Channel_URL;
                    jsonFindChannelID();
                }
                else{
                    if(Channel_URL == ""){
                        showToast("チャンネルのURLを入力してください。");
                    }
                    else if(Channel_URL == tmpChannel_URL){
                        showToast("既にこのチャンネルの検索結果を取得しています。");
                    }
                }
            }
        });

        //csvファイルを作成し、Storage Access Frameworkを起動するボタン
        csvOutputBtn.setOnClickListener( v ->createFile());




    return view;
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.menu, menu);



        MenuItem.OnActionExpandListener onActionExpandListener = new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                return true;
            }

        };

        return true;
    }

 */

    //Youtube Data API V3(search)を使用して入力されたチャンネルURLから、カスタムされていない「元のチャンネルID」とチャンネル名を取得する。
    private void jsonFindChannelID() {
        String url = "https://www.googleapis.com/youtube/v3/search?fields=items/snippet/channelId,items/snippet/title&part=snippet&maxResults=1&type=channel&q=" + Channel_URL + "&key=" + API_KEY;
        //参考↓
        //https://www.google.com/search?q=android+studio+AcyncLoader&rlz=1C1FQRR_jaJP977JP977&sxsrf=ALiCzsZYwBa_35HQEyBgshbhEWyVxDMTOw%3A1658145621294&ei=VUvVYs2hEe3s2roPsqGhyAU&ved=0ahUKEwiNoPjlsYL5AhVttlYBHbJQCFkQ4dUDCA4&uact=5&oq=android+studio+AcyncLoader&gs_lcp=Cgdnd3Mtd2l6EAM6BwgAEEcQsAM6CwgAEIAEEAQQJRAgOgQIIxAnOgUIABCABEoECEEYAEoECEYYAFD1BVj-DmDgEGgBcAB4AIABgwGIAYMEkgEDMy4ymAEAoAEBoAECyAEKwAEB&sclient=gws-wiz
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArrayItems = response.getJSONArray("items");
                    //json形式のデータから必要なものを取り出す。
                    JSONObject jsonObjectItems = jsonArrayItems.getJSONObject(0);
                    //snippetの中身
                    String snippet = jsonObjectItems.getString("snippet");
                    //チャンネルの元のID
                    String channelId = jsonObjectItems.getJSONObject("snippet").getString("channelId");
                    //チャンネル名
                    String title = jsonObjectItems.getJSONObject("snippet").getString("title");
                    //取得したデータを保存する
                    Channel_ID = channelId;
                    Channel_Name = title;
                    System.out.println(Channel_ID + "と" + Channel_Name);
                    //数珠つなぎで次のAPI(Search)を使用する関数を指定する。
                    jsonSearch();
                } catch (JSONException e) {
                    e.printStackTrace();
                    //ロード画面の終了
                    progressDialog.dismiss();
                    showToast("動画情報の取得に失敗しました");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(request);
    }

    //Youtube Data API V3(search)を使用して指定のチャンネルからアップロードされている動画のIDをリクエストし、レスポンスとして入手できるJson形式のデータをStringとして抜き出す。
    private void jsonSearch() {
        String url = "https://www.googleapis.com/youtube/v3/search?fields=items/id/videoId,items/snippet/publishedAt,items/snippet/title,items/snippet/thumbnails/default/url&order=viewCount&part=id,snippet&maxResults=" + Max_Results + "&channelId=" + Channel_ID + "&key=" + API_KEY;
        //System.out.println(url);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArrayItems = response.getJSONArray("items");

                    for (int i = 0; i < jsonArrayItems.length(); i++) {
                        //json形式のデータから必要なものを取り出す。
                        JSONObject jsonObjectItems = jsonArrayItems.getJSONObject(i);
                        //snippetの中身
                        String snippet = jsonObjectItems.getString("snippet");
                        //投稿日時
                        String publishedAt = jsonObjectItems.getJSONObject("snippet").getString("publishedAt");
                        //動画タイトル
                        String title = jsonObjectItems.getJSONObject("snippet").getString("title");
                        //サムネイルURL
                        String thumbnail = jsonObjectItems.getJSONObject("snippet").getJSONObject("thumbnails").getJSONObject("default").getString("url");
                        //idの中身
                        String id = jsonObjectItems.getString("id");
                        //その動画の固有ID
                        String videoId = jsonObjectItems.getJSONObject("id").getString("videoId");
                        //参考(https://ja.stackoverflow.com/questions/47502/%E3%83%8D%E3%82%B9%E3%83%88%E3%81%95%E3%82%8C%E3%81%A6%E3%82%8Bjson%E3%81%AE%E3%83%87%E3%83%BC%E3%82%BF%E3%82%92%E5%8F%96%E5%BE%97%E3%81%97%E3%81%9F%E3%81%84)

                        //二次元ArrayListの要素になるArrayListを作る
                        ArrayList<String> tmpArray = new ArrayList<>();
                        //取り出したデータをArrayListの中に格納する。
                        tmpArray.add(title);
                        tmpArray.add(publishedAt);
                        tmpArray.add(videoId);
                        youtubeDataArray.add(tmpArray);

                        //サムネイルURLは別の配列に保存する
                        thumbnailArray.add(thumbnail);
                    }
                    //取得した動画の本数をカウント
                    videoSum = youtubeDataArray.size();
                    //数珠つなぎで次に使用する関数(動画IDの作成)を指定する。
                    makeVideoId();
                } catch (JSONException e) {
                    e.printStackTrace();
                    //ロード画面の終了
                    progressDialog.dismiss();
                    showToast("動画情報を取得に失敗しました");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(request);
    }

    public void makeVideoId(){
        //Video_IDの初期化
        Video_ID = "";
        //APIのリクエストURLに使うVideo_IDを作成する。
        for (int m=0; m<videoSum; m++){
            //リクエストURLのidのフォーマットが"id001,id002,id003"なので条件分岐をする。
            if(m == videoSum-1) {
                Video_ID = Video_ID + youtubeDataArray.get(m).get(2);
            }
            else{
                Video_ID = Video_ID + youtubeDataArray.get(m).get(2) + ",";
            }
        }
        videoSum = 0; //初期化しておく
        //数珠つなぎで次のAPI(Videos)を使用する関数を指定する。
        jsonVideo();
    }




    //Youtube Data API V3(videos)を使用して先ほど取得した動画のIDを元にそれらの詳細なデータをリクエストし、レスポンスとして入手できるJson形式のデータをStringとして抜き出す。
    private void jsonVideo() {
        String url = "https://www.googleapis.com/youtube/v3/videos?part=statistics&fields=items/statistics&maxResults=" + Max_Results + "&id=" + Video_ID + "&key=" + API_KEY;
        //System.out.println(url);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArrayItems = response.getJSONArray("items");
                    for (int i = 0; i < jsonArrayItems.length(); i++) {

                        //json形式のデータから必要なものを取り出す。
                        JSONObject jsonObjectItems = jsonArrayItems.getJSONObject(i);
                        //statisticsの中身
                        String statistics = jsonObjectItems.getString("statistics");
                        //再生回数
                        String viewCount = jsonObjectItems.getJSONObject("statistics").getString("viewCount");
                        //高評価数
                        String likeCount = jsonObjectItems.getJSONObject("statistics").getString("likeCount");
                        //コメント数
                        String commentCount = jsonObjectItems.getJSONObject("statistics").getString("commentCount");

                        //取り出したデータをArrayListの中に格納する。
                        youtubeDataArray.get(i).add(viewCount);
                        youtubeDataArray.get(i).add(likeCount);
                        youtubeDataArray.get(i).add(commentCount);
                        //動画を識別するための固有のインデックスを追加する。
                        youtubeDataArray.get(i).add("" + (100*loop+i));
                        //tmpArray.clear();
                    }
                    Log.i("test","jsonVideo完了時点で取得したデータ " + youtubeDataArray + "\n" );
                    Log.i("test","jsonVideo完了時点で取得したサムネイル"  + thumbnailArray + "\n" );
                    showToast("動画情報を取得しました！");
                } catch (JSONException e) {
                    showToast("動画情報を取得に失敗しました");
                    e.printStackTrace();
                }
                //ロード画面の終了
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(request);
    }

    /*
    //共有画面を開くメソッド
    private void shareCsv(){
        //csvファイルを送信するインテント
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        //MIMEデータタイプの指定
        shareIntent.setType("text/csv");
        //メールでも送信できるようにする
        shareIntent.putExtra(Intent.EXTRA_SUBJECT,"Subject Here");
        //shareIntent.putExtra(Intent.EXTRA_TEXT,csvToShare);
        //shareIntent.putExtra(Intent.EXTRA_STREAM,csvUri);
        //共有するファイルのディレクトリ指定
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(csvUri)) ;
        shareIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "share file with"));
    }
     */



    //トーストはこの関数を使って表示する
    private void showToast(String message){
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    //ファイルを生成する関数
    private void createFile() {
        String fileName = Channel_Name + ".csv";
        //ファイルを保存するディレクトリをユーザーが選択する標準エクスプローラー(Storage Access Framework)の準備
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        //MIMEタイプの指定
        intent.setType("text/csv");
        intent.putExtra(Intent.EXTRA_TITLE, fileName);
        resultLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent resultData  = result.getData();
                    if (resultData  != null) {
                        Uri uri = resultData.getData();

                        csvToShare =  ExportCsv.ConvertARtoST(youtubeDataArray);
                        if(TextUtils.isEmpty(csvToShare)){
                            showToast("書き込むデータが存在しません");
                        }
                        else {Context applicationContext = MainActivity.getContextOfApplication();

                            try (OutputStream outputStream =
                                         applicationContext.getContentResolver().openOutputStream(uri)) {
                                if (outputStream != null) {
                                    outputStream.write(csvToShare.getBytes());
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });



}







