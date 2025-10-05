package com.example.newFeatures;

import com.example.base.ui.MainLayout;
import com.example.examplefeature.Task;
import com.example.examplefeature.TaskService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.stream.Collectors;

@Route(value = "send-email", layout = MainLayout.class)
public class EmailSendView extends VerticalLayout {

    private final TaskService taskService;
    private final EmailCreator emailCreator;

    @Autowired
    public EmailSendView(TaskService taskService, EmailCreator emailCreator) {
        this.taskService = taskService;
        this.emailCreator = emailCreator;

        EmailField toField = new EmailField("Destinatário");
        toField.setPlaceholder("exemplo@empresa.com");

        TextField subjectField = new TextField("Assunto");
        subjectField.setValue("Lista de Tarefas");

        TextArea messageField = new TextArea("Mensagem");

        List<Task> tasks = taskService.list(PageRequest.of(0, 1000));

        String taskListContent = tasks.stream()
                .map(Task::toString)
                .collect(Collectors.joining("\n"));

        messageField.setValue("Segue a sua lista de tarefas:\n\n" + taskListContent);

        Button sendButton = new Button("Enviar Email", event -> {
            try {
                emailCreator.sendEmail(
                        toField.getValue(),
                        subjectField.getValue(),
                        messageField.getValue()
                );

                Notification.show("Email enviado com sucesso!")
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);

            } catch (MessagingException e) {
                e.printStackTrace();
                Notification.show("Erro ao enviar email: " + e.getMessage())
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            } catch (Exception e) {
                e.printStackTrace();
                Notification.show("Erro inesperado: " + e.getMessage())
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });

        FormLayout form = new FormLayout();
        form.add(toField, subjectField, messageField, sendButton);
        form.setColspan(messageField, 2);
        form.setColspan(sendButton, 2);

        add(form);
    }
}
