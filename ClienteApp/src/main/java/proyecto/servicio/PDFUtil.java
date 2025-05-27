package proyecto.servicio;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
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
}
