package rp3.marketforce.sync;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.transport.HttpResponseException;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import rp3.configuration.PreferenceManager;
import rp3.connection.HttpConnection;
import rp3.connection.WebService;
import rp3.content.*;
import rp3.db.sqlite.DataBase;
import rp3.marketforce.Contants;
import rp3.marketforce.models.AgendaTarea;
import rp3.marketforce.models.AgendaTareaActividades;
import rp3.marketforce.models.pedido.Pago;
import rp3.marketforce.models.pedido.PedidoDetalle;
import rp3.marketforce.utils.Utils;
import rp3.util.Convert;

/**
 * Created by magno_000 on 14/10/2015.
 */
public class Pedido {
    public static int executeSync(DataBase db, long idPedido) {
        WebService webService = new WebService("MartketForce", "UpdatePedido");
        webService.setTimeOut(20000);

        rp3.marketforce.models.pedido.Pedido pedidoUpload = rp3.marketforce.models.pedido.Pedido.getPedido(db, idPedido);

        JSONObject jObject = new JSONObject();
        try {
            DecimalFormat df = new DecimalFormat("#.##");
            df.setRoundingMode(RoundingMode.CEILING);

            jObject.put("IdAgenda", pedidoUpload.getIdAgenda());
            jObject.put("IdRuta", PreferenceManager.getInt(Contants.KEY_IDRUTA));
            jObject.put("IdPedido", pedidoUpload.getIdPedido());
            jObject.put("IdCliente", pedidoUpload.getIdCliente());
            jObject.put("ValorTotal", df.format(pedidoUpload.getValorTotal()));
            jObject.put("Email", pedidoUpload.getEmail());
            jObject.put("Estado", pedidoUpload.getEstado());
            jObject.put("FechaCreacionTicks", Convert.getDotNetTicksFromDate(pedidoUpload.getFechaCreacion()));
            if(pedidoUpload.get_idAgenda() != 0)
                jObject.put("IdAgenda", rp3.marketforce.models.Agenda.getAgenda(db, pedidoUpload.get_idAgenda()).getIdAgenda());
            if(pedidoUpload.getEstado().equalsIgnoreCase("A")) {
                jObject.put("Anulado", true);
                jObject.put("FecAnula", pedidoUpload.getEstado());
            }
            else
                jObject.put("Anulado", false);

            jObject.put("BaseImponible", df.format(pedidoUpload.getBaseImponible()));
            jObject.put("BaseImponibleCero", df.format(pedidoUpload.getBaseImponibleCero()));
            jObject.put("Cambio", df.format(pedidoUpload.getExcedente()));

            jObject.put("IdEmpresa", PreferenceManager.getInt(Contants.KEY_ID_EMPRESA));
            jObject.put("IdEstablecimiento", PreferenceManager.getInt(Contants.KEY_ID_ESTABLECIMIENTO));
            jObject.put("IdMoneda", PreferenceManager.getInt(Contants.KEY_ID_MONEDA));
            jObject.put("IdNumeroCaja", PreferenceManager.getInt(Contants.KEY_ID_CAJA));
            jObject.put("IdPuntoOperacion", PreferenceManager.getInt(Contants.KEY_ID_PUNTO_OPERACION));
            jObject.put("NumeroDocumento", pedidoUpload.getNumeroDocumento());
            jObject.put("Observacion", pedidoUpload.getObservaciones());
            jObject.put("Redondeo", df.format(pedidoUpload.getRedondeo()));
            jObject.put("Subtotal", df.format(pedidoUpload.getSubtotal()));
            jObject.put("SubtotalSinDescuento", df.format(pedidoUpload.getSubtotalSinDescuento()));
            jObject.put("SubtotalSinImpuesto", df.format((pedidoUpload.getValorTotal() + pedidoUpload.getExcedente() - pedidoUpload.getTotalImpuestos())));
            jObject.put("TipoTransaccion", pedidoUpload.getTipoDocumento());
            jObject.put("ValorDescAutomatico", df.format(pedidoUpload.getTotalDescuentos()));
            jObject.put("ValorImpuestoIvaVenta", df.format(pedidoUpload.getTotalImpuestos()));
            if(pedidoUpload.getTipoDocumento().equalsIgnoreCase("FA"))
                jObject.put("Secuencia", PreferenceManager.getInt(Contants.KEY_SECUENCIA_FACTURA));
            if(pedidoUpload.getTipoDocumento().equalsIgnoreCase("NC"))
                jObject.put("Secuencia", PreferenceManager.getInt(Contants.KEY_SECUENCIA_NOTA_CREDITO));


            JSONArray jArrayDetalle = new JSONArray();
            for (PedidoDetalle det : pedidoUpload.getPedidoDetalles()) {
                JSONObject jObjectDetalle = new JSONObject();
                jObjectDetalle.put("IdProducto", det.getIdProducto());
                jObjectDetalle.put("Descripcion", det.getDescripcion());
                jObjectDetalle.put("IdPedido", det.getIdPedido());
                jObjectDetalle.put("IdPedidoDetalle", det.getIdPedidoDetalle());
                jObjectDetalle.put("ValorUnitario", df.format(det.getValorUnitario()));
                jObjectDetalle.put("Cantidad", det.getCantidad());
                jObjectDetalle.put("ValorTotal", df.format(det.getValorTotal()));
                jObjectDetalle.put("BaseImponible", df.format(det.getBaseImponible()));
                jObjectDetalle.put("BaseImponibleCero", df.format(det.getBaseImponibleCero()));
                //jObjectDetalle.put("IdBeneficio", det.getProducto().getIdBeneficio());
                jObjectDetalle.put("PorcDescAutomatico", df.format(det.getPorcentajeDescuentoAutomatico()));
                jObjectDetalle.put("PorcDescManual", df.format(det.getPorcentajeDescuentoManual()));
                jObjectDetalle.put("PorcImpuestoIvaVenta", df.format(det.getPorcentajeImpuesto()));
                jObjectDetalle.put("Subtotal", df.format(det.getSubtotal()));
                jObjectDetalle.put("SubtotalSinDescuento", df.format(det.getSubtotalSinDescuento()));
                jObjectDetalle.put("SubtotalSinImpuesto", df.format(det.getSubtotalSinImpuesto()));
                jObjectDetalle.put("ValorDescAutomatico", df.format(det.getValorDescuentoAutomatico()));
                jObjectDetalle.put("ValorDescAutomaticoTotal", df.format(det.getValorDescuentoAutomaticoTotal()));
                jObjectDetalle.put("ValorDescManual", df.format(det.getValorDescuentoManual()));
                jObjectDetalle.put("ValorDescManualTotal", df.format(det.getValorDescuentoManualTotal()));
                jObjectDetalle.put("ValorImpuestoIvaVenta", df.format(det.getValorImpuesto()));
                jObjectDetalle.put("ValorImpuestoIvaVentaTotal", df.format(det.getValorImpuestoTotal()));

                jArrayDetalle.put(jObjectDetalle);
            }

            jObject.put("PedidoDetalles", jArrayDetalle);

            JSONArray jArrayPago = new JSONArray();
            for (Pago pago : pedidoUpload.getPagos()) {
                JSONObject jObjectPago = new JSONObject();
                jObjectPago.put("IdPedido", pago.getIdPedido());
                jObjectPago.put("FactorCambio", 1);
                jObjectPago.put("IdFormaPago", pago.getIdFormaPago());
                jObjectPago.put("IdMoneda", PreferenceManager.getInt(Contants.KEY_ID_MONEDA));
                jObjectPago.put("Observacion", pago.getObservacion());
                jObjectPago.put("Valor", df.format(pago.getValor()));
                jObjectPago.put("ValorMoneda", 1);

                jArrayPago.put(jObjectPago);
            }

            jObject.put("Pagos", jArrayPago);
        } catch (Exception ex) {

        }

        webService.addParameter("pedido", jObject);

        try {
            webService.addCurrentAuthToken();

            try {
                webService.invokeWebService();
                int id = webService.getIntegerResponse();
                pedidoUpload.setIdPedido(id);
                rp3.marketforce.models.pedido.Pedido.update(db, pedidoUpload);
                for (PedidoDetalle det : pedidoUpload.getPedidoDetalles()) {
                    det.setIdPedido(id);
                    PedidoDetalle.update(db, det);
                }
            } catch (HttpResponseException e) {
                if (e.getStatusCode() == HttpConnection.HTTP_STATUS_UNAUTHORIZED)
                    return rp3.content.SyncAdapter.SYNC_EVENT_AUTH_ERROR;
                return rp3.content.SyncAdapter.SYNC_EVENT_HTTP_ERROR;
            } catch (Exception e) {
                return rp3.content.SyncAdapter.SYNC_EVENT_ERROR;
            }

        } finally {
            webService.close();
        }

        return rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS;
    }

    public static int executeSyncAnular(DataBase db, long idPedido) {
        WebService webService = new WebService("MartketForce", "AnularPedido");
        webService.setTimeOut(20000);

        rp3.marketforce.models.pedido.Pedido pedidoUpload = rp3.marketforce.models.pedido.Pedido.getPedido(db, idPedido);

        JSONObject jObject = new JSONObject();
        try {
            DecimalFormat df = new DecimalFormat("#.##");
            df.setRoundingMode(RoundingMode.CEILING);

            jObject.put("IdPedido", pedidoUpload.getIdPedido());
            jObject.put("MotivoAnulacion", pedidoUpload.getMotivoAnulacion());
            jObject.put("ObservacionAnulacion", pedidoUpload.getObservacionAnulacion());

        } catch (Exception ex) {

        }

        webService.addParameter("pedido", jObject);

        try {
            webService.addCurrentAuthToken();

            try {
                webService.invokeWebService();
            } catch (HttpResponseException e) {
                if (e.getStatusCode() == HttpConnection.HTTP_STATUS_UNAUTHORIZED)
                    return rp3.content.SyncAdapter.SYNC_EVENT_AUTH_ERROR;
                return rp3.content.SyncAdapter.SYNC_EVENT_HTTP_ERROR;
            } catch (Exception e) {
                return rp3.content.SyncAdapter.SYNC_EVENT_ERROR;
            }

        } finally {
            webService.close();
        }

        return rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS;
    }
}
