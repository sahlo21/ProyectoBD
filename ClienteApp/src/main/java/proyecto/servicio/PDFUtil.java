package proyecto.servicio;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

public class PDFUtil {

    /**
     * Genera un PDF con tabla: Cédula | Nombre | Cargo | Precio Evento
     * @param datos       Lista de mapas con keys “cedulaTrabajador”, “nombreTrabajador”, “nombreCargo”, “precioPorEvento”
     * @param rutaArchivo Ruta donde se guardará el PDF
     */
    public static void generarReporteTrabajadoresPDF(
            List<Map<String,Object>> datos,
            String rutaArchivo) throws Exception {

        Document doc = new Document(PageSize.A4.rotate(), 36,36,54,36);
        PdfWriter.getInstance(doc, new FileOutputStream(rutaArchivo));
        doc.open();

        // Título
        Font fTitle = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
        Paragraph title = new Paragraph("Reporte: Trabajadores y Precio por Evento", fTitle);
        title.setAlignment(Element.ALIGN_CENTER);
        doc.add(title);
        doc.add(Chunk.NEWLINE);

        // Creamos una tabla de 4 columnas
        PdfPTable table = new PdfPTable(new float[]{2,4,3,2});
        table.setWidthPercentage(100);

        // Encabezados
        Font fHead = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        for (String h : new String[]{"Cédula","Nombre","Cargo","Precio Evento"}) {
            PdfPCell c = new PdfPCell(new Phrase(h, fHead));
            c.setBackgroundColor(BaseColor.LIGHT_GRAY);
            c.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c);
        }

        // Filas
        Font fBody = new Font(Font.FontFamily.HELVETICA, 11);
        for (Map<String,Object> fila : datos) {
            // Cédula (int)
            Object ced = fila.get("cedulaTrabajador");
            table.addCell(new PdfPCell(new Phrase(
                    ced != null ? ced.toString() : "", fBody)));

            // Nombre
            Object nom = fila.get("nombreTrabajador");
            table.addCell(new PdfPCell(new Phrase(
                    nom != null ? nom.toString() : "", fBody)));

            // Cargo
            Object cargo = fila.get("nombreCargo");
            table.addCell(new PdfPCell(new Phrase(
                    cargo != null ? cargo.toString() : "", fBody)));

            // Precio Evento (double)
            Object precio = fila.get("precioPorEvento");
            String textoPrecio = "";
            if (precio instanceof Number) {
                textoPrecio = String.format("%.2f", ((Number)precio).doubleValue());
            }
            table.addCell(new PdfPCell(new Phrase(textoPrecio, fBody)));
        }

        doc.add(table);
        doc.close();
    }
    public static void generarReporteEventosCostosPDF(
            List<Map<String,Object>> datos,
            String rutaArchivo) throws Exception {

        Document doc = new Document(PageSize.A4.rotate(), 36,36,54,36);
        PdfWriter.getInstance(doc, new FileOutputStream(rutaArchivo));
        doc.open();

        // Título
        Font fTitle = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
        Paragraph title = new Paragraph("Reporte: Ingresos generados por eventos (suma total de eventos realizados)", fTitle);
        title.setAlignment(Element.ALIGN_CENTER);
        doc.add(title);
        doc.add(Chunk.NEWLINE);

        PdfPTable table = new PdfPTable(new float[]{3,2,2,2,2,2});
        table.setWidthPercentage(100);

        // Encabezados
        Font fHead = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        for (String h : new String[]{"Evento","Fecha","Precio Entrada","#Trabajadores","Costo Trabajadores","Total Costo"}) {
            PdfPCell c = new PdfPCell(new Phrase(h, fHead));
            c.setBackgroundColor(BaseColor.LIGHT_GRAY);
            c.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c);
        }

        // Datos
        Font fBody = new Font(Font.FontFamily.HELVETICA, 11);
        for (Map<String,Object> fila : datos) {
            table.addCell(new PdfPCell(new Phrase(fila.get("nombreEvento").toString(), fBody)));
            table.addCell(new PdfPCell(new Phrase(fila.get("fecha").toString(), fBody)));
            table.addCell(new PdfPCell(new Phrase(String.format("%.2f", fila.get("precioEntrada")), fBody)));
            table.addCell(new PdfPCell(new Phrase(fila.get("cantidadTrabajadores").toString(), fBody)));
            table.addCell(new PdfPCell(new Phrase(String.format("%.2f", fila.get("costoTotalTrabajadores")), fBody)));
            table.addCell(new PdfPCell(new Phrase(String.format("%.2f", fila.get("totalCostoEvento")), fBody)));
        }

        doc.add(table);
        doc.close();
    }
    public static void generarReporteInventarioPDF(
            List<Map<String,Object>> datos,
            String rutaArchivo) throws Exception {

        Document doc = new Document(PageSize.A4.rotate(), 36,36,54,36);
        PdfWriter.getInstance(doc, new FileOutputStream(rutaArchivo));
        doc.open();

        Font fTitle = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
        Paragraph title = new Paragraph("Reporte: Inventario disponible y valor total", fTitle);
        title.setAlignment(Element.ALIGN_CENTER);
        doc.add(title);
        doc.add(Chunk.NEWLINE);

        PdfPTable table = new PdfPTable(new float[]{4,2,2,3});
        table.setWidthPercentage(100);

        Font fHead = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        String[] headers = {
                "Producto", "Precio Unitario", "Cantidad", "Valor Total en Stock"
        };
        for (String h : headers) {
            PdfPCell c = new PdfPCell(new Phrase(h, fHead));
            c.setBackgroundColor(BaseColor.LIGHT_GRAY);
            c.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c);
        }

        Font fBody = new Font(Font.FontFamily.HELVETICA, 11);
        for (Map<String,Object> fila : datos) {
            table.addCell(new Phrase(fila.get("nombreProducto").toString(), fBody));
            table.addCell(new Phrase(String.format("%.2f", fila.get("precio")), fBody));
            table.addCell(new Phrase(fila.get("cantidad").toString(), fBody));
            table.addCell(new Phrase(String.format("%.2f", fila.get("valorTotalStock")), fBody));
        }

        doc.add(table);
        doc.close();
    }
    public static void generarReporteAuditoriaPDF(
            List<Map<String,Object>> datos,
            String rutaArchivo) throws Exception {

        Document doc = new Document(PageSize.A4.rotate(), 36, 36, 54, 36);
        PdfWriter.getInstance(doc, new FileOutputStream(rutaArchivo));
        doc.open();

        Font fTitle = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
        Paragraph title = new Paragraph("Reporte de Auditoría: Acciones de Usuarios", fTitle);
        title.setAlignment(Element.ALIGN_CENTER);
        doc.add(title);
        doc.add(Chunk.NEWLINE);

        PdfPTable table = new PdfPTable(new float[]{3, 2, 2, 3, 3});
        table.setWidthPercentage(100);

        Font fHead = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        String[] headers = {
                "Usuario", "Acción", "Total", "Primera Acción", "Última Acción"
        };
        for (String h : headers) {
            PdfPCell c = new PdfPCell(new Phrase(h, fHead));
            c.setBackgroundColor(BaseColor.LIGHT_GRAY);
            c.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c);
        }

        Font fBody = new Font(Font.FontFamily.HELVETICA, 11);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Map<String,Object> fila : datos) {
            table.addCell(new Phrase(fila.get("nombreUsuario").toString(), fBody));
            table.addCell(new Phrase(fila.get("accion").toString(), fBody));
            table.addCell(new Phrase(fila.get("totalAcciones").toString(), fBody));
            table.addCell(new Phrase(sdf.format(fila.get("primeraAccion")), fBody));
            table.addCell(new Phrase(sdf.format(fila.get("ultimaAccion")), fBody));
        }

        doc.add(table);
        doc.close();
    }
    public static void generarFacturaEventoPDF(
            Map<String, Object> datos,
            String rutaArchivo) throws Exception {

        Document doc = new Document(PageSize.A4, 36, 36, 54, 36);
        PdfWriter.getInstance(doc, new FileOutputStream(rutaArchivo));
        doc.open();

        Font fTitle = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
        Paragraph title = new Paragraph("Factura de Evento", fTitle);
        title.setAlignment(Element.ALIGN_CENTER);
        doc.add(title);
        doc.add(Chunk.NEWLINE);

        Font fBody = new Font(Font.FontFamily.HELVETICA, 12);

        // Datos básicos del evento y cliente
        doc.add(new Paragraph("Cliente: " + safeToString(datos.get("cliente")), fBody));
        doc.add(new Paragraph("Cédula: " + safeToString(datos.get("cedulaCliente")), fBody));
        doc.add(new Paragraph("Evento: " + safeToString(datos.get("evento")), fBody));
        doc.add(new Paragraph("Lugar: " + safeToString(datos.get("lugar")), fBody));
        doc.add(new Paragraph("Fecha: " + safeToString(datos.get("fecha")), fBody));
        doc.add(new Paragraph("ID Evento: " + safeToString(datos.get("idEvento")), fBody));
        doc.add(Chunk.NEWLINE);

        // Tabla de trabajadores
        doc.add(new Paragraph("Trabajadores", fBody));
        PdfPTable tTrab = new PdfPTable(new float[]{3, 3, 2});
        tTrab.setWidthPercentage(100);
        String[] cab1 = {"Nombre", "Cargo", "Precio"};
        for (String c : cab1) {
            PdfPCell cell = new PdfPCell(new Phrase(c, fBody));
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            tTrab.addCell(cell);
        }

        double totalTrab = 0;
        List<Map<String, Object>> trabajadores = (List<Map<String, Object>>) datos.get("trabajadores");
        for (Map<String, Object> fila : trabajadores) {
            tTrab.addCell(safeToString(fila.get("nombre")));
            tTrab.addCell(safeToString(fila.get("cargo")));
            double precio = toDouble(fila.get("precio"));
            tTrab.addCell(String.format("$%.2f", precio));
            totalTrab += precio;
        }
        doc.add(tTrab);
        doc.add(new Paragraph("Subtotal Trabajadores: " + String.format("$%.2f", totalTrab), fBody));
        doc.add(Chunk.NEWLINE);

        // Tabla de productos
        doc.add(new Paragraph("Productos", fBody));
        PdfPTable tProd = new PdfPTable(new float[]{3, 2, 2, 2});
        tProd.setWidthPercentage(100);
        String[] cab2 = {"Producto", "Cantidad", "Precio Unitario", "Subtotal"};
        for (String c : cab2) {
            PdfPCell cell = new PdfPCell(new Phrase(c, fBody));
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            tProd.addCell(cell);
        }

        double totalProd = 0;
        List<Map<String, Object>> productos = (List<Map<String, Object>>) datos.get("productos");
        for (Map<String, Object> fila : productos) {
            tProd.addCell(safeToString(fila.get("nombre")));
            tProd.addCell(safeToString(fila.get("cantidad")));
            double precioUnitario = toDouble(fila.get("precioUnitario"));
            double subtotal = toDouble(fila.get("subtotal"));
            tProd.addCell(String.format("$%.2f", precioUnitario));
            tProd.addCell(String.format("$%.2f", subtotal));
            totalProd += subtotal;
        }
        doc.add(tProd);
        doc.add(new Paragraph("Subtotal Productos: " + String.format("$%.2f", totalProd), fBody));
        doc.add(Chunk.NEWLINE);

        // Total final
        double precioBase = toDouble(datos.get("precioBase"));
        double totalFinal = precioBase + totalTrab + totalProd;
        doc.add(new Paragraph("Precio Base del Evento: " + String.format("$%.2f", precioBase), fBody));
        doc.add(new Paragraph("Total a Pagar: " + String.format("$%.2f", totalFinal), fBody));

        doc.close();
    }

    private static String safeToString(Object o) {
        return o == null ? "" : o.toString();
    }

    private static double toDouble(Object o) {
        if (o instanceof Number) return ((Number) o).doubleValue();
        try {
            return Double.parseDouble(o.toString());
        } catch (Exception e) {
            return 0;
        }
    }


    public static void generarReporteAlquileresMensualesPDF(
            List<Map<String,Object>> datos,
            String rutaArchivo) throws Exception {

        Document doc = new Document(PageSize.A4.rotate(), 36, 36, 54, 36);
        PdfWriter.getInstance(doc, new FileOutputStream(rutaArchivo));
        doc.open();

        Font fTitle = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
        Paragraph title = new Paragraph("Reporte de Alquileres Mensuales", fTitle);
        title.setAlignment(Element.ALIGN_CENTER);
        doc.add(title);
        doc.add(Chunk.NEWLINE);

        PdfPTable table = new PdfPTable(new float[]{2, 4, 2, 3});
        table.setWidthPercentage(100);

        Font fHead = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        String[] headers = {
                "Mes", "Producto", "Veces Alquilado", "Ingreso Total"
        };
        for (String h : headers) {
            PdfPCell c = new PdfPCell(new Phrase(h, fHead));
            c.setBackgroundColor(BaseColor.LIGHT_GRAY);
            c.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c);
        }

        Font fBody = new Font(Font.FontFamily.HELVETICA, 11);
        for (Map<String,Object> fila : datos) {
            table.addCell(new Phrase(fila.get("mes").toString(), fBody));
            table.addCell(new Phrase(fila.get("producto").toString(), fBody));
            table.addCell(new Phrase(fila.get("vecesAlquilado").toString(), fBody));
            table.addCell(new Phrase("$" + String.format("%.2f", fila.get("ingresoTotal")), fBody));
        }

        doc.add(table);
        doc.close();
    }

}
