package jp.ac.gifu_u.z3033116.progjissen2finalassignment;

import androidx.appcompat.widget.Toolbar;

import java.io.OutputStream;
import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

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

import android.view.View;

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
    public String Channel_URL, Channel_ID, Channel_Name;
    public static String Max_Results = "50";
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
    //フラグメントを使用するためのマネージャー
    FragmentManager manager;
    //UI 関連のデータを保存し管理するためのクラス
    SharedVIewModel sharedViewModel;
    private View view;
    //ロード画面の表示
    ProgressDialog progressDialog;

    //private final int CREATE_DOCUMENT_REQUEST = 1000;

    //フラグメントはコンストラクタ部を空っぽにする必要がある
    public StartMenu(){}


    //フラグメントでのコンストラクタ部(onCreate)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //UI 関連のデータを保存し管理するためのクラス
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedVIewModel.class);
        //start_menu.xmlを画面にはめ込む
        view = inflater.inflate(R.layout.start_menu,container,false);
        MainActivity activity = (MainActivity) getActivity();
        assert activity != null;

        //ボタン・テキストエディタなどの配置とIDの登録
        textView = view.findViewById(R.id.text_view_result);
        textView.setTextColor(Color.RED);
        Button buttonSearch = view.findViewById(R.id.button_search);
        Button buttonSort = view.findViewById(R.id.button_sort);
        Toolbar toolbar = view.findViewById(R.id.id_toolbar);
        editText = view.findViewById(R.id.editText_channelId);
        Button csvOutputBtn = (Button) view.findViewById(R.id.button_csv);
        requestQueue = Volley.newRequestQueue(requireContext());


        //動画並べ替えのフラグメントへ遷移するボタン
        buttonSort.setOnClickListener(v -> {
            manager = getParentFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.frame_layout_activity_layout, new SortActivity());
            transaction.addToBackStack(null);
            transaction.commit();
        });


        //APIを使用を開始するボタン
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    // 入力したURLを使ってAPIを使用しカスタムされていないチャンネルIDを取得
                    Channel_URL = editText.getText().toString();
                if(Channel_URL.length()>5){
                        //処理が終わるまでロード画面を表示する
                        progressDialog = new ProgressDialog(getContext());
                        progressDialog.setTitle("Search");
                        progressDialog.setMessage("チャンネルの動画情報を検索しています...");
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.show();
                        //APIを実行する
                        jsonFindChannelID();
                }
                else{
                        showToast("チャンネルのURLを入力してください。");
                }
            }
        });

        //csvファイルを作成し、Storage Access Frameworkを起動するボタン
        csvOutputBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(youtubeDataArray.size() != 0){
                    createFile();
                }
                else{
                    showToast("出力するデータがありません");
                }
            }
        });

    return view;
    }


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
                    showToast("動画情報の取得に失敗しました(Find Channel ID)");
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
        String url = "https://www.googleapis.com/youtube/v3/search?fields=items/id/videoId,items/snippet/publishedAt,items/snippet/title,items/snippet/thumbnails/default/url&order=date&part=id,snippet&maxResults=" + Max_Results + "&channelId=" + Channel_ID + "&key=" + API_KEY;
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
                    showToast("動画情報の取得に失敗しました(jsonSearch)");
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
                    showToast("動画情報の取得に失敗しました(jsonVideo)");
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
    //ActivityResultContractの実行プロセスを開始するためのランチャー
    //参考：https://akira-watson.com/android/action_create_document.html
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



}







