module cse213.cse213_sporting_club_operations {
    requires javafx.controls;
    requires javafx.fxml;

    // Open the package containing your controller class to JavaFX FXML
    opens cse213.cse213_sporting_club_operations.TanvirMahmud to javafx.fxml;

    // Main application package
    opens cse213.cse213_sporting_club_operations to javafx.fxml;
    exports cse213.cse213_sporting_club_operations;
}