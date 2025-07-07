const functions = require("firebase-functions");
const admin = require("firebase-admin");
const fetch = require("node-fetch");

admin.initializeApp();

const ACCESS_TOKEN = "TEST-5966536219334383-062903-36ce8d0ac0af4d245ec28717028e5a37-2286021716";

exports.mercadoPagoWebhook = functions.https.onRequest(async (req, res) => {
  const { type, data, action } = req.body;

  // ✅ Solo procesamos payment.updated
  if (type === "payment" && action === "payment.updated") {
    const paymentId = data.id;

    try {
      const paymentRes = await fetch(`https://api.mercadopago.com/v1/payments/${paymentId}`, {
        headers: {
          Authorization: `Bearer ${ACCESS_TOKEN}`
        }
      });

      const paymentInfo = await paymentRes.json();
      const estado = paymentInfo.status;
      const external = paymentInfo.external_reference;

      if (!external || !external.includes("|")) {
        console.error("❌ external_reference inválido:", external);
        return res.status(400).send("Referencia externa inválida");
      }

      const [uid, ordenId] = external.split("|");

      // ✅ Actualizar orden del usuario
      await admin.firestore()
        .collection("usuarios").doc(uid)
        .collection("ordenes").doc(ordenId)
        .update({ estado });

      // ✅ Guardar compra global
      await admin.firestore()
        .collection("compras")
        .doc(ordenId)
        .set({
          uid,
          ordenId,
          estado,
          metodo: paymentInfo.payment_method_id || "desconocido",
          monto: paymentInfo.transaction_amount || 0,
          fecha: new Date().toISOString(),
          email: paymentInfo.payer?.email || "sin_email"
        });

      console.log("✅ Webhook procesado correctamente");
      return res.status(200).send("OK");

    } catch (e) {
      console.error("❌ Error en Webhook:", e);
      return res.status(500).send("Error interno");
    }
  }

  return res.status(200).send("Evento ignorado");
});
