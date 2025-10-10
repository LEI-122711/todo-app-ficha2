package com.example.newFeatures;

import com.example.base.ui.MainLayout;
import com.example.examplefeature.TaskService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "share-view", layout = MainLayout.class)
public class ShareView extends VerticalLayout {

    private final TaskService taskService;

    public ShareView(TaskService taskService) {
        this.taskService = taskService;

        // Share by generating PDF
        Button generatePDF = new Button("Gerar PDF", VaadinIcon.FILE.create());
        add(generatePDF);
        generatePDF.addClickListener(event -> {
            getUI().ifPresent(ui ->
                    ui.getPage().executeJs("window.open($0, '_blank')", "/pdf-download")
            );
            Notification.show("PDF gerado com sucesso!");
        });

        // Share by sending Email
        Button sendEmail = new Button("Enviar Email", VaadinIcon.ENVELOPE.create());
        add(sendEmail);
        sendEmail.addClickListener(event ->
                getUI().ifPresent(ui -> ui.navigate(EmailSendView.class))
        );

        // Connect to Google Calendar
        Button connectButton = new Button("Conectar ao Google Calendar", VaadinIcon.CALENDAR.create(), e -> {
            UI.getCurrent().getPage().setLocation(buildAuthUrl());
        });
        add(connectButton);

        // Share by sending SMS/WhatsApp
        Button sendMessageButton = new Button("Enviar SMS/WhatsApp", VaadinIcon.PAPERPLANE.create());
        add(sendMessageButton);
        sendMessageButton.addClickListener(event ->
                getUI().ifPresent(ui -> ui.navigate(MessageView.class))
        );

    }

    private static final String CLIENT_ID = "1041964997115-vv0vgdr91kc7hgvk2auvj3glnvtig1sf.apps.googleusercontent.com";
    private static final String REDIRECT_URI = "http://localhost:8080/oauth2callback";
    private static final String SCOPE = "https://www.googleapis.com/auth/calendar.events";

    private String buildAuthUrl() {
        return "https://accounts.google.com/o/oauth2/v2/auth"
                + "?client_id=" + CLIENT_ID
                + "&redirect_uri=" + REDIRECT_URI
                + "&response_type=code"
                + "&scope=" + SCOPE
                + "&access_type=offline"
                + "&prompt=consent";
    }
}
