//package com.deft.admin.views.user;
//
//import com.deft.admin.views.MainLayout;
//import com.vaadin.flow.component.combobox.ComboBox;
//import com.vaadin.flow.component.datepicker.DatePicker;
//import com.vaadin.flow.component.grid.Grid;
//import com.vaadin.flow.component.grid.Grid.SelectionMode;
//import com.vaadin.flow.component.grid.GridVariant;
//import com.vaadin.flow.component.grid.HeaderRow;
//import com.vaadin.flow.component.grid.dataview.GridListDataView;
//import com.vaadin.flow.component.gridpro.GridPro;
//import com.vaadin.flow.component.html.Div;
//import com.vaadin.flow.component.html.Image;
//import com.vaadin.flow.component.html.Span;
//import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
//import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
//import com.vaadin.flow.component.textfield.TextField;
//import com.vaadin.flow.data.renderer.ComponentRenderer;
//import com.vaadin.flow.data.renderer.LocalDateRenderer;
//import com.vaadin.flow.data.renderer.NumberRenderer;
//import com.vaadin.flow.data.value.ValueChangeMode;
//import com.vaadin.flow.router.PageTitle;
//import com.vaadin.flow.router.Route;
//import java.text.NumberFormat;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Locale;
//import org.apache.commons.lang3.StringUtils;
//
//@PageTitle("User")
//@Route(value = "user", layout = MainLayout.class)
//public class UserView extends Div {
//
//    private GridPro<Client> grid;
//    private GridListDataView<Client> gridListDataView;
//
//    private Grid.Column<Client> clientColumn;
//    private Grid.Column<Client> amountColumn;
//    private Grid.Column<Client> statusColumn;
//    private Grid.Column<Client> dateColumn;
//
//    public UserView() {
//        addClassName("user-view");
//        setSizeFull();
//        createGrid();
//        add(grid);
//    }
//
//    private void createGrid() {
//        createGridComponent();
//        addColumnsToGrid();
//        addFiltersToGrid();
//    }
//
//    private void createGridComponent() {
//        grid = new GridPro<>();
//        grid.setSelectionMode(SelectionMode.MULTI);
//        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_COLUMN_BORDERS);
//        grid.setHeight("100%");
//
//        List<Client> clients = getClients();
//        gridListDataView = grid.setItems(clients);
//    }
//
//    private void addColumnsToGrid() {
//        createClientColumn();
//        createAmountColumn();
//        createStatusColumn();
//        createDateColumn();
//    }
//
//    private void createClientColumn() {
//        clientColumn = grid.addColumn(new ComponentRenderer<>(client -> {
//            HorizontalLayout hl = new HorizontalLayout();
//            hl.setAlignItems(Alignment.CENTER);
//            Image img = new Image(client.getImg(), "");
//            Span span = new Span();
//            span.setClassName("name");
//            span.setText(client.getClient());
//            hl.add(img, span);
//            return hl;
//        })).setComparator(Client::getClient).setHeader("Client");
//    }
//
//    private void createAmountColumn() {
//        amountColumn = grid
//                .addEditColumn(Client::getAmount,
//                        new NumberRenderer<>(Client::getAmount, NumberFormat.getCurrencyInstance(Locale.US)))
//                .text((item, newValue) -> item.setAmount(Double.parseDouble(newValue)))
//                .setComparator(Client::getAmount).setHeader("Amount");
//    }
//
//    private void createStatusColumn() {
//        statusColumn = grid.addEditColumn(Client::getClient, new ComponentRenderer<>(client -> {
//            Span span = new Span();
//            span.setText(client.getStatus());
//            span.getElement().setAttribute("theme", "badge " + client.getStatus().toLowerCase());
//            return span;
//        })).select(Client::setStatus, Arrays.asList("Pending", "Success", "Error"))
//                .setComparator(Client::getStatus).setHeader("Status");
//    }
//
//    private void createDateColumn() {
//        dateColumn = grid
//                .addColumn(new LocalDateRenderer<>(client -> LocalDate.parse(client.getDate()),
//                        () -> DateTimeFormatter.ofPattern("M/d/yyyy")))
//                .setComparator(Client::getDate).setHeader("Date").setWidth("180px").setFlexGrow(0);
//    }
//
//    private void addFiltersToGrid() {
//        HeaderRow filterRow = grid.appendHeaderRow();
//
//        TextField clientFilter = new TextField();
//        clientFilter.setPlaceholder("Filter");
//        clientFilter.setClearButtonVisible(true);
//        clientFilter.setWidth("100%");
//        clientFilter.setValueChangeMode(ValueChangeMode.EAGER);
//        clientFilter.addValueChangeListener(event -> gridListDataView
//                .addFilter(client -> StringUtils.containsIgnoreCase(client.getClient(), clientFilter.getValue())));
//        filterRow.getCell(clientColumn).setComponent(clientFilter);
//
//        TextField amountFilter = new TextField();
//        amountFilter.setPlaceholder("Filter");
//        amountFilter.setClearButtonVisible(true);
//        amountFilter.setWidth("100%");
//        amountFilter.setValueChangeMode(ValueChangeMode.EAGER);
//        amountFilter.addValueChangeListener(event -> gridListDataView.addFilter(client -> StringUtils
//                .containsIgnoreCase(Double.toString(client.getAmount()), amountFilter.getValue())));
//        filterRow.getCell(amountColumn).setComponent(amountFilter);
//
//        ComboBox<String> statusFilter = new ComboBox<>();
//        statusFilter.setItems(Arrays.asList("Pending", "Success", "Error"));
//        statusFilter.setPlaceholder("Filter");
//        statusFilter.setClearButtonVisible(true);
//        statusFilter.setWidth("100%");
//        statusFilter.addValueChangeListener(
//                event -> gridListDataView.addFilter(client -> areStatusesEqual(client, statusFilter)));
//        filterRow.getCell(statusColumn).setComponent(statusFilter);
//
//        DatePicker dateFilter = new DatePicker();
//        dateFilter.setPlaceholder("Filter");
//        dateFilter.setClearButtonVisible(true);
//        dateFilter.setWidth("100%");
//        dateFilter.addValueChangeListener(
//                event -> gridListDataView.addFilter(client -> areDatesEqual(client, dateFilter)));
//        filterRow.getCell(dateColumn).setComponent(dateFilter);
//    }
//
//    private boolean areStatusesEqual(Client client, ComboBox<String> statusFilter) {
//        String statusFilterValue = statusFilter.getValue();
//        if (statusFilterValue != null) {
//            return StringUtils.equals(client.getStatus(), statusFilterValue);
//        }
//        return true;
//    }
//
//    private boolean areDatesEqual(Client client, DatePicker dateFilter) {
//        LocalDate dateFilterValue = dateFilter.getValue();
//        if (dateFilterValue != null) {
//            LocalDate clientDate = LocalDate.parse(client.getDate());
//            return dateFilterValue.equals(clientDate);
//        }
//        return true;
//    }
//
//    private List<Client> getClients() {
//        return Arrays.asList(
//                createClient(4957, "https://randomuser.me/api/portraits/women/42.jpg", "Amarachi Nkechi", 47427.0,
//                        "Success", "2019-05-09"),
//                createClient(675, "https://randomuser.me/api/portraits/women/24.jpg", "Bonelwa Ngqawana", 70503.0,
//                        "Success", "2019-05-09"),
//                createClient(6816, "https://randomuser.me/api/portraits/men/42.jpg", "Debashis Bhuiyan", 58931.0,
//                        "Success", "2019-05-07"),
//                createClient(5144, "https://randomuser.me/api/portraits/women/76.jpg", "Jacqueline Asong", 25053.0,
//                        "Pending", "2019-04-25"),
//                createClient(9800, "https://randomuser.me/api/portraits/men/24.jpg", "Kobus van de Vegte", 7319.0,
//                        "Pending", "2019-04-22"),
//                createClient(3599, "https://randomuser.me/api/portraits/women/94.jpg", "Mattie Blooman", 18441.0,
//                        "Error", "2019-04-17"),
//                createClient(3989, "https://randomuser.me/api/portraits/men/76.jpg", "Oea Romana", 33376.0, "Pending",
//                        "2019-04-17"),
//                createClient(1077, "https://randomuser.me/api/portraits/men/94.jpg", "Stephanus Huggins", 75774.0,
//                        "Success", "2019-02-26"),
//                createClient(8942, "https://randomuser.me/api/portraits/men/16.jpg", "Torsten Paulsson", 82531.0,
//                        "Pending", "2019-02-21"));
//    }
//
//    private Client createClient(int id, String img, String client, double amount, String status, String date) {
//        Client c = new Client();
//        c.setId(id);
//        c.setImg(img);
//        c.setClient(client);
//        c.setAmount(amount);
//        c.setStatus(status);
//        c.setDate(date);
//
//        return c;
//    }
//}
