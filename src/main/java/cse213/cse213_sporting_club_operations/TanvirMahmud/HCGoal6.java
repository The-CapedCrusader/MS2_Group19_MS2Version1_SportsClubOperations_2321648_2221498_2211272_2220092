package cse213.cse213_sporting_club_operations.TanvirMahmud;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class HCGoal6 {
    @FXML private ComboBox<String> meetingTypeComboBox;
    @FXML private DatePicker meetingDatePicker;
    @FXML private ComboBox<String> meetingTimeComboBox;
    @FXML private ComboBox<String> locationComboBox;
    @FXML private TextField meetingTitleField;
    @FXML private TextArea meetingAgendaArea;
    @FXML private ListView<String> attendeeListView;
    @FXML private ComboBox<String> participantComboBox;
    @FXML private Button addParticipantButton;
    @FXML private Button removeParticipantButton;
    @FXML private CheckBox notifyParticipantsCheckBox;
    @FXML private Button scheduleMeetingButton;

    @FXML private TableView<MeetingRecord> scheduledMeetingsTable;
    @FXML private TableColumn<MeetingRecord, String> titleColumn;
    @FXML private TableColumn<MeetingRecord, String> typeColumn;
    @FXML private TableColumn<MeetingRecord, LocalDate> dateColumn;
    @FXML private TableColumn<MeetingRecord, String> timeColumn;
    @FXML private TableColumn<MeetingRecord, String> locationColumn;
    @FXML private TableColumn<MeetingRecord, Integer> attendeesColumn;
    @FXML private TableColumn<MeetingRecord, String> statusColumn;

    @FXML private TextArea meetingNotesArea;
    @FXML private TextArea actionItemsArea;
    @FXML private Button saveMeetingRecordButton;
    @FXML private ComboBox<String> meetingStatusComboBox;
    @FXML private Button generateReportButton;

    private ObservableList<String> attendees = FXCollections.observableArrayList();
    private ObservableList<MeetingRecord> meetings = FXCollections.observableArrayList();
    private ObservableList<String> participants = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupMeetingTypeOptions();
        setupTimeOptions();
        setupLocationOptions();
        setupParticipantsList();
        setupMeetingStatusOptions();
        setupTableColumns();
        loadSampleData();

        // Set up default values
        meetingDatePicker.setValue(LocalDate.now());

        // Add listeners for button enablement
        addParticipantButton.setDisable(true);
        participantComboBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> addParticipantButton.setDisable(newValue == null));

        removeParticipantButton.setDisable(true);
        attendeeListView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> removeParticipantButton.setDisable(newValue == null));

        // Set up attendee list
        attendeeListView.setItems(attendees);
    }

    private void setupMeetingTypeOptions() {
        meetingTypeComboBox.getItems().addAll(
                "Team Tactical Analysis",
                "Pre-Match Briefing",
                "Post-Match Analysis",
                "Individual Performance Reviews",
                "Staff Coordination Meeting",
                "Season Planning",
                "Opposition Analysis",
                "Training Focus Meeting"
        );
    }

    private void setupTimeOptions() {
        ObservableList<String> times = FXCollections.observableArrayList();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");

        LocalTime time = LocalTime.of(8, 0);
        LocalTime endTime = LocalTime.of(20, 0);

        while (!time.isAfter(endTime)) {
            times.add(time.format(formatter));
            time = time.plusMinutes(30);
        }

        meetingTimeComboBox.setItems(times);
        meetingTimeComboBox.setValue("10:00 AM");
    }

    private void setupLocationOptions() {
        locationComboBox.getItems().addAll(
                "Main Conference Room",
                "Video Analysis Suite",
                "Training Ground Meeting Room",
                "Head Coach's Office",
                "Team Lounge",
                "Boardroom",
                "Stadium Meeting Room"
        );
    }

    private void setupParticipantsList() {
        participants.addAll(
                "Assistant Coach - Tactical",
                "Assistant Coach - Technical",
                "Goalkeeper Coach",
                "Fitness Coach",
                "Performance Analyst",
                "Team Captain",
                "All Defensive Players",
                "All Midfield Players",
                "All Forward Players",
                "Medical Staff",
                "Entire Squad",
                "Scouting Team"
        );

        participantComboBox.setItems(participants);
    }

    private void setupMeetingStatusOptions() {
        meetingStatusComboBox.getItems().addAll(
                "Scheduled",
                "Completed",
                "Cancelled",
                "Postponed"
        );
        meetingStatusComboBox.setValue("Scheduled");
    }

    private void setupTableColumns() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        attendeesColumn.setCellValueFactory(new PropertyValueFactory<>("attendeeCount"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        scheduledMeetingsTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        populateSelectedMeetingDetails(newValue);
                    }
                });
    }

    private void loadSampleData() {
        meetings.addAll(
                new MeetingRecord(
                        "Pre-season tactical approach",
                        "Season Planning",
                        LocalDate.now().plusDays(2),
                        "10:00 AM",
                        "Main Conference Room",
                        6,
                        "Scheduled"
                ),
                new MeetingRecord(
                        "Opposition analysis: Manchester City",
                        "Pre-Match Briefing",
                        LocalDate.now().plusDays(1),
                        "2:00 PM",
                        "Video Analysis Suite",
                        15,
                        "Scheduled"
                ),
                new MeetingRecord(
                        "Defensive unit performance review",
                        "Post-Match Analysis",
                        LocalDate.now().minusDays(2),
                        "9:30 AM",
                        "Training Ground Meeting Room",
                        7,
                        "Completed"
                )
        );

        scheduledMeetingsTable.setItems(meetings);
    }

    private void populateSelectedMeetingDetails(MeetingRecord meeting) {
        meetingTitleField.setText(meeting.getTitle());
        meetingTypeComboBox.setValue(meeting.getType());
        meetingDatePicker.setValue(meeting.getDate());
        meetingTimeComboBox.setValue(meeting.getTime());
        locationComboBox.setValue(meeting.getLocation());
        meetingStatusComboBox.setValue(meeting.getStatus());

        // Sample data for meeting details
        meetingNotesArea.setText(generateSampleNotes(meeting));
        actionItemsArea.setText(generateSampleActionItems(meeting));

        // Clear and update attendees
        attendees.clear();
        if (meeting.getTitle().contains("Opposition")) {
            attendees.addAll("Entire Squad", "Performance Analyst", "Assistant Coach - Tactical");
        } else if (meeting.getTitle().contains("Defensive")) {
            attendees.addAll("Assistant Coach - Defensive", "All Defensive Players", "Team Captain");
        } else {
            attendees.addAll("Assistant Coach - Tactical", "Assistant Coach - Technical", "Fitness Coach");
        }
    }

    private String generateSampleNotes(MeetingRecord meeting) {
        if (meeting.getType().equals("Pre-Match Briefing")) {
            return "- Opposition tends to build up play through the right flank\n" +
                    "- Their striker has scored 70% of goals from crosses\n" +
                    "- Midfield press can be exploited through quick transitions\n" +
                    "- Set-piece vulnerability when defending near post corners";
        } else if (meeting.getType().equals("Post-Match Analysis")) {
            return "- Defensive shape improved in the second half\n" +
                    "- Need to work on quicker transitions from defense to attack\n" +
                    "- Positional discipline was excellent\n" +
                    "- Need to increase shots on target ratio";
        } else {
            return "- Review of season objectives and tactical philosophy\n" +
                    "- Discussion of player roles and responsibilities\n" +
                    "- Analysis of pre-season performance metrics\n" +
                    "- Identified areas for tactical improvement";
        }
    }

    private String generateSampleActionItems(MeetingRecord meeting) {
        if (meeting.getType().equals("Pre-Match Briefing")) {
            return "1. Defensive unit to practice against cross scenarios\n" +
                    "2. Midfielders to focus on quick transition drills\n" +
                    "3. Set piece defensive assignments to be distributed\n" +
                    "4. Individual player matchup details to be shared";
        } else if (meeting.getType().equals("Post-Match Analysis")) {
            return "1. Additional crossing practice for fullbacks\n" +
                    "2. Video session on transition moments for attackers\n" +
                    "3. Individual feedback sessions scheduled\n" +
                    "4. Fitness coach to review stamina data";
        } else {
            return "1. Finalize team tactical document for season\n" +
                    "2. Prepare individual role briefings\n" +
                    "3. Schedule follow-up specific unit meetings\n" +
                    "4. Performance team to establish season benchmarks";
        }
    }

    @FXML
    public void addParticipant() {
        String selected = participantComboBox.getValue();
        if (selected != null && !attendees.contains(selected)) {
            attendees.add(selected);
            participantComboBox.getSelectionModel().clearSelection();
            addParticipantButton.setDisable(true);
        }
    }

    @FXML
    public void removeParticipant() {
        String selected = attendeeListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            attendees.remove(selected);
            removeParticipantButton.setDisable(true);
        }
    }

    @FXML
    public void scheduleMeeting() {
        String title = meetingTitleField.getText();
        String type = meetingTypeComboBox.getValue();
        LocalDate date = meetingDatePicker.getValue();
        String time = meetingTimeComboBox.getValue();
        String location = locationComboBox.getValue();

        if (title == null || title.trim().isEmpty() ||
                type == null || date == null || time == null || location == null) {
            showAlert(Alert.AlertType.WARNING, "Incomplete Information",
                    "Please fill in all required fields (title, type, date, time, and location).");
            return;
        }

        if (attendees.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "No Participants",
                    "Please add at least one participant to the meeting.");
            return;
        }

        MeetingRecord newMeeting = new MeetingRecord(
                title, type, date, time, location, attendees.size(), "Scheduled");

        meetings.add(newMeeting);
        scheduledMeetingsTable.refresh();

        if (notifyParticipantsCheckBox.isSelected()) {
            showAlert(Alert.AlertType.INFORMATION, "Notifications Sent",
                    "Meeting notifications have been sent to all participants.");
        }

        // Clear form for next entry
        clearMeetingForm();
    }

    private void clearMeetingForm() {
        meetingTitleField.clear();
        meetingTypeComboBox.getSelectionModel().clearSelection();
        meetingDatePicker.setValue(LocalDate.now());
        meetingTimeComboBox.getSelectionModel().clearSelection();
        locationComboBox.getSelectionModel().clearSelection();
        attendees.clear();
        meetingAgendaArea.clear();
        notifyParticipantsCheckBox.setSelected(false);
    }

    @FXML
    public void saveMeetingRecord() {
        MeetingRecord selectedMeeting = scheduledMeetingsTable.getSelectionModel().getSelectedItem();
        if (selectedMeeting == null) {
            showAlert(Alert.AlertType.WARNING, "No Meeting Selected",
                    "Please select a meeting to save notes for.");
            return;
        }

        // Update meeting status
        selectedMeeting.setStatus(meetingStatusComboBox.getValue());
        scheduledMeetingsTable.refresh();

        showAlert(Alert.AlertType.INFORMATION, "Meeting Record Saved",
                "Meeting notes and action items have been saved successfully.");
    }

    @FXML
    public void generateReport() {
        MeetingRecord selectedMeeting = scheduledMeetingsTable.getSelectionModel().getSelectedItem();
        if (selectedMeeting == null) {
            showAlert(Alert.AlertType.WARNING, "No Meeting Selected",
                    "Please select a meeting to generate a report for.");
            return;
        }

        showAlert(Alert.AlertType.INFORMATION, "Report Generated",
                "Meeting report has been generated and shared with relevant stakeholders.");
    }

    @FXML
    public void deleteMeeting() {
        MeetingRecord selectedMeeting = scheduledMeetingsTable.getSelectionModel().getSelectedItem();
        if (selectedMeeting == null) {
            showAlert(Alert.AlertType.WARNING, "No Meeting Selected",
                    "Please select a meeting to delete.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm Deletion");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Are you sure you want to delete the selected meeting?");

        if (confirmation.showAndWait().get() == ButtonType.OK) {
            meetings.remove(selectedMeeting);
            clearMeetingForm();
            meetingNotesArea.clear();
            actionItemsArea.clear();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void dashboard() {
        try {
            // Navigate back to the Head Coach dashboard
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DashboardHeadCoach.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) meetingTypeComboBox.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error",
                    "Could not return to dashboard. Error: " + e.getMessage());
        }
    }

    // Data model for meeting records
    public static class MeetingRecord {
        private final SimpleStringProperty title;
        private final SimpleStringProperty type;
        private final javafx.beans.property.ObjectProperty<LocalDate> date;
        private final SimpleStringProperty time;
        private final SimpleStringProperty location;
        private final javafx.beans.property.SimpleIntegerProperty attendeeCount;
        private final SimpleStringProperty status;

        public MeetingRecord(String title, String type, LocalDate date, String time,
                             String location, int attendeeCount, String status) {
            this.title = new SimpleStringProperty(title);
            this.type = new SimpleStringProperty(type);
            this.date = new javafx.beans.property.SimpleObjectProperty<>(date);
            this.time = new SimpleStringProperty(time);
            this.location = new SimpleStringProperty(location);
            this.attendeeCount = new javafx.beans.property.SimpleIntegerProperty(attendeeCount);
            this.status = new SimpleStringProperty(status);
        }

        public String getTitle() { return title.get(); }
        public SimpleStringProperty titleProperty() { return title; }

        public String getType() { return type.get(); }
        public SimpleStringProperty typeProperty() { return type; }

        public LocalDate getDate() { return date.get(); }
        public javafx.beans.property.ObjectProperty<LocalDate> dateProperty() { return date; }

        public String getTime() { return time.get(); }
        public SimpleStringProperty timeProperty() { return time; }

        public String getLocation() { return location.get(); }
        public SimpleStringProperty locationProperty() { return location; }

        public int getAttendeeCount() { return attendeeCount.get(); }
        public javafx.beans.property.SimpleIntegerProperty attendeeCountProperty() { return attendeeCount; }

        public String getStatus() { return status.get(); }
        public void setStatus(String value) { status.set(value); }
        public SimpleStringProperty statusProperty() { return status; }
    }
}