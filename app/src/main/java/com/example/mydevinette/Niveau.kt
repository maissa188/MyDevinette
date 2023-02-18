package com.example.mydevinette

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.TextView
import android.annotation.SuppressLint
import android.content.Intent
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast

import kotlin.random.Random

class Niveau : AppCompatActivity() {

    internal  var helper= DatabaseHoelper(this)

    var list = mutableListOf<Joueur>()
    private  lateinit var tVScore: TextView

    private var nbEssais : Int=0
    private  var nbSecondesPassees:Int=0
    var score:Int =100
    var i: Int=0
    private  var msg : String=""
    private  val START_TIME_IN_MILI: Long = 80 * 1000
    private var remainingTime : Long = START_TIME_IN_MILI
    private  var isTimerRunning : Boolean=false
    private var timer: CountDownTimer? =null
    private  lateinit var  textViewEssais:TextView
    private  lateinit var textViewMessage: TextView
    private  lateinit var textViewTimer: TextView
    private lateinit var editText: EditText
    private lateinit var textViewHistory: TextView
    private  lateinit var imageButtonReset: ImageButton
    private  lateinit var  imageButtonCheck: ImageButton
    private  var random: Int = Random.nextInt(1, 1000)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_niveau)
        tVScore= findViewById(R.id.textViewScore)
        textViewEssais=findViewById(R.id.tvEssais)

        textViewHistory= findViewById(R.id.textViewMLHistorique)

        textViewTimer= findViewById(R.id.tvTimer)

        if(!isTimerRunning){
            startTimer()
        }
        textViewMessage= findViewById(R.id.tvMessage)




        editText= findViewById(R.id.editTextNumber)

        imageButtonCheck= findViewById(R.id.imageButtonCheck)
        imageButtonCheck.setOnClickListener {
            if(nbEssais<20){
                try {
                    i++
                    if(i==10){
                        msg=""
                        textViewHistory.text=""
                    }
                    val  number: Int=editText.text.toString().toInt()
                    val m:String=editText.text.toString()

                    if(number> 1000 || number< 1){
                        Toast.makeText(applicationContext,"Entrer un nombre entre 1 et 1000", Toast.LENGTH_LONG).show()
                    }else{

                        nbEssais++
                        var nbE= (20 - nbEssais)
                        textViewEssais.text= nbE.toString()


                        Toast.makeText(applicationContext,nbEssais.toString(), Toast.LENGTH_LONG).show()

                        if(number< random){
                            textViewMessage.text="Wrong, le numero est plus petit!"

                            editText.text.clear();
                            msg =msg+ m+" : plus petit\n"
                            textViewHistory.text=msg

                        }else if(number> random){
                            textViewMessage.text="Wrong, le numero est plus grand!"
                            editText.text.clear()
                            msg = msg+ m+" : plus grand\n"
                            textViewHistory.text=msg
                        }else{
                            textViewMessage.text="Super, Le numero est correct "
                            gameGood(m)

                        }



                    }

                }catch (e: java.lang.Exception){
                    e.message
                }


            }else {
                gameOver()

            }



        }

    }

    @SuppressLint("SuspiciousIndentation")
    private fun gameGood(m: String) {
        editText.setFocusable(false)

        imageButtonCheck.setClickable(false)

        msg = m + " : GOOD\n"
        timer?.cancel()
        textViewHistory.text = msg
        getScore()
        viewAll()
        if(list.size >= 0 && list.size < 10){
            intentScore()
        }else{
            if(firstJoueur(score)){
                intentScore()

            }

        }
    }
    private fun gameOver() {
        editText.setFocusable(false)
        imageButtonCheck.setClickable(false)
        timer?.cancel()
        getScore()

    }

    private fun startTimer() {
        timer = object : CountDownTimer(remainingTime, 1000) {
            override fun onTick(timeleft: Long) {
                remainingTime=timeleft
                updateTimerText()
                nbSecondesPassees++

            }

            override fun onFinish() {
                textViewTimer.text = "Game Over!"

                editText.setFocusable(false)
                imageButtonCheck.setClickable(false)
                isTimerRunning= false
                getScore()


            }


        }.start()

        isTimerRunning=true
    }



    private fun updateTimerText(){
        val minute = remainingTime.div(1000).div(60)
        val second = remainingTime.div(1000) % 60

        val formatTime = String.format("%02d:%02d",minute,second)
        textViewTimer.text= formatTime


    }
    private fun reset(){

        editText.setFocusable(true)
        imageButtonCheck.setClickable(true)
        msg=""
        random= Random.nextInt(1, 1000)
        editText.text.clear()
        resetTimer()
        textViewHistory.text=""
        tVScore.text="00"


    }

    private fun resetTimer() {
        timer?.cancel()
        remainingTime = START_TIME_IN_MILI
        updateTimerText()
        isTimerRunning=false
    }

    override fun onStart() {
        super.onStart()

    }

    override fun onStop() {
        super.onStop()
        timer?.cancel()
        isTimerRunning=false

    }
    override fun onPause() {
        super.onPause()
        timer?.cancel()
        isTimerRunning=false

    }

    override fun onRestart() {
        super.onRestart()
        if(!isTimerRunning){
            startTimer()
        }
    }
    override fun onResume() {
        super.onResume()
        if(!isTimerRunning){
            startTimer()

        }


    }

    private  fun getScore(){
        score= score - (nbEssais + nbSecondesPassees)
        tVScore.text = score.toString()

    }

    fun viewAll(){
        list.clear()
        val res=helper.allData
        if(res.count == 0){
            Toast.makeText(this, "No records", Toast.LENGTH_SHORT).show()

        }
        while (res.moveToNext()){
            list.add(Joueur(res.getString(0),
                res.getString(1),
                res.getString(2)))

        }
    }

    private fun intentScore() {
        val d = Intent(this, Score::class.java)
        d.putExtra("SCORE", score)
        startActivity(d)
        finish()
    }

    fun  firstJoueur(s : Int): Boolean{
        var p : Boolean = false
        for ( joueur in list ){
            if(Integer.parseInt(joueur.score) <= s){
                p= true
            }
        }

        return p
    }

}