package com.afifny.ticketing_customview

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.afifny.ticketing_customview.view.SeatsView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val seatsView = findViewById<SeatsView>(R.id.seatsView)
        val button = findViewById<Button>(R.id.finishButton)
        button.setOnClickListener {
            seatsView.seat?.let {
                Toast.makeText(this, "Kursi anda nomor ${it.name}", Toast.LENGTH_SHORT).show()
            } ?: run {
                Toast.makeText(this, "Silakan kursi terlebih dahulu!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}