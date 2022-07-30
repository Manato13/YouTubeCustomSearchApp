package jp.ac.gifu_u.z3033116.progjissen2finalassignment;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.OutputStream;
import java.util.ArrayList;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
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

import android.app.Activity;


//起動直後の処理を行うクラス
public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    FragmentManager manager;
    public static Context contextOfApplication;

    //コンストラクタ部(=onCreate)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //フラグメント、ツールバーなどの設定
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(toolbar);
        toolbar = findViewById(R.id.id_toolbar);
        toolbar.setTitle("Youtube Custom Search");
        contextOfApplication = getApplicationContext();

        //初期画面をstart_menu.xmlにする
        manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.frame_layout_activity_layout, new StartMenu());
        transaction.commit();
    }

    //メソッド部
    public static Context getContextOfApplication()
    {
        return contextOfApplication;
    }


}







