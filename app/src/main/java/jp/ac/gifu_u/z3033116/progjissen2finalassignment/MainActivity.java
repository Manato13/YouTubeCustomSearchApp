package jp.ac.gifu_u.z3033116.progjissen2finalassignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import java.util.Map;
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.Manifest;
import android.os.Build;
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
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    //フィールド変数部
    private TextView textView;
    private EditText editText;
    private RequestQueue requestQueue;
    //APIKey(ここで確認　https://console.cloud.google.com/apis/dashboard?project=red-function-354800)
    static private final String API_KEY = "AIzaSyAP-mnDKDFJLJ8hxPkJzIQN5hvTgctBne8";
    //制作過程で一時的に使用するパラメーター（完成版では任意に）
    public String Channel_ID = "UCpCesuCH4UxIcy65gSrC0Pw";
    public String Max_Results = "5";
    public String Video_ID;
    //取得した動画の情報を保存する二次元ArrayList配列
    public ArrayList<ArrayList<String>> youtubeDataArray = new ArrayList<ArrayList<String>>();
    //取得した動画の合計数
    public int videoSum = 0;
    //csvファイルを出力するクラス
    ExportCsv exportCsv;
    //ループした回数
    public int loop = 0;

    private final int REQUEST_CODE = 1000;

    //コンストラクタ部(=onCreate)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.text_view_result);
        textView.setTextColor(Color.RED);
        //ボタン・テキストエディタの配置とIDの登録
        Button buttonSearch = findViewById(R.id.button_search);
        Button buttonCsv = findViewById(R.id.button_csv);
        Toolbar toolbar = findViewById(R.id.id_toolbar);
        editText = findViewById(R.id.editText_channelId);

        setSupportActionBar(toolbar);

        requestQueue = Volley.newRequestQueue(this);

        //外部ストレージを使用するので権限のリクエストをする。
        // Android 6, API 23以上でパーミッションの確認
        if(Build.VERSION.SDK_INT >= 23) {
            String[] permissions = {
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
            };
            checkPermission(permissions, REQUEST_CODE);
        }


        //APIを使用を開始するボタン
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 入力したチャンネルIDを取得
                Channel_ID = editText.getText().toString();
                Log.i("test",Channel_ID + "\n" );
                //jsonSearch();
                fakeSearch();
                Log.i("test","search完了時点で取得したデータ" + youtubeDataArray + "\n" );
                makeVideoId();
                //jsonVideo();
                //Log.i("test","video完了時点で取得したデータ" + youtubeDataArray + "\n" );
                //https://www.google.com/search?q=android+studio+AcyncLoader&rlz=1C1FQRR_jaJP977JP977&sxsrf=ALiCzsZYwBa_35HQEyBgshbhEWyVxDMTOw%3A1658145621294&ei=VUvVYs2hEe3s2roPsqGhyAU&ved=0ahUKEwiNoPjlsYL5AhVttlYBHbJQCFkQ4dUDCA4&uact=5&oq=android+studio+AcyncLoader&gs_lcp=Cgdnd3Mtd2l6EAM6BwgAEEcQsAM6CwgAEIAEEAQQJRAgOgQIIxAnOgUIABCABEoECEEYAEoECEYYAFD1BVj-DmDgEGgBcAB4AIABgwGIAYMEkgEDMy4ymAEAoAEBoAECyAEKwAEB&sclient=gws-wiz

            }
        });

        //csvファイルを出力するボタン
        buttonCsv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //ExportCsvクラスを呼び出してファイルを作成する。
                exportCsv = new ExportCsv(youtubeDataArray);
            }
        });


    }

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

    //Youtube Data API V3(search)を使用して指定のチャンネルからアップロードされている動画のIDをリクエストし、レスポンスとして入手できるJson形式のデータをStringとして抜き出す。
    private void jsonSearch() {
        String url = "https://www.googleapis.com/youtube/v3/search?fields=items/id/videoId,items/snippet/publishedAt,items/snippet/title&order=date&part=id,snippet&maxResults=" + Max_Results + "&channelId=" + Channel_ID + "&key=" + API_KEY;
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
                        //tmpArray.clear();

                        //動画の本数をカウントする。
                        videoSum++;

                        //System.out.println(i+1 + "週目のsearchの動画ID：" + youtubeDataArray.get(i));

                    }
                    //System.out.println("searchの最終結果：" + youtubeDataArray);
                } catch (JSONException e) {
                    e.printStackTrace();
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
                //Log.i("test",youtubeDataArray.get(m).get(2) + "\n" + "makeVideoの中、中身ある？" );
                Video_ID = Video_ID + youtubeDataArray.get(m).get(2) + ",";
            }
        }
        System.out.println("複数の動画ID：" + Video_ID);
        videoSum = 0; //初期化しておく
    }




    //Youtube Data API V3(video)を使用して先ほど取得した動画のIDを元にそれらの詳細なデータをリクエストし、レスポンスとして入手できるJson形式のデータをStringとして抜き出す。
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

                        //取り出したデータをArrayListの中に格納する。search
                        youtubeDataArray.get(i).add(viewCount);
                        youtubeDataArray.get(i).add(likeCount);
                        youtubeDataArray.get(i).add(commentCount);
                        //動画を識別するための固有のインデックスを追加する。
                        youtubeDataArray.get(i).add("" + (100*loop+i));
                        //tmpArray.clear();

                    }
                    //System.out.println(youtubeDataArray);
                } catch (JSONException e) {
                    e.printStackTrace();
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


    //try {
//        Thread.sleep(10000); // 1秒(1000ミリ秒)間だけ処理を止める
//        } catch (InterruptedException e) {
//        }

    private void fakeSearch() { //APIを圧迫しないように...
        //for (int i = 0; i < 5; i++) {
            //二次元ArrayListの要素になるArrayListを作る
            ArrayList<String> tmpArray1 = new ArrayList<>();
            //取り出したデータをArrayListの中に格納する。
            tmpArray1.add("仮タイトル"+ 1 + "本目");
            tmpArray1.add("仮日時時刻"+ 1 + "本目");
            tmpArray1.add("yGFsJId5euM");
            youtubeDataArray.add(tmpArray1);

        ArrayList<String> tmpArray2 = new ArrayList<>();
        //取り出したデータをArrayListの中に格納する。
        tmpArray2.add("仮タイトル"+ 2 + "本目");
        tmpArray2.add("仮日時時刻"+ 2 + "本目");
        tmpArray2.add("tO4vhehooBs");
        youtubeDataArray.add(tmpArray2);

        ArrayList<String> tmpArray3 = new ArrayList<>();
        //取り出したデータをArrayListの中に格納する。
        tmpArray3.add("仮タイトル"+ 3 + "本目");
        tmpArray3.add("仮日時時刻"+ 3 + "本目");
        tmpArray3.add("mPDyLJ7hSdk");
        youtubeDataArray.add(tmpArray3);

        ArrayList<String> tmpArray4 = new ArrayList<>();
        //取り出したデータをArrayListの中に格納する。
        tmpArray4.add("仮タイトル"+ 4 + "本目");
        tmpArray4.add("仮日時時刻"+ 4 + "本目");
        tmpArray4.add("pNh7kXHpVKc");
        youtubeDataArray.add(tmpArray4);

        ArrayList<String> tmpArray5 = new ArrayList<>();
        //取り出したデータをArrayListの中に格納する。
        tmpArray5.add("仮タイトル"+ 5 + "本目");
        tmpArray5.add("仮日時時刻"+ 5 + "本目");
        tmpArray5.add("gFganG1nM_w");
        youtubeDataArray.add(tmpArray5);

            //tmpArray.clear();

            //動画の本数をカウントする。
            videoSum = 5;
        }


    // 位置情報許可の確認
    public void checkPermission(final String[] permissions,final int request_code){
        // 許可されていないものだけダイアログが表示される
        ActivityCompat.requestPermissions(this, permissions, request_code);
    }

    // requestPermissionsのコールバック
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case REQUEST_CODE:
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        Toast toast = Toast.makeText(this,
                                "Added Permission: " + permissions[i], Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        Toast toast = Toast.makeText(this,
                                "Rejected Permission: " + permissions[i], Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
                break;
            default:
                break;
        }
    }





}






