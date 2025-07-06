const functions = require("firebase-functions");
const admin = require("firebase-admin");
admin.initializeApp();

exports.mercadoPagoWebhook = functions.https.onRequest(async (req, res) => {
  const secret = req.get("x-signature");
  const expectedSecret = "tu_clave_secreta"; // ← reemplaza por tu clave del panel de Webhooks

  if (secret !== expectedSecret) {
    return res.status(401).send("Unauthorized");
  }

  const { type, data, action } = req.body;

  if (type === "payment" && action === "payment.updated") {
    const paymentId = data.id;

    try {
      const response = await fetch(`https://api.mercadopago.com/v1/payments/${paymentId}`, {
        headers: {
          Authorization: `Bearer TU_ACCESS_TOKEN` // ← reemplaza por tu token de prueba o producción
        }
      });
      const paymentInfo = await response.json();

      if (paymentInfo.status === "approved") {
        const externalReference = paymentInfo.external_reference; // debe ser el ID de tu orden

        await admin.firestore()
          .collection("usuarios")
          .doc(paymentInfo.payer.id) // o el UID que hayas usado
          .collection("ordenes")
          .doc(externalReference)
          .update({
            estado: "Pagado"
          });

        return res.status(200).send("Estado actualizado a Pagado");
      }
    } catch (error) {
      console.error("Error al consultar pago:", error);
      return res.status(500).send("Error interno");
    }
  }

  res.status(200).send("OK");
});
