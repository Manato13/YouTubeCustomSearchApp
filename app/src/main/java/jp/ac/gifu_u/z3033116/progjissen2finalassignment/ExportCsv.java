package jp.ac.gifu_u.z3033116.progjissen2finalassignment;

import java.util.ArrayList;

//csvファイルを出力する下準備をするクラス
public class ExportCsv {

public int NumOfVideos = Integer.parseInt(StartMenu.Max_Results); //動画の本数（一時的に使う）
//メソッド部
    //二次元ListArray型だったデータをコンマと改行で区切られたString型の文字列に変換する
    public static String ConvertARtoST(ArrayList<ArrayList<String>> YTarray){
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
        for(ArrayList<String> subYTarray : YTarray) { //前後が逆になる不具合が治ったら使う
        //for(int i = 0; i< NumOfVideos; i++){ //仮
            for(int k = 0; k < 6; k++) {
                //sb.append(YTarray.get(i).get(k)); //仮
                sb.append(subYTarray.get(k)); //前後が逆になる不具合が治ったら使う
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





}