package rp3.berlin.sync;

import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.transport.HttpResponseException;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import rp3.berlin.models.pedido.PedidoTrazabilidad;
import rp3.berlin.tracking.TrackingDetailFragment;
import rp3.berlin.tracking.TrackingListFragment;
import rp3.configuration.PreferenceManager;
import rp3.connection.HttpConnection;
import rp3.connection.WebService;
import rp3.db.sqlite.DataBase;
import rp3.berlin.Contants;
import rp3.berlin.models.Agenda;
import rp3.berlin.models.pedido.Pago;
import rp3.berlin.models.pedido.PedidoDetalle;
import rp3.berlin.models.pedido.VentaPerdida;
import rp3.runtime.Session;
import rp3.sync.SyncAudit;
import rp3.util.Convert;
import rp3.util.NumberUtils;

/**
 * Created by magno_000 on 14/10/2015.
 */
public class Pedido {
    public static int executeSync(DataBase db, long idPedido) {
        WebService webService = new WebService("MartketForce", "UpdatePedido");
        webService.setTimeOut(20000);

        rp3.berlin.models.pedido.Pedido pedidoUpload = rp3.berlin.models.pedido.Pedido.getPedido(db, idPedido, true);
        /*ControlCaja controlCaja = ControlCaja.getControlCajaActiva(db);
        if(controlCaja.getIdControlCaja() == 0) {
            Caja.executeSyncInsertControl(db, controlCaja.getID());
            controlCaja = ControlCaja.getControlCajaActiva(db);
        }

        if(controlCaja.getIdControlCaja() == 0)
            return SyncAdapter.SYNC_EVENT_ERROR;*/

        JSONObject jObject = new JSONObject();
        try {
            DecimalFormat df = new DecimalFormat("#.##");
            df.setRoundingMode(RoundingMode.CEILING);
            DecimalFormat df2 = new DecimalFormat("#.####");
            df2.setRoundingMode(RoundingMode.CEILING);

            jObject.put("IdAgenda", pedidoUpload.getIdAgenda());
            jObject.put("IdRuta", PreferenceManager.getInt(Contants.KEY_IDRUTA));
            jObject.put("IdPedido", pedidoUpload.getIdPedido());
            jObject.put("IdCliente", pedidoUpload.getIdCliente());
            jObject.put("ValorTotal", removeCommas(df.format(pedidoUpload.getValorTotal())));
            jObject.put("Email", pedidoUpload.getEmail());
            jObject.put("Estado", pedidoUpload.getEstado());
            jObject.put("FechaCreacionTicks", Convert.getDotNetTicksFromDate(pedidoUpload.getFechaCreacion()));
            if(pedidoUpload.get_idAgenda() != 0) {
                rp3.berlin.models.Agenda agd = Agenda.getAgenda(db, pedidoUpload.get_idAgenda());
                if(agd.getIdAgenda() != 0) {
                    jObject.put("IdAgenda", agd.getIdAgenda());
                }
                else
                {
                    return rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS;
                }
            }
            if(pedidoUpload.getEstado().equalsIgnoreCase("A")) {
                jObject.put("Anulado", true);
                jObject.put("FecAnula", pedidoUpload.getEstado());
            }
            else
                jObject.put("Anulado", false);

            jObject.put("BaseImponible", removeCommas(df.format(pedidoUpload.getBaseImponible())));
            jObject.put("BaseImponibleCero", removeCommas(df.format(pedidoUpload.getBaseImponibleCero())));
            jObject.put("Cambio", removeCommas(df.format(pedidoUpload.getExcedente())));

            jObject.put("IdEmpresa", PreferenceManager.getInt(Contants.KEY_ID_EMPRESA));
            jObject.put("IdEstablecimiento", PreferenceManager.getInt(Contants.KEY_ID_ESTABLECIMIENTO, 0));
            jObject.put("IdMoneda", PreferenceManager.getInt(Contants.KEY_ID_MONEDA));
            jObject.put("IdNumeroCaja", PreferenceManager.getInt(Contants.KEY_ID_CAJA, 0));
            jObject.put("IdPuntoOperacion", PreferenceManager.getInt(Contants.KEY_ID_PUNTO_OPERACION, 0));
            jObject.put("NumeroDocumento", pedidoUpload.getNumeroDocumento());
            jObject.put("Observacion", pedidoUpload.getObservaciones());
            jObject.put("Redondeo", removeCommas(df.format(pedidoUpload.getRedondeo())));
            jObject.put("Subtotal", removeCommas(df.format(pedidoUpload.getSubtotal())));
            jObject.put("SubtotalSinDescuento", removeCommas(df.format(pedidoUpload.getSubtotalSinDescuento())));
            jObject.put("SubtotalSinImpuesto", removeCommas(df.format(pedidoUpload.getSubtotal() - pedidoUpload.getTotalDescuentos())));
            jObject.put("TipoTransaccion", pedidoUpload.getTipoDocumento());
            jObject.put("ValorDescAutomatico", removeCommas(df.format(pedidoUpload.getTotalDescuentos())));
            jObject.put("ValorImpuestoIvaVenta", removeCommas(df.format(pedidoUpload.getTotalImpuestos())));
            jObject.put("IdControlCaja", 0);
            jObject.put("IdDocumentoRef", pedidoUpload.getIdDocumentoRef());
            jObject.put("TotalImpuesto2", removeCommas(df.format(pedidoUpload.getTotalImpuesto2())));
            jObject.put("TotalImpuesto3", removeCommas(df.format(pedidoUpload.getTotalImpuesto3())));
            jObject.put("TotalImpuesto4", removeCommas(df.format(pedidoUpload.getTotalImpuesto4())));
            jObject.put("IdNumeroLocalSRI", 0);
            jObject.put("IdNumeroCajaSRI", 0);
            jObject.put("Serie", pedidoUpload.getSerie());
            jObject.put("TipoOrden", pedidoUpload.getTipoOrden());
            jObject.put("IdDireccion", pedidoUpload.getIdDireccion());
            jObject.put("Ciudad", pedidoUpload.getCiudad());
            /*if(pedidoUpload.getTipoDocumento().equalsIgnoreCase("FA"))
                jObject.put("Secuencia", PreferenceManager.getInt(Contants.KEY_SECUENCIA_FACTURA));
            if(pedidoUpload.getTipoDocumento().equalsIgnoreCase("NC"))
                jObject.put("Secuencia", PreferenceManager.getInt(Contants.KEY_SECUENCIA_NOTA_CREDITO));*/


            JSONArray jArrayDetalle = new JSONArray();
            for (PedidoDetalle det : pedidoUpload.getPedidoDetalles()) {
                JSONObject jObjectDetalle = new JSONObject();
                jObjectDetalle.put("IdProducto", det.getIdProducto());
                jObjectDetalle.put("Descripcion", det.getDescripcion());
                jObjectDetalle.put("IdPedido", det.getIdPedido());
                jObjectDetalle.put("IdPedidoDetalle", det.getIdPedidoDetalle());
                jObjectDetalle.put("ValorUnitario", removeCommas(df.format(det.getValorUnitario())));
                jObjectDetalle.put("Cantidad", det.getCantidad());
                jObjectDetalle.put("ValorTotal", removeCommas(df.format(det.getValorTotal())));
                jObjectDetalle.put("BaseImponible",removeCommas(df.format(det.getBaseImponible())));
                jObjectDetalle.put("BaseImponibleCero", removeCommas(df.format(det.getBaseImponibleCero())));
                //jObjectDetalle.put("IdBeneficio", det.getProducto().getIdBeneficio());
                jObjectDetalle.put("PorcDescOro", removeCommas(df.format(NumberUtils.Round(det.getPorcentajeDescuentoOro(), 2))));
                jObjectDetalle.put("ValorDescOro", removeCommas(df.format(NumberUtils.Round(det.getValorDescuentoOro(), 2))));
                jObjectDetalle.put("ValorDescOroTotal", removeCommas(df.format(NumberUtils.Round(det.getValorDescuentoOroTotal(), 2))));
                jObjectDetalle.put("PorcDescAutomatico", removeCommas(df2.format(det.getPorcentajeDescuentoAutomatico())));
                jObjectDetalle.put("PorcDescManual", removeCommas(df2.format(det.getPorcentajeDescuentoManual())));
                jObjectDetalle.put("PorcImpuestoIvaVenta", removeCommas(df2.format(det.getPorcentajeImpuesto())));
                jObjectDetalle.put("Subtotal", removeCommas(df.format(det.getSubtotal())));
                jObjectDetalle.put("SubtotalSinDescuento", removeCommas(df.format(det.getSubtotalSinDescuento())));
                jObjectDetalle.put("SubtotalSinImpuesto", removeCommas(df.format(det.getSubtotalSinImpuesto())));
                jObjectDetalle.put("ValorDescAutomatico", removeCommas(df.format(det.getValorDescuentoAutomatico())));
                jObjectDetalle.put("ValorDescAutomaticoTotal", removeCommas(df.format(det.getValorDescuentoAutomaticoTotal())));
                jObjectDetalle.put("ValorDescManual", removeCommas(df.format(det.getValorDescuentoManual())));
                jObjectDetalle.put("ValorDescManualTotal", removeCommas(df.format(det.getValorDescuentoManualTotal())));
                jObjectDetalle.put("ValorImpuestoIvaVenta", removeCommas(df.format(det.getValorImpuesto())));
                jObjectDetalle.put("ValorImpuestoIvaVentaTotal", removeCommas(df.format(det.getValorImpuestoTotal())));
                jObjectDetalle.put("BaseICE", removeCommas(df.format(det.getBaseICE())));
                jObjectDetalle.put("CantidadDevolucion", det.getCantidadDevolucion());
                jObjectDetalle.put("IdVendedor", det.getIdVendedor());
                jObjectDetalle.put("LibroPrecio", det.getLibroPrecio());
                jObjectDetalle.put("PararDescuento", det.getPararDescuento());

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
                jObjectPago.put("Valor", removeCommas(df.format(pago.getValor())));
                jObjectPago.put("ValorMoneda", 1);

                jArrayPago.put(jObjectPago);
            }

            JSONArray jArrayTrazabilidad = new JSONArray();
            for (PedidoTrazabilidad trazabilidad : pedidoUpload.getPedidoTrazabilidad()) {
                JSONObject jObjectTrazabilidad = new JSONObject();
                jObjectTrazabilidad.put("FechaTicks", Convert.getDotNetTicksFromDate(trazabilidad.getFecha()));
                jObjectTrazabilidad.put("Estado", trazabilidad.getEstado());

                jArrayTrazabilidad.put(jObjectTrazabilidad);
            }

            jObject.put("Trazabilidad", jArrayTrazabilidad);
        } catch (Exception ex) {

        }

        webService.addParameter("pedido", jObject);

        try {
            webService.addCurrentAuthToken();

            try {
                webService.invokeWebService();
                int id = webService.getIntegerResponse();
                pedidoUpload.setIdPedido(id);
                rp3.berlin.models.pedido.Pedido.update(db, pedidoUpload);
                for (PedidoDetalle det : pedidoUpload.getPedidoDetalles()) {
                    det.setIdPedido(id);
                    PedidoDetalle.update(db, det);
                }
                for (Pago pag : pedidoUpload.getPagos()) {
                    pag.setIdPedido(id);
                    Pago.update(db, pag);
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

    public static int executeSyncPendientes(DataBase db) {
        List<rp3.berlin.models.pedido.Pedido> pedidos = rp3.berlin.models.pedido.Pedido.getPedidosPendientes(db);
        if(pedidos.size() == 0)
            return rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS;

        for(rp3.berlin.models.pedido.Pedido pedidoUpload : pedidos)
        {
            WebService webService = new WebService("MartketForce", "UpdatePedido");
            webService.setTimeOut(20000);

            /*ControlCaja controlCaja = ControlCaja.getControlCajaActiva(db);
            if (controlCaja.getIdControlCaja() == 0) {
                Caja.executeSyncInsertControl(db, controlCaja.getID());
                controlCaja = ControlCaja.getControlCajaActiva(db);
            }

            if (controlCaja.getIdControlCaja() == 0)
                return SyncAdapter.SYNC_EVENT_ERROR;*/

            JSONObject jObject = new JSONObject();
            try {
                DecimalFormat df = new DecimalFormat("#.##");
                df.setRoundingMode(RoundingMode.CEILING);
                DecimalFormat df2 = new DecimalFormat("#.####");
                df2.setRoundingMode(RoundingMode.CEILING);

                jObject.put("IdAgenda", pedidoUpload.getIdAgenda());
                jObject.put("IdRuta", PreferenceManager.getInt(Contants.KEY_IDRUTA));
                jObject.put("IdPedido", pedidoUpload.getIdPedido());
                if (pedidoUpload.getIdCliente() == 0 && pedidoUpload.get_idCliente() != 0) {
                    rp3.berlin.models.Cliente cli = rp3.berlin.models.Cliente.getClienteID(db, pedidoUpload.get_idCliente(), false);
                    if(cli.getIdCliente() == 0)
                        continue;
                    else
                        jObject.put("IdCliente", cli.getIdCliente());
                }
                else {
                    jObject.put("IdCliente", pedidoUpload.getIdCliente());
                }
                jObject.put("ValorTotal", removeCommas(df.format(NumberUtils.Round(pedidoUpload.getValorTotal(), 2))));
                jObject.put("Email", pedidoUpload.getEmail());
                jObject.put("Estado", pedidoUpload.getEstado());
                jObject.put("FechaCreacionTicks", Convert.getDotNetTicksFromDate(pedidoUpload.getFechaCreacion()));
                if(pedidoUpload.get_idAgenda() != 0) {
                    rp3.berlin.models.Agenda agd = Agenda.getAgenda(db, pedidoUpload.get_idAgenda());
                    if(agd.getIdAgenda() != 0) {
                        jObject.put("IdAgenda", agd.getIdAgenda());
                    }
                    else
                    {
                        continue;
                    }
                }
                if (pedidoUpload.getEstado().equalsIgnoreCase("A")) {
                    jObject.put("Anulado", true);
                    jObject.put("FecAnula", pedidoUpload.getEstado());
                } else
                    jObject.put("Anulado", false);

                jObject.put("BaseImponible", removeCommas(df.format(NumberUtils.Round(pedidoUpload.getBaseImponible(), 2))));
                jObject.put("BaseImponibleCero", removeCommas(df.format(NumberUtils.Round(pedidoUpload.getBaseImponibleCero(), 2))));
                jObject.put("Cambio", removeCommas(df.format(NumberUtils.Round(pedidoUpload.getExcedente(), 2))));

                jObject.put("IdEmpresa", PreferenceManager.getInt(Contants.KEY_ID_EMPRESA));
                jObject.put("IdEstablecimiento", PreferenceManager.getInt(Contants.KEY_ID_ESTABLECIMIENTO, 0));
                jObject.put("IdMoneda", PreferenceManager.getInt(Contants.KEY_ID_MONEDA));
                jObject.put("IdNumeroCaja", PreferenceManager.getInt(Contants.KEY_ID_CAJA, 0));
                jObject.put("IdPuntoOperacion", PreferenceManager.getInt(Contants.KEY_ID_PUNTO_OPERACION, 0));
                jObject.put("NumeroDocumento", pedidoUpload.getNumeroDocumento());
                jObject.put("Observacion", pedidoUpload.getObservaciones());
                jObject.put("Redondeo", removeCommas(df.format(NumberUtils.Round(pedidoUpload.getRedondeo(), 2))));
                jObject.put("Subtotal", removeCommas(df.format(NumberUtils.Round(pedidoUpload.getSubtotal(), 2))));
                jObject.put("SubtotalSinDescuento", removeCommas(df.format(NumberUtils.Round(pedidoUpload.getSubtotalSinDescuento(), 2))));
                jObject.put("SubtotalSinImpuesto", removeCommas(df.format(NumberUtils.Round(pedidoUpload.getSubtotal() - pedidoUpload.getTotalDescuentos(), 2))));
                jObject.put("TipoTransaccion", pedidoUpload.getTipoDocumento());
                jObject.put("ValorDescAutomatico", removeCommas(df.format(NumberUtils.Round(pedidoUpload.getTotalDescuentos(), 2))));
                jObject.put("ValorImpuestoIvaVenta", removeCommas(df.format(NumberUtils.Round(pedidoUpload.getTotalImpuestos(), 2))));
                /*ControlCaja controlCaja = ControlCaja.getControlCaja(db, pedidoUpload.get_idControlCaja());
                if (controlCaja.getIdControlCaja() == 0) {
                    Caja.executeSyncInsertControl(db, controlCaja.getID());
                    controlCaja = ControlCaja.getControlCaja(db, pedidoUpload.get_idControlCaja());
                }

                if (controlCaja.getIdControlCaja() == 0)
                    continue;*/
                jObject.put("IdControlCaja", 0);
                if(pedidoUpload.getTipoDocumento().equalsIgnoreCase("NC") && pedidoUpload.getIdDocumentoRef() == 0)
                {
                    rp3.berlin.models.pedido.Pedido pedidoRef = rp3.berlin.models.pedido.Pedido.getPedido(db, pedidoUpload.get_idDocumentoRef(), false);
                    if(pedidoRef.getIdPedido() == 0)
                        continue;
                    jObject.put("IdDocumentoRef", pedidoRef.getIdPedido());
                }
                else {
                    jObject.put("IdDocumentoRef", pedidoUpload.getIdDocumentoRef());
                }
                jObject.put("TotalImpuesto2", removeCommas(df.format(NumberUtils.Round(pedidoUpload.getTotalImpuesto2(), 2))));
                jObject.put("TotalImpuesto3", removeCommas(df.format(NumberUtils.Round(pedidoUpload.getTotalImpuesto3(), 2))));
                jObject.put("TotalImpuesto4", removeCommas(df.format(NumberUtils.Round(pedidoUpload.getTotalImpuesto4(), 2))));
                jObject.put("IdNumeroLocalSRI", 0);
                jObject.put("IdNumeroCajaSRI", 0);
                jObject.put("Serie", pedidoUpload.getSerie());
                jObject.put("TipoOrden", pedidoUpload.getTipoOrden());
                jObject.put("IdDireccion", pedidoUpload.getIdDireccion());
                jObject.put("Ciudad", pedidoUpload.getCiudad());
                /*if (pedidoUpload.getTipoDocumento().equalsIgnoreCase("FA"))
                    jObject.put("Secuencia", PreferenceManager.getInt(Contants.KEY_SECUENCIA_FACTURA));
                if (pedidoUpload.getTipoDocumento().equalsIgnoreCase("NC"))
                    jObject.put("Secuencia", PreferenceManager.getInt(Contants.KEY_SECUENCIA_NOTA_CREDITO));
                if (pedidoUpload.getTipoDocumento().equalsIgnoreCase("CT"))
                    jObject.put("Secuencia", PreferenceManager.getInt(Contants.KEY_SECUENCIA_COTIZACION));
                if (pedidoUpload.getTipoDocumento().equalsIgnoreCase("PD"))
                    jObject.put("Secuencia", PreferenceManager.getInt(Contants.KEY_SECUENCIA_PEDIDO));*/


                JSONArray jArrayDetalle = new JSONArray();
                for (PedidoDetalle det : pedidoUpload.getPedidoDetalles()) {
                    JSONObject jObjectDetalle = new JSONObject();
                    jObjectDetalle.put("IdProducto", det.getIdProducto());
                    jObjectDetalle.put("Descripcion", det.getDescripcion());
                    jObjectDetalle.put("IdPedido", det.getIdPedido());
                    jObjectDetalle.put("IdPedidoDetalle", det.getIdPedidoDetalle());
                    jObjectDetalle.put("ValorUnitario", removeCommas(df.format(det.getValorUnitario())));
                    jObjectDetalle.put("Cantidad", det.getCantidad());
                    jObjectDetalle.put("ValorTotal", removeCommas(df.format(NumberUtils.Round(det.getValorTotal(), 2))));
                    jObjectDetalle.put("BaseImponible", removeCommas(df.format(NumberUtils.Round(det.getBaseImponible(), 2))));
                    jObjectDetalle.put("BaseImponibleCero", removeCommas(df.format(NumberUtils.Round(det.getBaseImponibleCero(), 2))));
                    jObjectDetalle.put("IdBeneficio", det.getIdBeneficio());
                    jObjectDetalle.put("ValorDescOro", removeCommas(df.format(NumberUtils.Round(det.getValorDescuentoOro(), 2))));
                    if(det.getValorDescuentoOro() == 0)
                        jObjectDetalle.put("PorcDescOro", 0);
                    else
                        jObjectDetalle.put("PorcDescOro", removeCommas(df.format(NumberUtils.Round(det.getPorcentajeDescuentoOro(), 2))));

                    jObjectDetalle.put("ValorDescOroTotal", removeCommas(df.format(NumberUtils.Round(det.getValorDescuentoOroTotal(), 2))));

                    jObjectDetalle.put("ValorDescAutomatico", removeCommas(df.format(NumberUtils.Round(det.getValorDescuentoAutomatico(), 2))));
                    if(det.getValorDescuentoAutomatico() == 0)
                        jObjectDetalle.put("PorcDescAutomatico", 0);
                    else
                        jObjectDetalle.put("PorcDescAutomatico", removeCommas(df2.format(det.getPorcentajeDescuentoAutomatico())));

                    jObjectDetalle.put("PorcDescManual", removeCommas(df2.format(det.getPorcentajeDescuentoManual())));
                    jObjectDetalle.put("PorcImpuestoIvaVenta", removeCommas(df2.format(NumberUtils.Round(det.getPorcentajeImpuesto(), 2))));
                    jObjectDetalle.put("Subtotal", removeCommas(df.format(NumberUtils.Round(det.getSubtotal(), 2))));
                    jObjectDetalle.put("SubtotalSinDescuento", removeCommas(df.format(NumberUtils.Round(det.getSubtotalSinDescuento(), 2))));
                    jObjectDetalle.put("SubtotalSinImpuesto", removeCommas(df.format(NumberUtils.Round(det.getSubtotalSinImpuesto(), 2))));

                    jObjectDetalle.put("ValorDescAutomaticoTotal", removeCommas(df.format(NumberUtils.Round(det.getValorDescuentoAutomaticoTotal(), 2))));
                    jObjectDetalle.put("ValorDescManual", removeCommas(df.format(NumberUtils.Round(det.getValorDescuentoManual(), 2))));
                    jObjectDetalle.put("ValorDescManualTotal", removeCommas(df.format(NumberUtils.Round(det.getValorDescuentoManualTotal(), 2))));
                    jObjectDetalle.put("ValorImpuestoIvaVenta", removeCommas(df.format(NumberUtils.Round(det.getValorImpuesto(), 2))));
                    jObjectDetalle.put("ValorImpuestoIvaVentaTotal", removeCommas(df.format(NumberUtils.Round(det.getValorImpuestoTotal(), 2))));
                    jObjectDetalle.put("BaseICE", removeCommas(df.format(NumberUtils.Round(det.getBaseICE(), 2))));
                    jObjectDetalle.put("UsrAutorizaDescManual", det.getUsrDescManual());
                    jObjectDetalle.put("CantidadDevolucion", det.getCantidadDevolucion());
                    jObjectDetalle.put("IdVendedor", det.getIdVendedor());
                    jObjectDetalle.put("LibroPrecio", det.getLibroPrecio());
                    jObjectDetalle.put("PararDescuento", det.getPararDescuento());

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
                    jObjectPago.put("Valor", removeCommas(df.format(pago.getValor())));
                    jObjectPago.put("ValorMoneda", 1);
                    if(pago.getCodigoSeguridad() != 0)
                        jObjectPago.put("CodigoSeguridad", pago.getCodigoSeguridad());
                    if(pago.getNumeroDocumento() != null && pago.getNumeroDocumento().trim().length() > 0)
                        jObjectPago.put("NumeroDocumento", pago.getNumeroDocumento());
                    if(pago.getAutorizadorTarjeta() != 0)
                        jObjectPago.put("AutorizadorTarjeta", pago.getAutorizadorTarjeta());
                    if(pago.getIdBanco() != 0)
                        jObjectPago.put("IdBanco", pago.getIdBanco());
                    if(pago.getIdMarcaTarjeta() != 0)
                        jObjectPago.put("IdMarcaTarjeta", pago.getIdMarcaTarjeta());
                    if(pago.getNumeroCuenta() != null && pago.getNumeroCuenta().trim().length() > 0)
                        jObjectPago.put("NumeroCuenta", pago.getNumeroCuenta());
                    if(pago.getIdTipoDiferido() != 0)
                        jObjectPago.put("IdTipoDiferido", pago.getIdTipoDiferido());


                    jArrayPago.put(jObjectPago);
                }

                jObject.put("Pagos", jArrayPago);

                JSONArray jArrayTrazabilidad = new JSONArray();
                for (PedidoTrazabilidad trazabilidad : pedidoUpload.getPedidoTrazabilidad()) {
                    JSONObject jObjectTrazabilidad = new JSONObject();
                    jObjectTrazabilidad.put("FechaTicks", Convert.getDotNetTicksFromDate(trazabilidad.getFecha()));
                    jObjectTrazabilidad.put("Estado", trazabilidad.getEstado());

                    jArrayTrazabilidad.put(jObjectTrazabilidad);
                }

                jObject.put("Trazabilidad", jArrayTrazabilidad);
            } catch (Exception ex) {

            }

            webService.addParameter("pedido", jObject);

            try {
                webService.addCurrentAuthToken();

                try {
                    webService.invokeWebService();
                    int id = webService.getIntegerResponse();
                    pedidoUpload.setIdPedido(id);
                    rp3.berlin.models.pedido.Pedido.update(db, pedidoUpload);
                    for (PedidoDetalle det : pedidoUpload.getPedidoDetalles()) {
                        det.setIdPedido(id);
                        PedidoDetalle.update(db, det);
                    }
                    for (Pago pag : pedidoUpload.getPagos()) {
                        pag.setIdPedido(id);
                        Pago.update(db, pag);
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
        }

        return rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS;
    }

    public static int executeSyncCancelar(DataBase db, long idPedido) {
        WebService webService = new WebService("MartketForce", "CancelarNC");
        webService.setTimeOut(20000);

        rp3.berlin.models.pedido.Pedido pedidoUpload = rp3.berlin.models.pedido.Pedido.getPedido(db, idPedido, false);

        JSONObject jObject = new JSONObject();
        try {
            DecimalFormat df = new DecimalFormat("#.##");
            df.setRoundingMode(RoundingMode.CEILING);

            jObject.put("IdPedido", pedidoUpload.getIdPedido());
            jObject.put("NumeroDocumento", pedidoUpload.getNumeroDocumento());
            //jObject.put("MotivoAnulacion", pedidoUpload.getMotivoAnulacion());
            //jObject.put("ObservacionAnulacion", pedidoUpload.getObservacionAnulacion());
            //jObject.put("FecAnulaTicks",  Convert.getDotNetTicksFromDate(pedidoUpload.getFechaAnulacion()));

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

    public static int executeSyncAnular(DataBase db, long idPedido) {
        WebService webService = new WebService("MartketForce", "AnularPedido");
        webService.setTimeOut(20000);

        rp3.berlin.models.pedido.Pedido pedidoUpload = rp3.berlin.models.pedido.Pedido.getPedido(db, idPedido, false);

        JSONObject jObject = new JSONObject();
        try {
            DecimalFormat df = new DecimalFormat("#.##");
            df.setRoundingMode(RoundingMode.CEILING);

            jObject.put("IdPedido", pedidoUpload.getIdPedido());
            jObject.put("MotivoAnulacion", pedidoUpload.getMotivoAnulacion());
            jObject.put("ObservacionAnulacion", pedidoUpload.getObservacionAnulacion());
            jObject.put("FecAnulaTicks",  Convert.getDotNetTicksFromDate(pedidoUpload.getFechaAnulacion()));

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

    public static int executeSyncDocRef(DataBase db){
        WebService webService = new WebService("MartketForce","GetDocRef");
        Calendar fechaUlt = Calendar.getInstance();
        fechaUlt.setTime(SyncAudit.getLastSyncDate(rp3.berlin.sync.SyncAdapter.SYNC_TYPE_DOC_REF, rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS));
        fechaUlt.add(Calendar.MINUTE, -30);
        long fecha = rp3.util.Convert.getDotNetTicksFromDate(fechaUlt.getTime());
        try
        {
            webService.addParameter("@ultimaFechaActualizacion", fecha);
            webService.addCurrentAuthToken();

            try {
                webService.invokeWebService();
            } catch (HttpResponseException e) {
                if(e.getStatusCode() == HttpConnection.HTTP_STATUS_UNAUTHORIZED)
                    return rp3.content.SyncAdapter.SYNC_EVENT_AUTH_ERROR;
                return rp3.content.SyncAdapter.SYNC_EVENT_HTTP_ERROR;
            } catch (Exception e) {
                return rp3.content.SyncAdapter.SYNC_EVENT_ERROR;
            }

            JSONArray types = webService.getJSONArrayResponse();

            for(int i=0; i < types.length(); i++){
                try {

                    JSONObject type = types.getJSONObject(i);

                    rp3.berlin.models.pedido.Pedido pedido = rp3.berlin.models.pedido.Pedido.getPedidoByIdServer(db, type.getInt("IdPedido"));

                    pedido.setTieneNotaCreditoRP3POS(type.getBoolean("TieneNotaCreditoPOSRP3"));

                    rp3.berlin.models.pedido.Pedido.update(db, pedido);

                } catch (JSONException e) {
                    Log.e("Entro", "Error: " + e.toString());
                    return rp3.content.SyncAdapter.SYNC_EVENT_ERROR;
                }
            }
        }finally{
            webService.close();
        }
        return rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS;
    }

    public static int executeSyncVentasPerdidas(DataBase db) {
        WebService webService = new WebService("MartketForce", "InsertVentaPerdida");

        List<VentaPerdida> ventas = VentaPerdida.getPendientes(db);
        if (ventas.size() == 0)
            return rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS;

        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        JSONArray jArray = new JSONArray();
        for (int i = 0; i < ventas.size(); i++) {
            VentaPerdida ventaUpload = ventas.get(i);
            JSONObject jObject = new JSONObject();
            try {
                jObject.put("CodigoProducto", ventaUpload.getCodigoProducto());
                jObject.put("Vendedor", Session.getUser().getLogonName());
                jObject.put("FecIngTicks", Convert.getDotNetTicksFromDate(ventaUpload.getFecha()));

                jArray.put(jObject);
            } catch (Exception ex) {

            }
        }

        webService.addParameter("ventas", jArray);

        try {
            webService.addCurrentAuthToken();

            try {
                webService.invokeWebService();
                for (int i = 0; i < ventas.size(); i++) {
                    VentaPerdida ventaUpload = ventas.get(i);
                    ventaUpload.setPendiente(false);
                    VentaPerdida.update(db, ventaUpload);
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

    public static Bundle executeSyncGetPedido(long fechaInicio, long fechaFin, String cliente, String estado, String infor){
        Bundle resp = new Bundle();
        WebService webService = new WebService("MartketForce","GetPedido");
        try
        {
            estado = estado.replace(" ", "%20");
            webService.addParameter("@fechaInicioTicks", fechaInicio);
            webService.addParameter("@fechaFinTicks", fechaFin);
            webService.addParameter("@cliente", cliente);
            webService.addParameter("@estado", estado);
            if(infor.equalsIgnoreCase("Si"))
                webService.addParameter("@estaInfor", true);
            if(infor.equalsIgnoreCase("No"))
                webService.addParameter("@estaInfor", false);
            webService.addCurrentAuthToken();

            try {
                webService.invokeWebService();
            } catch (HttpResponseException e) {
                if(e.getStatusCode() == HttpConnection.HTTP_STATUS_UNAUTHORIZED) {
                    resp.putInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE, rp3.content.SyncAdapter.SYNC_EVENT_AUTH_ERROR);
                }
                resp.putInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE,rp3.content.SyncAdapter.SYNC_EVENT_HTTP_ERROR);
                return resp;
            } catch (Exception e) {
                resp.putInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE,rp3.content.SyncAdapter.SYNC_EVENT_ERROR);
                return resp;
            }

            JSONArray types = webService.getJSONArrayResponse();
            if(types != null)
                resp.putString(TrackingListFragment.ARG_PEDIDOS, types.toString());
            else
                resp.putString(TrackingListFragment.ARG_PEDIDOS, "");
            resp.putInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE,rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS);
        }finally{
            webService.close();
        }
        return resp;
    }

    public static Bundle executeSyncGetInfoPedido(long idPedido){
        Bundle resp = new Bundle();
        WebService webService = new WebService("MartketForce","GetTrazabilidad");
        try
        {
            webService.addParameter("@idPedido", idPedido);
            webService.addCurrentAuthToken();

            try {
                webService.invokeWebService();
            } catch (HttpResponseException e) {
                if(e.getStatusCode() == HttpConnection.HTTP_STATUS_UNAUTHORIZED) {
                    resp.putInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE, rp3.content.SyncAdapter.SYNC_EVENT_AUTH_ERROR);
                }
                resp.putInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE,rp3.content.SyncAdapter.SYNC_EVENT_HTTP_ERROR);
                return resp;
            } catch (Exception e) {
                resp.putInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE,rp3.content.SyncAdapter.SYNC_EVENT_ERROR);
                return resp;
            }

            JSONArray types = webService.getJSONArrayResponse();
            if(types != null)
                resp.putString(TrackingDetailFragment.ARG_ITEM_ID, types.toString());
            else
                resp.putString(TrackingDetailFragment.ARG_ITEM_ID, "");
            resp.putInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE,rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS);
        }finally{
            webService.close();
        }
        return resp;
    }

    private static String removeCommas(String value)
    {
        return value.replace(",",".");
    }
}
