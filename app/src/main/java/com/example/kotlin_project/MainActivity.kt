package com.example.kotlin_project

import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.view.MotionEvent
import android.view.View
import android.view.View.OnLongClickListener
import android.view.View.OnTouchListener
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity(), View.OnClickListener, OnLongClickListener, OnTouchListener {
    lateinit var txv: TextView
    lateinit var btn_zero: Button
    var counter: Int = 0
    lateinit var txv_touch:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txv = findViewById(R.id.txv)
        txv.text = counter.toString()
        txv.setOnClickListener(this)
        txv.setOnLongClickListener(this)

        btn_zero = findViewById(R.id.btn_zero)
        btn_zero.setOnClickListener(this)

        var btn_double: Button = findViewById(R.id.btn_double)
        btn_double.setOnClickListener(object : View.OnClickListener {    //Anonymous Listener寫法
            override fun onClick(p0: View?) {
                counter *= 2
                txv.text = counter.toString()
            }
        })

        var btn_triple:Button = findViewById(R.id.btn_triple)
        btn_triple.setOnClickListener({    //使用Lambda匿名函式的簡化寫法
            counter*=3
            txv.text = counter.toString()
        })

    }

    fun happy(v: View){
        //var txv: TextView = findViewById(R.id.txv)
        counter = (1..100).random()
        txv.text = counter.toString()
    }

    override fun onClick(v: View?) {
        if (v==btn_zero){
            counter = 0
        }
        else{
            counter++
        }
        txv.text = counter.toString()
    }

    override fun onLongClick(v: View?): Boolean {    //true表示操作至此結束，手指放開不會再觸發click事件
        counter+=2                                   //false表示使用者放開手指，繼續引發click事件
        txv.text = counter.toString()
        return true                                  //如果改成return false，每次會變成加3 (長按加2後，又觸發click事件再加1)
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {  // API 31以上
            val vibratorManager = getSystemService(VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {  //API < 31
            getSystemService(VIBRATOR_SERVICE) as Vibrator      //取得震動之系統服務
        }


        //利用event.getAction()傳遞的參數可得知觸控的種類
        //(MotionEvent.ACTION_DOWN按下)
        //(MotionEvent.ACTION_UP手指彈開)
        if (event?.action == MotionEvent.ACTION_DOWN) {
            txv_touch.text = "手指按下"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {  //API >= 26
                //VibrationEffect.createOneShot 設定執行震動第一個參數是毫秒數第二個參數是震動強度，值在 1 - 255 之間
                vibrator.vibrate(VibrationEffect.createOneShot(5000, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                vibrator.vibrate(5000)
            }
        }else if (event?.action == MotionEvent.ACTION_UP){
            txv_touch.text = "手指彈開"
            vibrator.cancel()
        }
        return true
    }
}
