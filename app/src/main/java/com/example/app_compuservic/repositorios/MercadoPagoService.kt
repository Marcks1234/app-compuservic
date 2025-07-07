package com.example.app_compuservic.repositorios

import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL


object MercadoPagoService {

    private const val ACCESS_TOKEN =
        "TEST-5966536219334383-062903-36ce8d0ac0af4d245ec28717028e5a37-2286021716"

    fun crearPreferenciaPago(
        total: Double,
        ordenId: String,
        email: String? = null,
        uid: String
    ): String? {
        return try {
            val url = URL("https://api.mercadopago.com/checkout/preferences")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.setRequestProperty("Authorization", "Bearer $ACCESS_TOKEN")
            connection.setRequestProperty("Content-Type", "application/json")
            connection.doOutput = true

            val body = JSONObject().apply {
                put("items", JSONArray().apply {
                    put(JSONObject().apply {
                        put("title", "Orden $ordenId")
                        put("quantity", 1)
                        put("unit_price", total)
                        put("currency_id", "PEN")
                    })
                })
                put("external_reference", "$uid|$ordenId") // ← IMPORTANTE
                email?.let {
                    put("payer", JSONObject().apply {
                        put("email", it)
                    })
                }
                put("back_urls", JSONObject().apply {
                    put("success", "https://www.tusitio.com/success")
                    put("failure", "https://www.tusitio.com/failure")
                    put("pending", "https://www.tusitio.com/pending")
                })
                put("auto_return", "approved")
            }

            val writer = OutputStreamWriter(connection.outputStream)
            writer.write(body.toString())
            writer.flush()
            writer.close()

            if (connection.responseCode == HttpURLConnection.HTTP_CREATED) {
                val response = connection.inputStream.bufferedReader().use { it.readText() }
                val json = JSONObject(response)
                return json.getString("init_point")
            } else {
                val error = connection.errorStream?.bufferedReader()?.use { it.readText() }
                Log.e("MercadoPago", "Error al crear preferencia: $error")
            }

            null
        } catch (e: Exception) {
            Log.e("MercadoPago", "Excepción: ${e.message}")
            null
        }
    }
}