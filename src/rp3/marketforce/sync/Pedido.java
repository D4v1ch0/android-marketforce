package rp3.marketforce.sync;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.transport.HttpResponseException;

import rp3.configuration.PreferenceManager;
import rp3.connection.HttpConnection;
import rp3.connection.WebService;
import rp3.content.*;
import rp3.db.sqlite.DataBase;
import rp3.marketforce.Contants;
import rp3.marketforce.models.AgendaTarea;
import rp3.marketforce.models.AgendaTareaActividades;
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
            jObject.put("IdAgenda", pedidoUpload.getIdAgenda());
            jObject.put("IdRuta", PreferenceManager.getInt(Contants.KEY_IDRUTA));
            jObject.put("IdPedido", pedidoUpload.getIdPedido());
            jObject.put("IdCliente", pedidoUpload.getIdCliente());
            jObject.put("ValorTotal", pedidoUpload.getValorTotal());
            jObject.put("Email", pedidoUpload.getEmail());
            jObject.put("Estado", pedidoUpload.getEstado());
            jObject.put("FechaCreacionTicks", Convert.getDotNetTicksFromDate(pedidoUpload.getFechaCreacion()));

            JSONArray jArrayDetalle = new JSONArray();
            for (PedidoDetalle det : pedidoUpload.getPedidoDetalles()) {
                JSONObject jObjectDetalle = new JSONObject();
                jObjectDetalle.put("IdProducto", det.getIdProducto());
                jObjectDetalle.put("Descripcion", det.getDescripcion());
                jObjectDetalle.put("IdPedido", det.getIdPedido());
                jObjectDetalle.put("IdPedidoDetalle", det.getIdPedidoDetalle());
                jObjectDetalle.put("ValorUnitario", det.getValorUnitario());
                jObjectDetalle.put("Cantidad", det.getCantidad());
                jObjectDetalle.put("ValorTotal", det.getValorTotal());

                jArrayDetalle.put(jObjectDetalle);
            }

            jObject.put("PedidoDetalles", jArrayDetalle);
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
}
