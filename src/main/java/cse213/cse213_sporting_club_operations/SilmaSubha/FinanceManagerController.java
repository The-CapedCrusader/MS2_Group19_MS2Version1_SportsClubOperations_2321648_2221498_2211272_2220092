package cse213.cse213_sporting_club_operations.SilmaSubha;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class FinanceManagerController implements Initializable {

    // Home Tab
    @FXML private TableView<Budget> homeTable;
    @FXML private TableColumn<Budget, String> homeNameCol;
    @FXML private TableColumn<Budget, String> homeMatchCol;
    @FXML private TableColumn<Budget, Double> homeVenueCol;

    // Financial Report Tab
    @FXML private TableView<FinancialReport> reportTable;
    @FXML private TableColumn<FinancialReport, String> reportDateCol;
    @FXML private TableColumn<FinancialReport, Double> revenueCol;
    @FXML private TableColumn<FinancialReport, String> detailsCol;

    // Player Transfer Tab
    @FXML private TextField playerNameField;
    @FXML private ComboBox<String> transferFeeBox;
    @FXML private ComboBox<String> reviewRequestBox;
    @FXML private Button approveTransferBtn;
    @FXML private TableView<PlayerTransfer> transferTable;
    @FXML private TableColumn<PlayerTransfer, String> transferPlayerNameCol;
    @FXML private TableColumn<PlayerTransfer, String> transferFeeCol;
    @FXML private TableColumn<PlayerTransfer, String> reviewStatusCol;

    // Ticket Sales Tab
    @FXML private TableView<TicketSale> ticketSalesTable;
    @FXML private TableColumn<TicketSale, String> matchNameCol;
    @FXML private TableColumn<TicketSale, String> venueCol;
    @FXML private TableColumn<TicketSale, Integer> ticketsSoldCol;
    @FXML private TableColumn<TicketSale, Double> totalRevenueCol;

    // Sponsorship Tab
    @FXML private TextField sponsorNameField;
    @FXML private ComboBox<String> paymentStatusBox;
    @FXML private TextField contractField;
    @FXML private DatePicker sponsorDatePicker;
    @FXML private Button saveSponsorBtn;
    @FXML private TableView<Sponsorship> sponsorTable;
    @FXML private TableColumn<Sponsorship, String> sponsorNameCol;
    @FXML private TableColumn<Sponsorship, String> paymentStatusCol;
    @FXML private TableColumn<Sponsorship, String> contractCol;
    @FXML private TableColumn<Sponsorship, LocalDate> dateCol;

    // Logout
    @FXML private Button logoutButton;

    private ObservableList<Sponsorship> sponsorData = FXCollections.observableArrayList();
    private final String SPONSOR_FILE = "sponsors.bin";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeHomeTab();
        initializeReportTab();
        initializeTransferTab();
        initializeTicketSalesTab();
        initializeSponsorshipTab();
        loadSponsorData();
    }

    private void initializeHomeTab() {
        homeNameCol.setCellValueFactory(new PropertyValueFactory<>("budgetName"));
        homeMatchCol.setCellValueFactory(new PropertyValueFactory<>("matchName"));
        homeVenueCol.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));

        ObservableList<Budget> data = FXCollections.observableArrayList(
                new Budget("League Match",    "National Stadium", 25000.0),
                new Budget("Friendly Match",  "City Arena",       18000.0),
                new Budget("Championship",    "Olympic Stadium",  45000.0)
        );
        homeTable.setItems(data);
    }

    private void initializeReportTab() {
        reportDateCol.setCellValueFactory(new PropertyValueFactory<>("reportDate"));
        revenueCol.setCellValueFactory(new PropertyValueFactory<>("revenue"));
        detailsCol.setCellValueFactory(new PropertyValueFactory<>("details"));

        ObservableList<FinancialReport> data = FXCollections.observableArrayList(
                new FinancialReport("2023-10-15", 125000.0, "Quarterly financial report"),
                new FinancialReport("2023-07-20",  98000.0, "Mid-year financial report"),
                new FinancialReport("2023-04-05",  75000.0, "First quarter report")
        );
        reportTable.setItems(data);
    }

    private void initializeTransferTab() {
        transferPlayerNameCol.setCellValueFactory(new PropertyValueFactory<>("playerName"));
        transferFeeCol.setCellValueFactory(new PropertyValueFactory<>("transferFee"));
        reviewStatusCol.setCellValueFactory(new PropertyValueFactory<>("reviewStatus"));

        transferFeeBox.getItems().addAll("Paid", "Unpaid");
        reviewRequestBox.getItems().addAll("Done", "Pending");

        ObservableList<PlayerTransfer> data = FXCollections.observableArrayList(
                new PlayerTransfer("John Doe",     "Paid",   "Done"),
                new PlayerTransfer("Mike Smith",   "Unpaid", "Pending"),
                new PlayerTransfer("Alex Johnson", "Paid",   "Done")
        );
        transferTable.setItems(data);
    }

    private void initializeTicketSalesTab() {
        matchNameCol.setCellValueFactory(new PropertyValueFactory<>("matchName"));
        venueCol.setCellValueFactory(new PropertyValueFactory<>("venue"));
        ticketsSoldCol.setCellValueFactory(new PropertyValueFactory<>("ticketsSold"));
        totalRevenueCol.setCellValueFactory(new PropertyValueFactory<>("totalRevenue"));

        ObservableList<TicketSale> data = FXCollections.observableArrayList(
                new TicketSale("League Match",   "National Stadium", 1250,  62500.0),
                new TicketSale("Friendly Match", "City Arena",        850,  42500.0),
                new TicketSale("Championship",   "Olympic Stadium",   2100, 105000.0)
        );
        ticketSalesTable.setItems(data);
    }

    private void initializeSponsorshipTab() {
        sponsorNameCol.setCellValueFactory(new PropertyValueFactory<>("sponsorName"));
        paymentStatusCol.setCellValueFactory(new PropertyValueFactory<>("paymentStatus"));
        contractCol.setCellValueFactory(new PropertyValueFactory<>("contractDetails"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        paymentStatusBox.getItems().addAll("Paid", "Pending", "Overdue");
        sponsorDatePicker.setValue(LocalDate.now());
        sponsorTable.setItems(sponsorData);
    }

    @FXML
    private void handleTransferApproval(ActionEvent event) {
        String name = playerNameField.getText().trim();
        String fee  = transferFeeBox.getValue();
        String rev  = reviewRequestBox.getValue();
        if (name.isEmpty() || fee == null || rev == null) {
            showAlert("Error", "Please fill all fields", null);
            return;
        }
        PlayerTransfer pt = new PlayerTransfer(name, fee, rev);
        transferTable.getItems().add(pt);
        playerNameField.clear();
        transferFeeBox.setValue(null);
        reviewRequestBox.setValue(null);
        showAlert("Success", "Transfer approved and added", null);
    }

    @FXML
    private void handleSaveSponsor(ActionEvent event) {
        String name   = sponsorNameField.getText().trim();
        String status = paymentStatusBox.getValue();
        String cont   = contractField.getText().trim();
        LocalDate d   = sponsorDatePicker.getValue();
        if (name.isEmpty() || status == null || cont.isEmpty() || d == null) {
            showAlert("Error", "Please fill all fields", null);
            return;
        }
        Sponsorship sp = new Sponsorship(name, status, cont, d);
        sponsorData.add(sp);
        saveSponsorData();
        sponsorNameField.clear();
        paymentStatusBox.setValue(null);
        contractField.clear();
        sponsorDatePicker.setValue(LocalDate.now());
        showAlert("Success", "Sponsor saved", null);
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            // Close current window
            Stage st = (Stage)logoutButton.getScene().getWindow();
            st.close();

            // Load Login.fxml
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/com/cse213/fproject/Login.fxml"));
            Parent root = loader.load();
            Stage loginStage = new Stage();
            loginStage.setTitle("Login");
            loginStage.setScene(new Scene(root, 600, 400));
            loginStage.setResizable(false);
            loginStage.show();
        } catch (IOException e) {
            showAlert("Error", "Could not load login screen:\n" + e.getMessage(), null);
            e.printStackTrace();
        }
    }

    private void saveSponsorData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SPONSOR_FILE))) {
            oos.writeObject(new java.util.ArrayList<>(sponsorData));
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to save sponsor data", null);
        }
    }

    @SuppressWarnings("unchecked")
    private void loadSponsorData() {
        File f = new File(SPONSOR_FILE);
        if (!f.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            java.util.List<Sponsorship> list =
                    (java.util.List<Sponsorship>) ois.readObject();
            sponsorData.setAll(list);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load sponsor data", null);
        }
    }

    private void showAlert(String t, String m, javafx.stage.Window w) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(t);
        a.setHeaderText(null);
        a.setContentText(m);
        if (w != null) a.initOwner(w);
        a.showAndWait();
    }

    // ─── Model Classes ────────────────────────────────────────────────────────────

    public static class Budget {
        private final String budgetName, matchName;
        private final double totalAmount;
        public Budget(String b, String m, double t) { budgetName = b; matchName = m; totalAmount = t; }
        public String getBudgetName() { return budgetName; }
        public String getMatchName()  { return matchName;  }
        public double getTotalAmount(){ return totalAmount;}
    }

    public static class FinancialReport {
        private final String reportDate, details;
        private final double revenue;
        public FinancialReport(String d, double r, String det) { reportDate = d; revenue = r; details = det; }
        public String getReportDate(){ return reportDate; }
        public double getRevenue()    { return revenue;    }
        public String getDetails()    { return details;    }
    }

    public static class PlayerTransfer {
        private final String playerName, transferFee, reviewStatus;
        public PlayerTransfer(String p, String f, String s) { playerName = p; transferFee = f; reviewStatus = s; }
        public String getPlayerName(){ return playerName; }
        public String getTransferFee(){ return transferFee; }
        public String getReviewStatus(){ return reviewStatus;}
    }

    public static class TicketSale {
        private final String matchName, venue;
        private final int ticketsSold;
        private final double totalRevenue;
        public TicketSale(String m, String v, int t, double rev) { matchName = m; venue = v; ticketsSold = t; totalRevenue = rev; }
        public String getMatchName()   { return matchName;   }
        public String getVenue()       { return venue;       }
        public int getTicketsSold()    { return ticketsSold; }
        public double getTotalRevenue(){ return totalRevenue;}
    }

    public static class Sponsorship implements Serializable {
        private final String sponsorName, paymentStatus, contractDetails;
        private final LocalDate date;
        public Sponsorship(String n, String p, String c, LocalDate d) {
            sponsorName = n; paymentStatus = p; contractDetails = c; date = d;
        }
        public String getSponsorName()    { return sponsorName;    }
        public String getPaymentStatus()  { return paymentStatus;  }
        public String getContractDetails(){ return contractDetails;}
        public LocalDate getDate()        { return date;           }
    }
}
