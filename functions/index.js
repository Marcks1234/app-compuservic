const functions = require("firebase-functions");
const fetch = require("node-fetch");

// 🔁 Webhook que escucha pagos confirmados
exports.mercadoPagoWebhook = functions.https.onRequest(async (req, res) => {
  if (req.method !== "POST") {
    return res.status(405).send("Método no permitido");
  }

  const body = req.body;

  console.log("🔔 Webhook recibido:", JSON.stringify(body, null, 2));

  // Puedes verificar si el pago fue aprobado aquí
  if (body.type === "payment" && body.data?.id) {
    const paymentId = body.data.id;

    // Aquí puedes consultar la API de MercadoPago si lo deseas
    // O actualizar el estado de la orden en Firestore

    return res.status(200).send("Notificación procesada");
  }

  return res.status(400).send("Notificación no válida");
});
