package com.example.examplefeature.ui;

import com.example.base.ui.component.ViewToolbar;
import com.example.examplefeature.Task;
import com.example.examplefeature.TaskService;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Optional;

import static com.vaadin.flow.spring.data.VaadinSpringDataHelpers.toSpringPageRequest;

@Route("")
@PageTitle("Task List")
@Menu(order = 0, icon = "vaadin:clipboard-check", title = "Task List")
class TaskListView extends Main {

    private final TaskService taskService;

    final TextField description;
    final DatePicker dueDate;
    final Button createBtn;
    final Grid<Task> taskGrid;

    TaskListView(TaskService taskService) {
        this.taskService = taskService;

        description = new TextField();
        description.setPlaceholder("What do you want to do?");
        description.setAriaLabel("Task description");
        description.setMaxLength(Task.DESCRIPTION_MAX_LENGTH);
        description.setMinWidth("20em");

        dueDate = new DatePicker();
        dueDate.setPlaceholder("Due date");
        dueDate.setAriaLabel("Due date");

        createBtn = new Button("Create", event -> createTask());
        createBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        var dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(getLocale())
                .withZone(ZoneId.systemDefault());
        var dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(getLocale());

        taskGrid = new Grid<>();
        taskGrid.setItems(query -> taskService.list(toSpringPageRequest(query)).stream());
        taskGrid.addColumn(Task::getDescription).setHeader("Description");
        taskGrid.addColumn(task -> Optional.ofNullable(task.getDueDate()).map(dateFormatter::format).orElse("Never"))
                .setHeader("Due Date");
        taskGrid.addColumn(task -> dateTimeFormatter.format(task.getCreationDate())).setHeader("Creation Date");
        taskGrid.setSizeFull();

        setSizeFull();
        addClassNames(LumoUtility.BoxSizing.BORDER, LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN,
                LumoUtility.Padding.MEDIUM, LumoUtility.Gap.SMALL);

        add(new ViewToolbar("Task List", ViewToolbar.group(description, dueDate, createBtn)));
        add(taskGrid);
    }

    private void createTask() {
        taskService.createTask(description.getValue(), dueDate.getValue());
        taskGrid.getDataProvider().refreshAll();
        String token = (String) UI.getCurrent().getSession().getAttribute("GOOGLE_ACCESS_TOKEN");
        if (token != null) {
            addEventToGoogleCalendar(token, description.getValue(), dueDate.getValue());
        }
        description.clear();
        dueDate.clear();
        Notification.show("Task added", 3000, Notification.Position.BOTTOM_END)
                .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

    private void addEventToGoogleCalendar(String accessToken, String taskDescription, LocalDate dueDate) {
        try {
            var transport = new NetHttpTransport();
            var jsonFactory = JacksonFactory.getDefaultInstance();

            var calendar = new com.google.api.services.calendar.Calendar.Builder(
                    transport,
                    jsonFactory,
                    new com.google.api.client.http.HttpRequestInitializer() {
                        public void initialize(com.google.api.client.http.HttpRequest request) {
                            request.getHeaders().setAuthorization("Bearer " + accessToken);
                        }
                    })
                    .setApplicationName("Vaadin ToDo App")
                    .build();

            var event = new com.google.api.services.calendar.model.Event()
                    .setSummary(taskDescription);

            if (dueDate != null) {
                var startDateTime = new com.google.api.client.util.DateTime(
                        java.util.Date.from(dueDate.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant()));
                var endDateTime = new com.google.api.client.util.DateTime(
                        java.util.Date.from(dueDate.plusDays(1).atStartOfDay(java.time.ZoneId.systemDefault()).toInstant()));

                event.setStart(new com.google.api.services.calendar.model.EventDateTime().setDateTime(startDateTime));
                event.setEnd(new com.google.api.services.calendar.model.EventDateTime().setDateTime(endDateTime));
            }

            calendar.events().insert("primary", event).execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
