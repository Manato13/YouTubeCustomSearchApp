package jp.ac.gifu_u.z3033116.progjissen2finalassignment;

import java.util.Map;
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

//参考にしたサイト(https://www.sejuku.net/blog/16176)
//↓↓↓↓youtubeDataArrayの各配列の中の凡例↓↓↓↓
//0.タイトル、1.投稿日時、2.動画ID、3.再生回数、4.高評価数、5.コメント数、6.動画のインデックス番号
//投稿日時のレスポンスフォーマット："YYYY-MM-DDT24:60:60Z"、"2018-08-29T15:00:03Z"
public class SortVideos {
    //フィールド変数部
    //動画の本数
    public int NumOfVideos = Integer.parseInt(MainActivity.Max_Results); //動画の本数（一時的に使う）

    //コンストラクタ部

    //メソッド部

    //並べ替えをする前の下準備を行うメソッド
    public Map<String, Double> preForSort(int selectMode,ArrayList<ArrayList<String>> YTarray) {

        Map<String, Double> dataMap = new HashMap<String, Double>(); //動画のインデックス,ソートする数値情報

        //3.再生回数順、4.高評価数順、5.コメント数順のいずれかでソートする場合
        if (selectMode == 3 || selectMode == 4 || selectMode == 5) {
            for (int i = 0; i < NumOfVideos; i++) {
                dataMap.put(YTarray.get(i).get(6), Double.parseDouble(YTarray.get(i).get(selectMode)));
            }
            //隠れた名作順(高評価/再生回数で算出。×100をすることで動画を再生した人の内、何%の人が高評価を押したか分かる)のいずれかでソートする場合
         } else if (selectMode == 7) {
            for (int i = 0; i < NumOfVideos; i++) {
                double likeDevideView = 0;
                likeDevideView = Double.parseDouble(YTarray.get(i).get(4)) / Double.parseDouble(YTarray.get(i).get(3));
                dataMap.put(YTarray.get(i).get(6), likeDevideView*100);
            }
            //1.投稿日時順でソートする場合
            //投稿日時のレスポンスフォーマット："YYYY-MM-DDT24:60:60Z"、"2018-08-29T15:00:03Z"
        } else if(selectMode == 1) {
            for (int i = 0; i < NumOfVideos; i++) {
                int timeSum = 0;
                //YYYYの部分を切り出す
                int year = Integer.parseInt(YTarray.get(i).get(1).substring(0, 4));
                //MMの部分を切り出す
                int month = Integer.parseInt(YTarray.get(i).get(1).substring(5, 7));
                //YYYYの部分を切り出す
                int date = Integer.parseInt(YTarray.get(i).get(1).substring(8, 10));
                //24(時間)の部分を切り出す
                int hour = Integer.parseInt(YTarray.get(i).get(1).substring(11, 13));
                //60(分)の部分を切り出す
                int minute = Integer.parseInt(YTarray.get(i).get(1).substring(14, 16));
                //60(秒)の部分を切り出す
                int second = Integer.parseInt(YTarray.get(i).get(1).substring(17, 19));
                //それぞれの値を足し合わせて、時間を疑似的に大小で比較できるようにする
                timeSum = year + month + date + hour + minute +second;
                dataMap.put(YTarray.get(i).get(6), (double)timeSum);
            }
            }
        else{
            System.out.println("有効なソート基準を選択してください" + "\n");
        }
        System.out.println("dataMapを表示します："+ dataMap + "\n");
        return dataMap;
    }


    //並べ替えを行う関数の本体
    public void sortMethod(boolean sort, Map<String, Double> dataMap, ArrayList<ArrayList<String>> YTarray) {
        // 2.Map.Entryのリストを作成する(MapのEntry(キーと値のペア）のリストを作成)
        List<Entry<String, Double>> list_entries = new ArrayList<Entry<String, Double>>(dataMap.entrySet());

        //sortがtrueならば昇順
        if (sort == true) {
            // 3.比較関数Comparatorを使用してMap.Entryの値を比較する(昇順)(Collectionクラスのsortメソッドを使用して、比較関数ComparatorでMapのEntryの値を比較し、compare関数を使用してMapのEntryのobj1（昇順）、obj2（降順）を定義)
            Collections.sort(list_entries, new Comparator<Entry<String, Double>>() {
                public int compare(Entry<String, Double> obj1, Entry<String, Double> obj2) {
                    // 4. 昇順(compareToメソッドを使用して降順で並べ替えられた要素を返す)
                    return obj1.getValue().compareTo(obj2.getValue());
                }
            });

            System.out.println("昇順でのソート");
            // 5. ループで要素順に値を取得する(ループで要素数を順に表示させていくと、昇順でソートされていく)
            for (Entry<String, Double> entry : list_entries) {
                //ここで動画のタイトルを出力
                System.out.println(YTarray.get(Integer.parseInt(entry.getKey())).get(0) + "\n");
                //ここでソートに使用した値を出力
                System.out.println(entry.getValue() + "\n");
                //System.out.println(entry.getKey() + " : " + entry.getValue());
            }

        //sortがfalseならば降順
        } else {
            // 6. 比較関数Comparatorを使用してMap.Entryの値を比較する（降順）(降順でソートするときは、compareToメソッドを使用するときに、Entryのobj2（降順）を指定して返せばOK)
            Collections.sort(list_entries, new Comparator<Entry<String, Double>>() {
                //compareを使用して値を比較する
                public int compare(Entry<String, Double> obj1, Entry<String, Double> obj2) {
                    //降順
                    return obj2.getValue().compareTo(obj1.getValue());
                }
            });

            System.out.println("降順でのソート");
            // 7. ループで要素順に値を取得する(ループで要素数を順に表示させていくと、降順でソートされていく)
            for (Entry<String, Double> entry : list_entries) {
                //ここで動画のタイトルを出力
                System.out.println(YTarray.get(Integer.parseInt(entry.getKey())).get(0) + "\n");
                //ここでソートに使用した値を出力
                System.out.println(entry.getValue() + "\n");
                //System.out.println(entry.getKey() + " : " + entry.getValue());
            }
        }

    }
}





