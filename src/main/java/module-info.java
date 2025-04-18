module cse213.cse213_sporting_club_operations {
    requires javafx.controls;
    requires javafx.fxml;

    opens cse213.cse213_sporting_club_operations to javafx.fxml;
    opens cse213.cse213_sporting_club_operations.TanvirMahmud to javafx.fxml, javafx.base;

    exports cse213.cse213_sporting_club_operations;
}