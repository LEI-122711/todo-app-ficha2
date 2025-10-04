package com.example.newFeatures;

import com.example.base.ui.MainLayout;
import com.example.examplefeature.Task;
import com.example.examplefeature.TaskService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route(value = "share-view", layout = MainLayout.class)
public class ShareView extends VerticalLayout {

    private final TaskService taskService;

    public ShareView(TaskService taskService) {
        this.taskService = taskService;

        //Share by generating PDF
        Button generatePDF = new Button("Gerar PDF", VaadinIcon.FILE.create());
        add(generatePDF);
        generatePDF.addClickListener(event -> {
            getUI().ifPresent(ui ->
                    ui.getPage().executeJs("window.open($0, '_blank')", "/pdf-download")
            );
            Notification.show("PDF gerado com sucesso!");
        });

        //Share by sending Email
        Button sendEmail = new Button("Enviar Email", VaadinIcon.ENVELOPE.create());
        add(sendEmail);
        sendEmail.addClickListener(event ->
            getUI().ifPresent(ui -> ui.navigate(EmailSendView.class))
        );
    }
}
