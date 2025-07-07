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

    //Esta función genera la URL de pago de Mercado Pago.
    fun crearPreferenciaPago(
        total: Double, // El total de la orden
        ordenId: String,// El ID de la orden
        email: String? = null,// El correo electrónico del comprador
        uid: String // El ID del usuario -->MUY IMPORTANTE para el Webhook
    ): String? {
        return try {
            //Aquí se crea la conexión a la API de Mercado Pago.
            val url = URL("https://api.mercadopago.com/checkout/preferences")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.setRequestProperty("Authorization", "Bearer $ACCESS_TOKEN")
            connection.setRequestProperty("Content-Type", "application/json")
            connection.doOutput = true

            //Construcción del cuerpo JSON
            val body = JSONObject().apply {
                put("items", JSONArray().apply {
                    put(JSONObject().apply {
                        put("title", "Orden $ordenId") //título que verá el usuario.
                        put("quantity", 1) //cantidad de productos
                        put("unit_price", total) //precio unitario
                        put("currency_id", "PEN") //moneda
                    })
                })

                //“Cuando este pago se complete, notifica a mi Webhook
                // y dile que fue la orden ordenId del usuario uid”.
                put("external_reference", "$uid|$ordenId") // ← IMPORTANTE

                //Si se pasa un email, se envía como parte de la preferencia.
                email?.let {
                    put("payer", JSONObject().apply {
                        put("email", it)
                    })
                }

                //Después del pago, Mercado Pago redirige al usuario a estas URLs.
                put("back_urls", JSONObject().apply {
                    put("success", "https://www.tusitio.com/success")
                    put("failure", "https://www.tusitio.com/failure")
                    put("pending", "https://www.tusitio.com/pending")
                })
                put("auto_return", "approved") // hace que el usuario vuelva
            // automáticamente cuando el pago se apruebe.
            }

            val writer = OutputStreamWriter(connection.outputStream)
            //Se envía el JSON.
            writer.write(body.toString())
            writer.flush()
            writer.close()

            if (connection.responseCode == HttpURLConnection.HTTP_CREATED) {
                val response = connection.inputStream.bufferedReader().use { it.readText() }
              //  Si la respuesta es HTTP_CREATED (201), se lee la respuesta:
                val json = JSONObject(response)
                return json.getString("init_point") //Esto devuelve el link de pago
                                                    // (init_point) que abrirás en el navegador.

            } else { //Si la respuesta no es HTTP_CREATED, se lee el error:
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