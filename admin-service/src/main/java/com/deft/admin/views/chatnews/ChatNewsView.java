package com.deft.admin.views.chatnews;

import com.deft.admin.data.ChatNewsAdminEntity;
import com.deft.admin.services.ChatNewsAdminService;
import com.deft.admin.views.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.Optional;

@PageTitle("Chat News")
@Route(value = "chat-news/:chatNewsId?/:action?(edit)", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class ChatNewsView extends Div implements BeforeEnterObserver {

    private final String CHAT_NEWS_ID = "chatNewsId";
    private final String CHAT_NEWS_EDIT_ROUTE_TEMPLATE = "chat-news/%s/edit";

    private final Grid<ChatNewsAdminEntity> grid = new Grid<>(ChatNewsAdminEntity.class, false);

    private TextField id;
    private TextField chatId;
    private TextField newsText;
    private DateTimePicker createDate;
    private DateTimePicker updateDate;

    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");

    private final BeanValidationBinder<ChatNewsAdminEntity> binder;

    private ChatNewsAdminEntity chatNews;

    private final ChatNewsAdminService chatNewsAdminService;

    public ChatNewsView(ChatNewsAdminService chatNewsAdminService) {
        this.chatNewsAdminService = chatNewsAdminService;
        addClassNames("chat-news-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("chatId").setAutoWidth(true);
        grid.addColumn("newsText").setAutoWidth(true);
        grid.addColumn("createDate").setAutoWidth(true);
        grid.addColumn("updateDate").setAutoWidth(true);
        grid.setItems(query -> chatNewsAdminService.list(
                        PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(CHAT_NEWS_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(ChatNewsView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(ChatNewsAdminEntity.class);

        // Bind fields. This is where you'd define e.g. validation rules
//        binder.forField(pages).withConverter(new StringToIntegerConverter("Only numbers are allowed")).bind("pages");


//        binder.forField(createDate).withConverter(new InstantToDateConverter()).bind("createDate");
//        binder.forField(updateDate).withConverter(new InstantToDateConverter()).bind("updateDate");
        binder.bindInstanceFields(this);
//        attachImageUpload(image, imagePreview);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.chatNews == null) {
                    this.chatNews = new ChatNewsAdminEntity();
                }
                binder.writeBean(this.chatNews);
                chatNewsAdminService.update(this.chatNews);
                clearForm();
                refreshGrid();
                Notification.show("Data updated");
                UI.getCurrent().navigate(ChatNewsView.class);
            } catch (ObjectOptimisticLockingFailureException exception) {
                Notification n = Notification.show(
                        "Error updating the data. Somebody else has updated the record while you were making changes.");
                n.setPosition(Position.MIDDLE);
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
            } catch (Exception ex) {
                Notification.show("Failed to update the data. Check again that all values are valid");
            }
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
//        Optional<UUID> chatNewsId = event.getRouteParameters().get(CHAT_NEWS_ID).map(UUID::fromString);
        Optional<String> chatNewsId = event.getRouteParameters().get(CHAT_NEWS_ID);
        if (chatNewsId.isPresent()) {
            Optional<ChatNewsAdminEntity> sampleBookFromBackend = chatNewsAdminService.get(chatNewsId.get());
            if (sampleBookFromBackend.isPresent()) {
                populateForm(sampleBookFromBackend.get());
            } else {
                Notification.show(String.format("The requested sampleBook was not found, ID = %s", chatNewsId.get()),
                        3000, Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(ChatNewsView.class);
            }
        }
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        chatId = new TextField("Chat Id");
        newsText = new TextField("News Text");
        createDate = new DateTimePicker("Create Date");
        updateDate = new DateTimePicker("Update Date");
        formLayout.add(chatId, newsText, createDate, updateDate);

        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("button-layout");
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save, cancel);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setClassName("grid-wrapper");
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getDataProvider().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(ChatNewsAdminEntity value) {
        this.chatNews = value;
        binder.readBean(this.chatNews);
    }
}
