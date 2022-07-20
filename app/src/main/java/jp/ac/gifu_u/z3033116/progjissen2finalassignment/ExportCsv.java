package jp.ac.gifu_u.z3033116.progjissen2finalassignment;

/*
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.ArrayList;
import android.Manifest;

import android.os.Environment;

//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;


public class ExportCsv {
    //フィールド変数部


    //コンストラクタ部
    ExportCsv(ArrayList<ArrayList<String>> DataArray){

        try {
            // 出力ファイルの作成
            //出力先を作成する
            //FileWriter fw = new FileWriter("YoutubeData.csv", false);
            FileWriter fw = new FileWriter(Environment.getExternalStorageDirectory().getPath() + "/YoutubeData.csv", false);

            // PrintWriterクラスのオブジェクトを生成
            PrintWriter pw = new PrintWriter(new BufferedWriter(fw));

            // ヘッダーの指定
            pw.print("タイトル");
            pw.print(",");
            pw.print("投稿日時");
            pw.print(",");
            pw.print("動画ID");
            pw.print(",");
            pw.print("再生回数");
            pw.print(",");
            pw.print("高評価数");
            pw.print(",");
            pw.print("コメント数");
            pw.println(); //改行

            // データを書き込む
            for(int i = 0; i < DataArray.size(); i++){
                for(int p = 0; p < 6; p++){
                    pw.print(DataArray.get(i).get(p));
                    pw.print(",");
                }
                pw.println(); //改行
            }

            // ファイルを閉じる
            pw.close();

            // 出力確認用のメッセージ
            System.out.println("csvファイルを出力しました");

            // 出力に失敗したときの処理
        } catch (IOException ex) {
            System.out.println("csvファイルを出力できませんでした。");
            ex.printStackTrace();
        }
    }

}

 */

import android.os.Environment;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ExportCsv {

    ExportCsv(ArrayList<ArrayList<String>> DataArray) {
        try {
            //出力先を作成する
            FileWriter fw = new FileWriter(Environment.getExternalStorageDirectory().getPath() + "/test.csv", false);
            PrintWriter pw = new PrintWriter(new BufferedWriter(fw));

            //内容を指定する
            pw.print("あ");
            pw.print(",");
            pw.print("い");
            pw.println();

            pw.print("01");
            pw.print(",");
            pw.print("02");
            pw.println();

            //ファイルに書き出す
            pw.close();

            //終了メッセージを画面に出力する
            System.out.println("出力が完了しました。");

        } catch (IOException ex) {
            //例外時処理
            ex.printStackTrace();
            System.out.println("出力に失敗しました。");
        }

    }
}





