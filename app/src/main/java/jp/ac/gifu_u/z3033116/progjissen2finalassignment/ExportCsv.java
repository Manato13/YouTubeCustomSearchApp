package jp.ac.gifu_u.z3033116.progjissen2finalassignment;


import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import android.Manifest;

import android.content.Intent;
import android.os.Bundle;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;


public class ExportCsv {

String fileName1111 = "data.csv";
String mainData;
public int NumOfVideos = Integer.parseInt(MainActivity.Max_Results); //動画の本数（一時的に使う）

//メソッド部
    //二次元ListArray型だったデータをコンマと改行で区切られたString型の文字列に変換する
    public String ConvertARtoST(ArrayList<ArrayList<String>> YTarray){
        //ファイルに書き込むための文字列を作成する
        StringBuilder sb = new StringBuilder();
        //↓↓↓↓youtubeDataArrayの各配列の中の凡例↓↓↓↓
        //0.タイトル、1.投稿日時、2.動画ID、3.再生回数、4.高評価数、5.コメント数、6.動画のインデックス番号
        //まずは最上部（データ名）の作成
        sb.append("タイトル,");
        sb.append("投稿日時,");
        sb.append("動画ID,");
        sb.append("再生回数,");
        sb.append("高評価数,");
        sb.append("コメント数");
        sb.append("\n");
        //実際に取得したデータを追加していく
        //for(ArrayList<String> subYTarray : YTarray) { 前後が逆になる不具合が治ったら使う
        for(int i = 0; i< NumOfVideos; i++){ //仮
            for(int k = 0; k < 6; k++) {
                sb.append(YTarray.get(i).get(k)); //仮
                //sb.append(subYTarray.get(k)); 前後が逆になる不具合が治ったら使う
                if(k==5){
                    //コメント数のところまで追加したら改行する
                    sb.append("\n");
                }
                else{
                    //それまではコンマで区切る
                    sb.append(",");
                }
                }
            }
        //データをコンマと改行で区切った文字列を返す
        System.out.println(sb.toString());
        return sb.toString();
    }

//public void writeCsvFile(String fileName, String data){
//    try(FileOutputStream fileOutputStream = openFileOutput(fileName,Context.MODE_APPEND)){
//        fileOutputStream.write(data.getBytes(StandardCharsets.UTF_8));
//    }catch (IOException e){
//        e.printStackTrace();
//    }
//}



}