package com.websarva.wings.android.alwaysonstandbytest001;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.TimerTask;
import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    private int count_num = 0;
    private boolean count_flag = false;
    private Timer timer;
    private TimerTask01 timerTask01;
//    Button bt_count_start       = findViewById(R.id.bt_count_start);                              // ボタンを登録
    private Button bt_count_start;                                                                  // グローバル化するため登録しておく
    private TextView tv_counter;
    // メインスレッドアクセス用ハンドラ
    private android.os.Handler handler001 = new android.os.Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt_count_start              = (Button)findViewById(R.id.bt_count_start);                    // オブジェクトに登録
        tv_counter                  = (TextView)findViewById(R.id.tv_counter);

        MainListener mainListener   = new MainListener();                                           // リスナ開始
        bt_count_start.setOnClickListener(mainListener);                                            // ボタンをリスナに登録

    }


    @Override
    public void onDestroy() {    // アプリが終了した場合
        super.onDestroy();
    }


    // クリック ～ ビュー関係 オーバーライド
    private class MainListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            int objID = view.getId();

            switch (objID) {
                case R.id.bt_count_start:
                    count_flag = !count_flag;                                                       // カウントフラグのスイッチ動作（false,true)
                    break;
                default:
                    String mesg = "認識されないオブジェクトがクリックされました。:" + String.valueOf(objID);
                    Toast.makeText(getApplicationContext(), mesg, Toast.LENGTH_LONG).show();
                    break;
            }

            // timer起動関係　これだめだね、bt_count_start以外のイベントが発生した場合もここくる、そのときたまたまfalseならtimer上書き、trueなら存在しないtimerをキャンセルすることに・・・、よく考える。 flag=null する？
            if(count_flag) {
                // flagがtureの場合、タイマーを機動
                Toast.makeText(getApplicationContext(), "タイマースタート", Toast.LENGTH_SHORT).show();
                timerTask01 = new TimerTask01();                                                    // ↓で作ったタイマータスクをインスタンス化
                timer = new Timer();                                                                // 実時間で定期的に起動するためのオブジェクト
                timer.schedule(timerTask01, 1000, 1000);                           // 一秒ごとにtimerTask01を起動する。
                bt_count_start.setText("カウントストップ");
            }else{
                // flagがfalseの場合、タイマーを停止
                Toast.makeText(getApplicationContext(), "タイマーストップ", Toast.LENGTH_SHORT).show();
                timer.cancel();                                                                     // timerを止める
                timer = null;                                                                       // timerを破棄する（これってポインタの参照を消す＝メモリ開放とかなのかな？）
                bt_count_start.setText("カウントスタート");
                count_num = 0;                                                                      // 止めるたびに０に戻したい場合はコメントアウトを外す。
            }
        }
    }

    /*
    * カウントアップタイマー設定
    * */
    public class TimerTask01 extends TimerTask{
        @Override
        public void run(){
            handler001.post(new Runnable(){
                @Override
                public void run() {
                    tv_counter.setText(String.valueOf(count_num));                                  // 値表示
                    count_num++;                                                                    // timerカウントアップ
                }
            });
        }
    }
}


