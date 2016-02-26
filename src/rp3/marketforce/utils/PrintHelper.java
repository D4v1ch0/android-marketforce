package rp3.marketforce.utils;

import android.content.Context;
import android.content.Intent;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import rp3.configuration.PreferenceManager;
import rp3.marketforce.Contants;
import rp3.marketforce.models.pedido.Pago;
import rp3.marketforce.models.pedido.Pedido;
import rp3.marketforce.models.pedido.PedidoDetalle;
import rp3.marketforce.pedido.CrearPedidoFragment;
import rp3.runtime.Session;
import rp3.util.StringUtils;

/**
 * Created by magno_000 on 26/02/2016.
 */
public class PrintHelper {
    public final static int SPACES = 36;
    private final static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public static String generarFacturaFísica(Pedido pedido, boolean reimpresion)
    {
        NumberFormat numberFormat;
        numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);
        String toPrint = "";

        toPrint = StringUtils.centerStringInLine(PreferenceManager.getString(Contants.KEY_EMPRESA), SPACES);
        toPrint = toPrint + StringUtils.centerStringInLine("Ced. Jur.:" + PreferenceManager.getString(Contants.KEY_RUC), SPACES);
        toPrint = toPrint + StringUtils.centerStringInLine("Tel:" + PreferenceManager.getString(Contants.KEY_TELEFONO), SPACES);
        toPrint = toPrint + StringUtils.centerStringInLine("Dir:" + PreferenceManager.getString(Contants.KEY_DIRECCION), SPACES);
        toPrint = toPrint + StringUtils.centerStringInLine("GUAYAQUIL - ECUADOR", SPACES);
        toPrint = toPrint + '\n';
        if(reimpresion)
            toPrint = toPrint + StringUtils.centerStringInLine("REIMPRESIÓN", SPACES);
        if(pedido.getEstado().equalsIgnoreCase("A"))
            toPrint = toPrint + StringUtils.centerStringInLine("DOCUMENTO ANULADO", SPACES);
        if(pedido.getTipoDocumento().equalsIgnoreCase("FA"))
            toPrint = toPrint + StringUtils.centerStringInLine("FACTURA", SPACES);
        if(pedido.getTipoDocumento().equalsIgnoreCase("NC"))
            toPrint = toPrint + StringUtils.centerStringInLine("NOTA DE CRÉDITO", SPACES);

        toPrint = toPrint + StringUtils.centerStringInLine("No." + pedido.getNumeroDocumento(), SPACES);
        toPrint = toPrint + '\n';
        toPrint = toPrint + StringUtils.centerStringInLine("Sr.(ES): " + pedido.getCliente().getNombreCompleto(), SPACES);
        toPrint = toPrint + StringUtils.centerStringInLine("Identif.: " + pedido.getCliente().getIdentificacion(), SPACES);
        toPrint = toPrint + StringUtils.centerStringInLine("Fecha: " + dateFormat.format(pedido.getFechaCreacion()), SPACES);
        toPrint = toPrint + '\n';

        for(int i = 1; i <= SPACES; i++)
            toPrint = toPrint + "=";
        toPrint = toPrint + '\n';
        toPrint = toPrint + "SKU               Descripción" + '\n';
        toPrint = toPrint + " P.Unit   Cant.  P.Desc    Subtotal" + '\n';
        for(int i = 1; i <= SPACES; i++)
            toPrint = toPrint + "=";
        toPrint = toPrint + '\n';

        for(PedidoDetalle detalle : pedido.getPedidoDetalles())
        {
            toPrint = toPrint + StringUtils.leftStringInSpace(detalle.getCodigoExterno(), 6) + " ";
            if(detalle.getDescripcion().length() > 28)
                toPrint = toPrint + StringUtils.leftStringInSpace(detalle.getDescripcion().substring(0,28), 28);
            else
                toPrint = toPrint + StringUtils.leftStringInSpace(detalle.getDescripcion(), 28);
            toPrint = toPrint + '\n';
            toPrint = toPrint + StringUtils.rightStringInSpace(numberFormat.format(detalle.getValorUnitario()), 10) + " ";
            toPrint = toPrint + StringUtils.rightStringInSpace(detalle.getCantidad() + "", 5) + " ";
            toPrint = toPrint + StringUtils.rightStringInSpace(numberFormat.format(detalle.getPorcentajeDescuentoManual() * 100)+ "%", 8) + " ";
            toPrint = toPrint + StringUtils.rightStringInSpace(numberFormat.format(detalle.getSubtotal()), 10) + " ";
            if(detalle.getValorImpuesto() == 0)
                toPrint = toPrint + "*";
            toPrint = toPrint + '\n';
        }

        for(int i = 1; i <= SPACES; i++)
            toPrint = toPrint + "=";
        toPrint = toPrint + '\n';
        toPrint = toPrint + StringUtils.rightStringInSpace("TOTAL A PAGAR :", 20) + " ";
        toPrint = toPrint + StringUtils.rightStringInSpace(numberFormat.format(pedido.getValorTotal()), 16) + '\n';

        toPrint = toPrint + StringUtils.centerStringInLine("RESUMEN DE TRANSACCION", SPACES);
        toPrint = toPrint + StringUtils.rightStringInSpace("Subtotal :", 20) + " ";
        toPrint = toPrint + StringUtils.rightStringInSpace(numberFormat.format(pedido.getSubtotal()), 16) + '\n';
        toPrint = toPrint + StringUtils.rightStringInSpace("Descuento :", 20) + " ";
        toPrint = toPrint + StringUtils.rightStringInSpace(numberFormat.format(pedido.getTotalDescuentos()), 16) + '\n';
        toPrint = toPrint + StringUtils.rightStringInSpace("Subtotal :", 20) + " ";
        toPrint = toPrint + StringUtils.rightStringInSpace(numberFormat.format(pedido.getSubtotal() - pedido.getTotalDescuentos()), 16) + '\n';
        toPrint = toPrint + StringUtils.rightStringInSpace("Base Imponible :", 20) + " ";
        toPrint = toPrint + StringUtils.rightStringInSpace(numberFormat.format(pedido.getBaseImponible()), 16) + '\n';
        toPrint = toPrint + StringUtils.rightStringInSpace("IVA 0% :", 20) + " ";
        toPrint = toPrint + StringUtils.rightStringInSpace(numberFormat.format(pedido.getBaseImponibleCero()), 16) + '\n';
        toPrint = toPrint + StringUtils.rightStringInSpace("13% IVA :", 20) + " ";
        toPrint = toPrint + StringUtils.rightStringInSpace(numberFormat.format(pedido.getTotalImpuestos()), 16) + '\n';
        toPrint = toPrint + StringUtils.rightStringInSpace("0% IVA :", 20) + " ";
        toPrint = toPrint + StringUtils.rightStringInSpace(numberFormat.format(0), 16) + '\n';
        toPrint = toPrint + StringUtils.rightStringInSpace("-------------------------", 36) + '\n';

        toPrint = toPrint + StringUtils.rightStringInSpace("Total :", 20) + " ";
        toPrint = toPrint + StringUtils.rightStringInSpace(numberFormat.format(pedido.getValorTotal()), 16) + '\n';

        for(int i = 1; i <= SPACES; i++)
            toPrint = toPrint + "=";
        toPrint = toPrint + '\n';
        toPrint = toPrint + '\n';

        toPrint = toPrint + StringUtils.leftStringInSpace("Total artículos : " + CrearPedidoFragment.getPedidoCantidad(pedido.getPedidoDetalles()) + "" , 35) + " ";
        toPrint = toPrint + '\n';

        double pagoEfectivo = 0;
        if(pedido.getTipoDocumento().equalsIgnoreCase("FA")) {
            for (Pago pago : pedido.getPagos()) {
                toPrint = toPrint + StringUtils.leftStringInSpace(pago.getFormaPago().getDescripcion() + ":", 20) + " ";
                toPrint = toPrint + StringUtils.rightStringInSpace(numberFormat.format(pago.getValor()), 16) + '\n';
                if (pago.getFormaPago().getDescripcion().equalsIgnoreCase("Efectivo"))
                    pagoEfectivo = pagoEfectivo + pago.getValor();
            }

            if (pagoEfectivo > 0)
                pagoEfectivo = pagoEfectivo - pedido.getExcedente();
        }
        if(pedido.getTipoDocumento().equalsIgnoreCase("NC"))
            pedido.setExcedente(0);

        toPrint = toPrint + '\n';
        toPrint = toPrint + StringUtils.leftStringInSpace("Efectivo recibido: " + numberFormat.format(pagoEfectivo), SPACES) + '\n';
        toPrint = toPrint + StringUtils.leftStringInSpace("Su cambio: " + numberFormat.format(-pedido.getExcedente()), SPACES) + '\n' + '\n';

        toPrint = toPrint + StringUtils.leftStringInSpace("Lo atendió: " + Session.getUser().getFullName(), SPACES) + '\n';

        toPrint = toPrint + StringUtils.centerStringInLine("*** GRACIAS POR SU VISITA ***", SPACES);

        return toPrint;
    }
}
