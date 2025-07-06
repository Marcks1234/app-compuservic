const functions = require("firebase-functions");
const admin = require("firebase-admin");
const fetch = require("node-fetch");
const { v4: uuidv4 } = require("uuid");

admin.initializeApp();

const ACCESS_TOKEN = "TEST-5966536219334383-062903-36ce8d0ac0af4d245ec28717028e5a37-2286021716"; // ← tu token de Mercado Pago
const WEBHOOK_SECRET = "43bf4b3b581a82ddb16a74c8956adee11bd855cee6422560a0b339ed8f6c9fe7"; // ← tu clave secreta de Webhooks


// ✅ Función para crear una preferencia de pago
exports.crearPreferencia = functions.https.onCall(async (data, context) => {
  const { total, ordenId, email } = data;

  const body = {
    items: [
      {
        title: `Orden ${ordenId}`,
        quantity: 1,
        unit_price: total,
        currency_id: "PEN",
      },
    ],
    external_reference: ordenId,
    payer: {
      email: email,
    },
    back_urls: {
      success: "https://www.tusitio.com/success",
      failure: "https://www.tusitio.com/failure",
      pending: "https://www.tusitio.com/pending",
    },
    auto_return: "approved",
  };

  const response = await fetch("https://api.mercadopago.com/checkout/preferences", {
    method: "POST",
    headers: {
      Authorization: `Bearer ${ACCESS_TOKEN}`,
      "Content-Type": "application/json",
    },
    body: JSON.stringify(body),
  });

  const json = await response.json();

  if (json.init_point) {
    return { url: json.init_point };
  } else {
    throw new functions.https.HttpsError("unknown", "Error al crear preferencia de pago", json);
  }
});

// ✅ Webhook para actualizar estado y guardar en Firestore
exports.mercadoPagoWebhook = functions.https.onRequest(async (req, res) => {
  const secret = req.get("x-signature");

  if (secret !== WEBHOOK_SECRET) {
    return res.status(401).send("Unauthorized");
  }

  const { type, action, data } = req.body;

  if (type === "payment" && action === "payment.updated") {
    const paymentId = data.id;

    try {
      const response = await fetch(`https://api.mercadopago.com/v1/payments/${paymentId}`, {
        headers: {
          Authorization: `Bearer ${ACCESS_TOKEN}`,
        },
      });

      const paymentInfo = await response.json();

      if (paymentInfo.status === "approved") {
        const ordenId = paymentInfo.external_reference;
        const uid = paymentInfo.payer.id || "sin_uid";

        const pagoData = {
          ordenId,
          estado: "Pagado",
          total: paymentInfo.transaction_amount,
          fecha: admin.firestore.Timestamp.now(),
          metodo: paymentInfo.payment_method_id,
          email: paymentInfo.payer.email,
        };

        // 🔄 Actualizar estado en colección del usuario
        await admin.firestore()
          .collection("usuarios")
          .doc(uid)
          .collection("ordenes")
          .doc(ordenId)
          .update({ estado: "Pagado" });

        // 🧾 Guardar en colección global de compras
        await admin.firestore()
          .collection("compras")
          .doc(ordenId)
          .set(pagoData);

        return res.status(200).send("Pago procesado y guardado");
      }

      return res.status(200).send("Pago no aprobado aún");
    } catch (e) {
      console.error("Error en webhook:", e);
      return res.status(500).send("Error interno");
    }
  }

  res.status(200).send("Evento no procesado");
});
