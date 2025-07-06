package com.example.app_compuservic.repositorios
import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL


object MercadoPagoService {

    private const val ACCESS_TOKEN = "TEST-5966536219334383-062903-36ce8d0ac0af4d245ec28717028e5a37-2286021716" // ← tu access token de prueba

    fun crearPreferenciaPago(total: Double, ordenId: String, email: String? = null): String? {
        try {
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
                email?.let {
                    put("payer", JSONObject().apply {
                        put("email", it)
                    })
                }
                // URLs de redirección
                put("back_urls", JSONObject().apply {
                    put("success", "https://www.tusitio.com/success")
                    put("failure", "https://www.tusitio.com/failure")
                    put("pending", "https://www.tusitio.com/pending")
                })

                put("auto_return", "approved")

            }
            Log.d("MercadoPago", "JSON enviado: ${body.toString(4)}")

            val writer = OutputStreamWriter(connection.outputStream)
            writer.write(body.toString())
            writer.flush()
            writer.close()

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_CREATED) {
                val response = connection.inputStream.bufferedReader().use { it.readText() }
                val jsonResponse = JSONObject(response)
                return jsonResponse.getString("init_point")
            } else {
                val error = connection.errorStream?.bufferedReader()?.use { it.readText() }
                Log.e("MercadoPago", "Error al crear preferencia: $error")
            }

        } catch (e: Exception) {
            Log.e("MercadoPago", "Excepción: ${e.message}")
        }

        return null
    }
}
