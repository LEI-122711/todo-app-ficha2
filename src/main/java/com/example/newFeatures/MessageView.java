package com.example.newFeatures;

import com.example.base.ui.MainLayout;
import com.example.examplefeature.Task;
import com.example.examplefeature.TaskService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.stream.Collectors;

@Route(value = "message-options", layout = MainLayout.class)
public class MessageView extends VerticalLayout {

    private final TaskService taskService;
    private final MessageService mensagemService;

    public MessageView(TaskService taskService, MessageService mensagemService) {
        this.taskService = taskService;
        this.mensagemService = mensagemService;

        setSpacing(true);
        setPadding(true);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        // Campo para inserir o número
        TextField numeroField = new TextField("Número de telefone");
        numeroField.setPlaceholder("+351XXXXXXXXX");
        numeroField.setPrefixComponent(VaadinIcon.PHONE.create());
        numeroField.setClearButtonVisible(true);
        numeroField.setWidth("300px");

        // Botões
        Button smsButton = new Button("Enviar Lista por SMS", VaadinIcon.COMMENT.create());
        Button whatsappButton = new Button("Enviar Lista por WhatsApp", VaadinIcon.MOBILE.create());
        Button voltar = new Button("Voltar", VaadinIcon.ARROW_LEFT.create(), e -> getUI().ifPresent(ui -> ui.navigate(ShareView.class)));

        add(numeroField, smsButton, whatsappButton, voltar);

        smsButton.addClickListener(e -> {
            String numero = numeroField.getValue().trim();
            if (numero.isEmpty()) {
                Notification.show("Por favor, introduz um número de telefone.", 3000, Notification.Position.MIDDLE)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }
            enviarPorSMS(numero);
        });

        whatsappButton.addClickListener(e -> {
            String numero = numeroField.getValue().trim();
            if (numero.isEmpty()) {
                Notification.show("Por favor, introduz um número de telefone.", 3000, Notification.Position.MIDDLE)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }
            enviarPorWhatsApp(numero);
        });
    }

    private void enviarPorSMS(String numero) {
        try {
            String texto = gerarTextoTarefas();
            mensagemService.enviarSMS(numero, texto);
            Notification.show("Lista enviada por SMS!", 3000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        } catch (Exception ex) {
            Notification.show("Erro ao enviar SMS: " + ex.getMessage(), 5000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    private void enviarPorWhatsApp(String numero) {
        try {
            String texto = gerarTextoTarefas();
            mensagemService.enviarWhatsApp(numero, texto);
            Notification.show("Lista enviada por WhatsApp!", 3000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        } catch (Exception ex) {
            Notification.show("Erro ao enviar WhatsApp: " + ex.getMessage(), 5000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    private String gerarTextoTarefas() {
        List<Task> tarefas = taskService.list(PageRequest.of(0, 50));
        if (tarefas.isEmpty()) {
            return "Sem tarefas na lista!";
        }
        return tarefas.stream()
                .map(Task::toString)
                .collect(Collectors.joining("\n"));
    }
}
