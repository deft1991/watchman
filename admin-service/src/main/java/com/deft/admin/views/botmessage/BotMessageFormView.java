package com.deft.admin.views.botmessage;

import com.deft.admin.data.BotMessageEntity;
import com.deft.admin.data.ChatSettingsEntity;
import com.deft.admin.services.BotMessageAdminService;
import com.deft.admin.services.ChatSettingsAdminService;
import com.deft.admin.views.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@PageTitle("Bot Message")
@Route(value = "bot-message", layout = MainLayout.class)
@Uses(Icon.class)
public class BotMessageFormView extends Composite<VerticalLayout> {

    public static final int charLimit = 1000;

    private final Select<SampleItem> chatSelect;
    private final TextArea chatMessageTextArea;
    private final DateTimePicker scheduledSendDateTimePicker;

    private final ChatSettingsAdminService chatSettingsAdminService;
    private final BotMessageAdminService botMessageAdminService;
    private final List<HasValue> formFields;

    private BotMessageEntity botMessageEntity;

    public BotMessageFormView(
            ChatSettingsAdminService chatSettingsAdminService,
            BotMessageAdminService botMessageAdminService

    ) {
        this.chatSettingsAdminService = chatSettingsAdminService;
        this.botMessageAdminService = botMessageAdminService;
        formFields = new ArrayList<>();

        VerticalLayout verticalLayoutColumn2 = new VerticalLayout();
        H3 h3 = new H3();
        FormLayout formLayout2Col = new FormLayout();
        chatSelect = new Select<>();
        chatSelect.setLabel("Chat");
        chatSelect.setWidth("min-content");
        setSelectSampleData(chatSelect);
        formLayout2Col.add(chatSelect);

        chatMessageTextArea = new TextArea();
        chatMessageTextArea.setLabel("Message");
        chatMessageTextArea.setMaxLength(charLimit);
        chatMessageTextArea.setValueChangeMode(ValueChangeMode.EAGER);
        chatMessageTextArea.addValueChangeListener(e -> {
            e.getSource()
                    .setHelperText(e.getValue().length() + "/" + charLimit);
        });

        formLayout2Col.add(chatMessageTextArea);

        scheduledSendDateTimePicker = new DateTimePicker();
        scheduledSendDateTimePicker.setLabel("Scheduled Send Time");
        scheduledSendDateTimePicker.setValue(LocalDateTime.now());
        formLayout2Col.add(scheduledSendDateTimePicker);

        HorizontalLayout layoutRow = new HorizontalLayout();
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.getStyle().set("flex-grow", "1");

        Button buttonSave = new Button();
        buttonSave.setText("Save");
        buttonSave.setWidth("min-content");
        buttonSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button buttonCancel = new Button();
        buttonCancel.setText("Cancel");
        buttonCancel.setWidth("min-content");

        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setJustifyContentMode(JustifyContentMode.START);
        getContent().setAlignItems(Alignment.CENTER);
        verticalLayoutColumn2.setWidth("100%");
        verticalLayoutColumn2.setMaxWidth("800px");
        verticalLayoutColumn2.setHeight("min-content");
        h3.setText("Create Bot Message");
        h3.setWidth("100%");
        formLayout2Col.setWidth("100%");

        getContent().add(verticalLayoutColumn2);
        verticalLayoutColumn2.add(h3);
        verticalLayoutColumn2.add(formLayout2Col);

        verticalLayoutColumn2.add(layoutRow);
        layoutRow.add(buttonSave);
        layoutRow.add(buttonCancel);

        buttonCancel.addClickListener(e -> clearForm());

        buttonSave.addClickListener(e -> {
            try {
                if (this.botMessageEntity == null) {
                    this.botMessageEntity = new BotMessageEntity();
                }
                boolean isSuccess = saveData();
                if (isSuccess) {
                    clearForm();
                    Notification.show("Data saved");
                }
                UI.getCurrent().navigate(BotMessageFormView.class);
            } catch (ObjectOptimisticLockingFailureException exception) {
                Notification n = Notification.show(
                        "Error updating the data. Somebody else has updated the record while you were making changes.");
                n.setPosition(Notification.Position.MIDDLE);
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
            } catch (Exception ex) {
                Notification.show("Failed to update the data. Check again that all values are valid");
            }
        });


        formFields.add(chatSelect);
        formFields.add(chatMessageTextArea);
        formFields.add(scheduledSendDateTimePicker);
    }

    private boolean saveData() {
        Notification notification = null;
        if (chatSelect.getValue() == null) {
            notification = Notification.show(
                    "Error updating the data. Please, choose chat.");

        }
        if (chatMessageTextArea.getValue().isEmpty()) {
            notification = Notification.show(
                    "Error updating the data. Please, enter your message.");
        }
        if (scheduledSendDateTimePicker.getValue() == null) {
            notification = Notification.show(
                    "Error updating the data. Please, choose date and time to send.");
        }
        if (notification != null) {
            notification.setPosition(Notification.Position.MIDDLE);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            return false;
        }

        Instant scheduledTime = scheduledSendDateTimePicker
                .getValue()
                .atZone(ZoneId.of(MainLayout.clientTimeZone))
                .toInstant();
        BotMessageEntity botMessage = BotMessageEntity
                .builder()
                .chatId(chatSelect.getValue().chatId)
                .message(chatMessageTextArea.getValue())
                .scheduledSendTime(scheduledTime)
                .build();
        botMessageAdminService.save(botMessage);
        return true;
    }

    private void clearForm() {
        formFields.forEach(HasValue::clear);
    }

    record SampleItem(long chatId, String name) {
    }

    private void setSelectSampleData(Select<SampleItem> select) {
        List<SampleItem> itemList = chatSettingsAdminService
                .findAll()
                .parallelStream()
                .sorted(Comparator.comparing(ChatSettingsEntity::getChatName))
                .map(el -> new SampleItem(el.getChatId(), el.getChatName()))
                .toList();
        select.setItems(itemList);
        select.setItemLabelGenerator(item -> item.name);
    }

}
