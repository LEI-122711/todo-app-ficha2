package com.example.newFeatures;
import com.example.examplefeature.Task;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PDFCreator {

    public byte[] generate(List<Task> tasks) throws IOException {
        PDDocument document = new PDDocument();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        final int maxLinesPerPage = 45; // máximo de linhas por página
        final float startX = 50;
        final float startY = 750;
        final float lineHeight = 15;
        final int maxCharsPerLine = 80; // máximo de caracteres por linha

        int lineCounter = 0;
        PDPage page = new PDPage();
        document.addPage(page);
        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        // Cabeçalho da primeira página
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
        contentStream.beginText();
        contentStream.newLineAtOffset(startX, startY);
        contentStream.showText("Lista de Tarefas:");
        contentStream.newLineAtOffset(0, -25);

        contentStream.setFont(PDType1Font.HELVETICA, 12);

        for (Task task : tasks) {
            String line = "- " + task.getDescription()
                    + " | Criado: " + DateTimeFormatter.ofPattern("dd/MM/yy")
                    .format(task.getCreationDate().atZone(ZoneId.systemDefault()))
                    + (task.getDueDate() != null ? " | Prazo: " + task.getDueDate().format(DateTimeFormatter.ofPattern("dd/MM/yy")) : "");

            // Quebra o texto em múltiplas linhas
            List<String> wrappedLines = wrapText(line, maxCharsPerLine);
            for (String wrappedLine : wrappedLines) {
                if (lineCounter == maxLinesPerPage) {
                    // Fecha o contentStream da página atual
                    contentStream.endText();
                    contentStream.close();

                    // Cria nova página
                    page = new PDPage();
                    document.addPage(page);
                    contentStream = new PDPageContentStream(document, page);

                    // Cabeçalho da nova página
                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
                    contentStream.beginText();
                    contentStream.newLineAtOffset(startX, startY);
                    contentStream.showText("Lista de Tarefas (Continuação):");
                    contentStream.newLineAtOffset(0, -25);

                    contentStream.setFont(PDType1Font.HELVETICA, 12);
                    lineCounter = 0;
                }

                contentStream.showText(wrappedLine);
                contentStream.newLineAtOffset(0, -lineHeight);
                lineCounter++;
            }
        }

        contentStream.endText();
        contentStream.close();

        document.save(out);
        document.close();
        return out.toByteArray();
    }

    // Quebra texto em várias linhas de até maxCharsPerLine
    private List<String> wrapText(String text, int maxCharsPerLine) {
        List<String> lines = new java.util.ArrayList<>();
        int start = 0;
        while (start < text.length()) {
            int end = Math.min(start + maxCharsPerLine, text.length());
            lines.add(text.substring(start, end));
            start += maxCharsPerLine;
        }
        return lines;
    }
}
