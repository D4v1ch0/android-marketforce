package rp3.marketforce.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.starmicronics.stario.StarPrinterStatus;

import java.text.Normalizer;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.regex.Pattern;

import rp3.configuration.PreferenceManager;
import rp3.db.sqlite.DataBase;
import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.models.pedido.ControlCaja;
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
    public final static int SPACES = 31;
    private final static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public static String generarFacturaFísica(Pedido pedido, boolean reimpresion)
    {
        NumberFormat numberFormat;
        numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);
        String toPrint = "";

        toPrint = StringUtils.centerStringInLine(PreferenceManager.getString(Contants.KEY_EMPRESA), SPACES);
        toPrint = toPrint + StringUtils.centerStringInLine("Ced. Jur.:" + PreferenceManager.getString(Contants.KEY_RUC).trim(), SPACES);
        toPrint = toPrint + StringUtils.centerStringInLine("Tel:" + PreferenceManager.getString(Contants.KEY_TELEFONO).trim(), SPACES);
        toPrint = toPrint + StringUtils.centerStringInLine("Dir:" + PreferenceManager.getString(Contants.KEY_DIRECCION).trim(), SPACES);
        //toPrint = toPrint + StringUtils.centerStringInLine("GUAYAQUIL - ECUADOR", SPACES);
        toPrint = toPrint + '\n';
        if(reimpresion)
            toPrint = toPrint + StringUtils.centerStringInLine("REIMPRESIÓN", SPACES);
        if(pedido.getEstado().equalsIgnoreCase("A"))
            toPrint = toPrint + StringUtils.centerStringInLine("DOCUMENTO ANULADO", SPACES);
        if(pedido.getEstado().equalsIgnoreCase("N"))
            toPrint = toPrint + StringUtils.centerStringInLine("DOCUMENTO CANCELADO", SPACES);

        toPrint = toPrint + StringUtils.centerStringInLine(pedido.getTransaccion().getValue().toUpperCase(), SPACES);

        toPrint = toPrint + StringUtils.centerStringInLine("No." + pedido.getNumeroDocumento(), SPACES);
        toPrint = toPrint + '\n';
        if(pedido.get_idCliente() != 0) {
            toPrint = toPrint + StringUtils.centerStringInLine("Sr.(ES): " + pedido.getCliente().getNombreCompleto(), SPACES);
            toPrint = toPrint + StringUtils.centerStringInLine("Identif.: " + pedido.getCliente().getIdentificacion(), SPACES);
        }
        else
        {
            toPrint = toPrint + StringUtils.centerStringInLine("Sr.(ES): Consumidor Final", SPACES);
            toPrint = toPrint + StringUtils.centerStringInLine("Identif.: 9999999999", SPACES);
        }
        toPrint = toPrint + StringUtils.centerStringInLine("Fecha: " + dateFormat.format(pedido.getFechaCreacion()), SPACES);
        toPrint = toPrint + '\n';

        for(int i = 1; i <= SPACES; i++)
            toPrint = toPrint + "=";
        toPrint = toPrint + '\n';
        toPrint = toPrint + "SKU            Descripción" + '\n';
        toPrint = toPrint + " P.Unit         Cant." + '\n';
        toPrint = toPrint + "P.Desc         Subtotal" + '\n';
        for(int i = 1; i <= SPACES; i++)
            toPrint = toPrint + "=";
        toPrint = toPrint + '\n';

        for(PedidoDetalle detalle : pedido.getPedidoDetalles())
        {
            toPrint = toPrint + StringUtils.leftStringInSpace(detalle.getCodigoExterno(), 6) + " ";
            if(detalle.getDescripcion().length() > 23)
                toPrint = toPrint + StringUtils.leftStringInSpace(detalle.getDescripcion().substring(0,23), 23);
            else
                toPrint = toPrint + StringUtils.leftStringInSpace(detalle.getDescripcion(), 23);
            toPrint = toPrint + '\n';
            toPrint = toPrint + StringUtils.rightStringInSpace(numberFormat.format(detalle.getValorUnitario()), 14) + " ";
            toPrint = toPrint + StringUtils.rightStringInSpace(detalle.getCantidad() + "", 5) + " ";
            toPrint = toPrint + '\n';
            toPrint = toPrint + StringUtils.rightStringInSpace(numberFormat.format(detalle.getPorcentajeDescuentoManual() * 100)+ "%", 9) + " ";
            toPrint = toPrint + StringUtils.rightStringInSpace(numberFormat.format(detalle.getSubtotal()), 17) + " ";
            if(detalle.getValorImpuesto() == 0)
                toPrint = toPrint + "*";
            toPrint = toPrint + '\n';
        }

        for(int i = 1; i <= SPACES; i++)
            toPrint = toPrint + "=";
        toPrint = toPrint + '\n';
        toPrint = toPrint + StringUtils.rightStringInSpace("TOTAL A PAGAR :", 18) + " ";
        toPrint = toPrint + StringUtils.rightStringInSpace(numberFormat.format(pedido.getValorTotal()), 12) + '\n';

        toPrint = toPrint + StringUtils.centerStringInLine("RESUMEN DE TRANSACCION", SPACES);
        toPrint = toPrint + StringUtils.rightStringInSpace("Subtotal :", 18) + " ";
        toPrint = toPrint + StringUtils.rightStringInSpace(numberFormat.format(pedido.getSubtotal()), 12) + '\n';
        toPrint = toPrint + StringUtils.rightStringInSpace("Descuento :", 18) + " ";
        toPrint = toPrint + StringUtils.rightStringInSpace(numberFormat.format(pedido.getTotalDescuentos()), 12) + '\n';
        toPrint = toPrint + StringUtils.rightStringInSpace("Subtotal :", 18) + " ";
        toPrint = toPrint + StringUtils.rightStringInSpace(numberFormat.format(pedido.getSubtotal() - pedido.getTotalDescuentos()), 12) + '\n';
        toPrint = toPrint + StringUtils.rightStringInSpace("Base Imponible :", 18) + " ";
        toPrint = toPrint + StringUtils.rightStringInSpace(numberFormat.format(pedido.getBaseImponible()), 12) + '\n';
        toPrint = toPrint + StringUtils.rightStringInSpace("IVA 0% :", 18) + " ";
        toPrint = toPrint + StringUtils.rightStringInSpace(numberFormat.format(pedido.getBaseImponibleCero()), 12) + '\n';
        toPrint = toPrint + StringUtils.rightStringInSpace("13% IVA :", 18) + " ";
        toPrint = toPrint + StringUtils.rightStringInSpace(numberFormat.format(pedido.getTotalImpuestos()), 12) + '\n';
        toPrint = toPrint + StringUtils.rightStringInSpace("0% IVA :", 18) + " ";
        toPrint = toPrint + StringUtils.rightStringInSpace(numberFormat.format(0), 12) + '\n';
        toPrint = toPrint + StringUtils.rightStringInSpace("Redondeo :", 18) + " ";
        toPrint = toPrint + StringUtils.rightStringInSpace(numberFormat.format(pedido.getRedondeo()), 12) + '\n';
        toPrint = toPrint + StringUtils.rightStringInSpace("-------------------------", SPACES) + '\n';

        toPrint = toPrint + StringUtils.rightStringInSpace("Total :", 18) + " ";
        toPrint = toPrint + StringUtils.rightStringInSpace(numberFormat.format(pedido.getValorTotal()), 12) + '\n';

        for(int i = 1; i <= SPACES; i++)
            toPrint = toPrint + "=";
        toPrint = toPrint + '\n';
        toPrint = toPrint + '\n';

        toPrint = toPrint + StringUtils.leftStringInSpace("Total artículos : " + CrearPedidoFragment.getPedidoCantidad(pedido.getPedidoDetalles()) + "" , 29) + " ";
        toPrint = toPrint + '\n';

        double pagoEfectivo = 0;
        if(pedido.getTipoDocumento().equalsIgnoreCase("FA")) {
            for (Pago pago : pedido.getPagos()) {
                if(pago.getFormaPago().getDescripcion().length() > 17)
                    toPrint = toPrint + StringUtils.leftStringInSpace(pago.getFormaPago().getDescripcion().substring(0,17) + ":", 18);
                else
                    toPrint = toPrint + StringUtils.leftStringInSpace(pago.getFormaPago().getDescripcion()+ ":", 18);

                toPrint = toPrint + StringUtils.rightStringInSpace(numberFormat.format(pago.getValor()), 13) + '\n';
                if (pago.getFormaPago().getDescripcion().equalsIgnoreCase("Efectivo"))
                    pagoEfectivo = pagoEfectivo + pago.getValor();
            }

            if (pagoEfectivo > 0)
                pagoEfectivo = pagoEfectivo - pedido.getExcedente();
        }
        if(pedido.getTipoDocumento().equalsIgnoreCase("NC"))
            pedido.setExcedente(0);

        toPrint = toPrint + '\n';
        if(pedido.getTipoDocumento().equalsIgnoreCase("FA")) {
            toPrint = toPrint + StringUtils.leftStringInSpace("Efectivo recibido: " + numberFormat.format(pagoEfectivo), SPACES) + '\n';
            toPrint = toPrint + StringUtils.leftStringInSpace("Su cambio: " + numberFormat.format(-pedido.getExcedente()), SPACES) + '\n' + '\n';
        }

        if(pedido.getTipoDocumento().equalsIgnoreCase("CT"))
        {
            toPrint = toPrint + StringUtils.leftStringInSpace("Vigencia: 15 día(s)", SPACES) + '\n';
        }

        toPrint = toPrint + StringUtils.leftStringInSpace("Lo atendió: " + Session.getUser().getFullName(), SPACES) + '\n';

        toPrint = toPrint + StringUtils.centerStringInLine("*** GRACIAS POR SU VISITA ***", SPACES);
        toPrint = toPrint + '\n';

        return removeASCII(toPrint);
    }

    public static String generarArqueo(List<Pago> pagos, ControlCaja control)
    {
        NumberFormat numberFormat;
        SimpleDateFormat format1, format2, format3, format5, format6;
        numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);

        format1 = new SimpleDateFormat("dd/MMM/yyyy");
        format2 = new SimpleDateFormat("dd");
        format3 = new SimpleDateFormat("MMMM");
        format5 = new SimpleDateFormat("yyyy");
        format6 = new SimpleDateFormat("HH:mm");
        String toPrint = "";

        toPrint = toPrint + StringUtils.centerStringInLine("ARQUEO DE CAJA", SPACES);
        toPrint = toPrint + '\n';
        toPrint = toPrint + StringUtils.centerStringInLine("Fecha Apertura:", SPACES);
        toPrint = toPrint + StringUtils.centerStringInLine(format1.format(control.getFechaApertura()) + " - " + format6.format(control.getFechaApertura()), SPACES);
        toPrint = toPrint + StringUtils.centerStringInLine("Aperturado por:" + Session.getUser().getLogonName(), SPACES);
        toPrint = toPrint + '\n';
        if(control.getFechaCierre() != null && control.getFechaCierre().getTime() > 0)
        {
            toPrint = toPrint + StringUtils.centerStringInLine("Fecha Cierre:", SPACES);
            toPrint = toPrint + StringUtils.centerStringInLine(format1.format(control.getFechaCierre()) + " - " + format6.format(control.getFechaCierre()), SPACES);
            toPrint = toPrint + StringUtils.centerStringInLine("Cerrado por:" + Session.getUser().getLogonName(), SPACES);
        }

        toPrint = toPrint + '\n';


        for(int i = 1; i <= SPACES; i++)
            toPrint = toPrint + "=";

        toPrint = toPrint + '\n';
        toPrint = toPrint + "Tipo Pago   # Pagos     Total" + '\n';
        for(int i = 1; i <= SPACES; i++)
            toPrint = toPrint + "=";
        toPrint = toPrint + '\n';

        int cantidad = 0;
        double valor = 0;
        for(Pago pago: pagos) {
            if(pago.getIdFormaPago() > - 2) {
                cantidad = cantidad + pago.getIdPago();
                valor = valor + pago.getValor();
            }

            if(pago.getIdFormaPago() == 0)
                toPrint = toPrint + StringUtils.leftStringInSpace("(-) NC", 12) + " ";
            else if(pago.getIdFormaPago() == -1)
                toPrint = toPrint + StringUtils.leftStringInSpace("Apertura", 12) + " ";
            else if(pago.getIdFormaPago() == -2) {
                int linea = 1;
                if(pago.getBancoDescripcion().length() > 11) {
                    toPrint = toPrint + StringUtils.leftStringInSpace(" " + (pago.getBancoDescripcion()).substring(0, 11), 12) + " ";
                    toPrint = toPrint + '\n';
                    while (pago.getBancoDescripcion().length() > (linea + 1) * 11) {
                        toPrint = toPrint + StringUtils.leftStringInSpace(" " + (pago.getBancoDescripcion()).substring(linea * 11, (linea + 1) * 11), 12) + " ";
                        linea++;
                        toPrint = toPrint + '\n';
                    }
                    toPrint = toPrint + StringUtils.leftStringInSpace(" " + (pago.getBancoDescripcion()).substring(linea * 11, pago.getBancoDescripcion().length()), 12) + " ";
                }
                else
                    toPrint = toPrint + StringUtils.leftStringInSpace(" " + pago.getBancoDescripcion(), 12) + " ";
            }
            else if(pago.getIdFormaPago() == -3) {
                int linea = 1;
                if((pago.getBancoDescripcion() + " - " + pago.getMarcaTarjetaDescripcion()).length() > 12) {
                    toPrint = toPrint + StringUtils.leftStringInSpace(" " + (pago.getBancoDescripcion() + " - " + pago.getMarcaTarjetaDescripcion()).substring(0, 11), 12) + " ";
                    toPrint = toPrint + '\n';
                    while ((pago.getBancoDescripcion() + " - " + pago.getMarcaTarjetaDescripcion()).length() > (linea + 1) * 11) {
                        toPrint = toPrint + StringUtils.leftStringInSpace(" " + (pago.getBancoDescripcion() + " - " + pago.getMarcaTarjetaDescripcion()).substring(linea * 11, (linea + 1) * 11), 12) + " ";
                        linea++;
                        toPrint = toPrint + '\n';
                    }
                    toPrint = toPrint + StringUtils.leftStringInSpace(" " + (pago.getBancoDescripcion() + " - " + pago.getMarcaTarjetaDescripcion()).substring(linea * 11, (pago.getBancoDescripcion() + " - " + pago.getMarcaTarjetaDescripcion()).length()), 12) + " ";
                }
                else
                    toPrint = toPrint + StringUtils.leftStringInSpace(" " + pago.getBancoDescripcion() + " - " + pago.getMarcaTarjetaDescripcion(), 12) + " ";
            }
            else if(pago.getFormaPago().getDescripcion().length() > 12)
                toPrint = toPrint + StringUtils.leftStringInSpace(pago.getFormaPago().getDescripcion().substring(0,12), 12) + " ";
            else
                toPrint = toPrint + StringUtils.leftStringInSpace(pago.getFormaPago().getDescripcion(), 12) + " ";

            toPrint = toPrint + StringUtils.rightStringInSpace(pago.getIdPago() + "" , 5) + " ";
            toPrint = toPrint + StringUtils.rightStringInSpace(numberFormat.format(pago.getValor()), 12);
            toPrint = toPrint + '\n';
        }

        for(int i = 1; i <= SPACES; i++)
            toPrint = toPrint + "=";
        toPrint = toPrint + '\n';
        toPrint = toPrint + StringUtils.leftStringInSpace("Total", 12) + " ";
        toPrint = toPrint + StringUtils.rightStringInSpace(cantidad + "" , 5) + " ";
        toPrint = toPrint + StringUtils.rightStringInSpace(numberFormat.format(valor), 12);
        toPrint = toPrint + '\n';

        return removeASCII(toPrint);
    }

    public static String removeASCII(String input) {
        // Cadena de caracteres original a sustituir.
        String original = "áàäéèëíìïóòöúùuñÁÀÄÉÈËÍÌÏÓÒÖÚÙÜÑçÇ";
        // Cadena de caracteres ASCII que reemplazarán los originales.
        String ascii = "aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcC";
        String output = input;
        for (int i=0; i<original.length(); i++) {
            // Reemplazamos los caracteres especiales.
            output = output.replace(original.charAt(i), ascii.charAt(i));
        }//for i
        return output;
    }

    public static int isPrinterReady(StarPrinterStatus starPrinterStatus) {

        if (starPrinterStatus.receiptPaperEmpty)
            return R.string.warning_sin_papel;
        else if (starPrinterStatus.coverOpen)
            return R.string.warning_impresora_tapa_abierta;
        else if (starPrinterStatus.presenterPaperJamError)
            return R.string.warning_impresora_papel_dañado;
        else
            return -1;
    }
}
