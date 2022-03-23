package com.aula.calculadorabteste

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.bloco_cotacao.*
import kotlinx.android.synthetic.main.bloco_entrada.*
import kotlinx.android.synthetic.main.bloco_saida.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONObject
import java.net.URL
import java.text.NumberFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    val API_URL ="https://www.mercadobitcoin.net/api/BTC/ticker/"
    var cotacaoBitcoin:Double= 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buscarCotacao()

        btn_calcular.setOnClickListener{
            calcular()
        }
    }

    fun calcular(){
        if(txt_valor.text.isEmpty()){
            txt_valor.error = "preencha um valor"
            return
        }

        val valor_digitado = txt_valor.text.toString().replace(",",".").toDouble()

        val resultado = if(cotacaoBitcoin > 0) valor_digitado/cotacaoBitcoin else 0.0

        txt_qtd_bitcoins.text = "%.8f".format(resultado)
    }

    fun buscarCotacao(){
        doAsync {
            val  resposta = URL(API_URL).readText()

            cotacaoBitcoin = JSONObject(resposta).getJSONObject("ticker").getDouble("last")

            val f =NumberFormat.getCurrencyInstance(Locale("pt","br"))

            val cotacaoFormatada = f.format(cotacaoBitcoin)

            uiThread {
                txt_cotacao.setText("$cotacaoFormatada")
            }
        }

    }
}