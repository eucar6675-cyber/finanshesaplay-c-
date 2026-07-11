package com.example.karzararhesapla

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {
    private lateinit var df: DecimalFormat
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        df = DecimalFormat("#,##0.00")
        
        val etYatirimTutari = findViewById<TextInputEditText>(R.id.etYatirimTutari)
        val etKomisyonOrani = findViewById<TextInputEditText>(R.id.etKomisyonOrani)
        val etVergiOrani = findViewById<TextInputEditText>(R.id.etVergiOrani)
        val etKarZararOrani = findViewById<TextInputEditText>(R.id.etKarZararOrani)
        val rgYon = findViewById<RadioGroup>(R.id.rgYon)
        val btnHesapla = findViewById<Button>(R.id.btnHesapla)
        val tvBrutResult = findViewById<TextView>(R.id.tvBrutResult)
        val tvKomisyonResult = findViewById<TextView>(R.id.tvKomisyonResult)
        val tvVergiResult = findViewById<TextView>(R.id.tvVergiResult)
        val tvNetParaResult = findViewById<TextView>(R.id.tvNetParaResult)
        val tvDurumEtiketi = findViewById<TextView>(R.id.tvDurumEtiketi)

        btnHesapla.setOnClickListener {
            val yatirimStr = etYatirimTutari.text.toString().trim()
            val komisyonStr = etKomisyonOrani.text.toString().trim()
            val vergiStr = etVergiOrani.text.toString().trim()
            val karZararStr = etKarZararOrani.text.toString().trim()

            if (yatirimStr.isEmpty() || komisyonStr.isEmpty() || vergiStr.isEmpty() || karZararStr.isEmpty()) {
                Toast.makeText(this, "Lütfen tüm alanları doldurun!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            try {
                val yatirimTutari = yatirimStr.replace(",", ".").toDouble()
                val komisyonOrani = komisyonStr.replace(",", ".").toDouble()
                val vergiOrani = vergiStr.replace(",", ".").toDouble()
                val karZararOrani = karZararStr.replace(",", ".").toDouble()
                
                val isProfit = rgYon.checkedRadioButtonId == R.id.rbKar
                val yonKatsayisi = if (isProfit) 1.0 else -1.0
                
                val brutKarZarar = yatirimTutari * (karZararOrani / 100.0) * yonKatsayisi
                val alimKomisyonu = yatirimTutari * (komisyonOrani / 100.0)
                val satisTutari = yatirimTutari + brutKarZarar
                val satisKomisyonu = satisTutari * (komisyonOrani / 100.0)
                val toplamKomisyon = alimKomisyonu + satisKomisyonu
                val netVergi = if (isProfit && brutKarZarar > 0) brutKarZarar * (vergiOrani / 100.0) else 0.0
                val netPara = yatirimTutari + brutKarZarar - toplamKomisyon - netVergi
                val paraBirimi = " TL"
                
                if (isProfit) {
                    tvBrutResult.text = "+${df.format(brutKarZarar)}$paraBirimi"
                    tvBrutResult.setTextColor(Color.parseColor("#4ADE80"))
                } else {
                    tvBrutResult.text = "${df.format(brutKarZarar)}$paraBirimi"
                    tvBrutResult.setTextColor(Color.parseColor("#F87171"))
                }
                
                tvKomisyonResult.text = "${df.format(toplamKomisyon)}$paraBirimi"
                tvVergiResult.text = "${df.format(netVergi)}$paraBirimi"
                tvNetParaResult.text = "${df.format(netPara)}$paraBirimi"
                
                val netKazanc = netPara - yatirimTutari
                if (netKazanc >= 0) {
                    tvNetParaResult.setTextColor(Color.parseColor("#22C55E"))
                    tvDurumEtiketi.text = "Net Durum: KÂRDASINIZ (+${df.format(netKazanc)} TL)"
                    tvDurumEtiketi.setTextColor(Color.parseColor("#22C55E"))
                } else {
                    tvNetParaResult.setTextColor(Color.parseColor("#EF4444"))
                    tvDurumEtiketi.text = "Net Durum: ZARARDASINIZ (${df.format(netKazanc)} TL)"
                    tvDurumEtiketi.setTextColor(Color.parseColor("#EF4444"))
                }
            } catch (e: Exception) {
                Toast.makeText(this, "Geçersiz değer girdiniz!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
