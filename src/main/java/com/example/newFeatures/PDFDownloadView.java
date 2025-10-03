package com.example.newFeatures;

import com.example.examplefeature.TaskService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class PDFDownloadView{

    private final TaskService taskService;

    public PDFDownloadView(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/pdf-download")
    public void downloadPdf(HttpServletResponse response) throws IOException {
        PDFCreator generator = new PDFCreator();
        var tasks = taskService.list(PageRequest.of(0, 1000));
        byte[] pdfBytes = generator.generate(tasks);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=tasks.pdf");
        response.getOutputStream().write(pdfBytes);
        response.getOutputStream().flush();
    }
}
