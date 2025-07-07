const functions = require("firebase-functions");
const admin = require("firebase-admin");
const fetch = require("node-fetch");

admin.initializeApp();

const ACCESS_TOKEN = "TEST-5966536219334383-062903-36ce8d0ac0af4d245ec28717028e5a37-2286021716";
const WEBHOOK_SECRET = "43bf4b3b581a82ddb16a74c8956adee11bd855cee6422560a0b339ed8f6c9fe7";

// ✅ WEBHOOK DE MERCADO PAGO
exports.mercadoPagoWebhook = functions.https.onRequest(async (req, res) => {
  const signature = req.get("x-signature");
  if (signature !== WEBHOOK_SECRET) {
    return res.status(401).send("Unauthorized");
  }

  const { type, data, action } = req.body;

  if (type === "payment" && (action === "payment.updated" || action === "payment.created")) {
    const paymentId = data.id;

    try {
      const paymentRes = await fetch(`https://api.mercadopago.com/v1/payments/${paymentId}`, {
        headers: {
          Authorization: `Bearer ${ACCESS_TOKEN}`
        }
      });

      const paymentInfo = await paymentRes.json();
      const estado = paymentInfo.status;
      const ordenId = paymentInfo.external_reference;
      const uid = paymentInfo.payer?.id || "sin_uid";

      if (!ordenId) return res.status(400).send("Sin referencia externa");

      // Actualizar orden en usuario
      await admin.firestore()
        .collection("usuarios").doc(uid)
        .collection("ordenes").doc(ordenId)
        .update({ estado });

      // Guardar en 'compras'
      await admin.firestore()
        .collection("compras")
        .doc(ordenId)
        .set({
          uid,
          ordenId,
          estado,
          metodo: paymentInfo.payment_method_id,
          monto: paymentInfo.transaction_amount,
          fecha: new Date().toISOString()
        });

      return res.status(200).send("Webhook procesado");
    } catch (e) {
      console.error("Error en webhook:", e);
      return res.status(500).send("Error interno");
    }
  }

  res.status(200).send("OK");
});
