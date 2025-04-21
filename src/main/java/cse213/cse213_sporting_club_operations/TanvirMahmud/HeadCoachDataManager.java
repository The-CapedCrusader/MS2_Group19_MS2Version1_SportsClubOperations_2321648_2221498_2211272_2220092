package cse213.cse213_sporting_club_operations.TanvirMahmud;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HeadCoachDataManager {
    // Use relative path instead of absolute path
    private static final String DATA_DIRECTORY = "data";
    private static final String USER_DATA_FILE = DATA_DIRECTORY + File.separator + "user.bin";
    private static final String LINEUPS_FILE = DATA_DIRECTORY + File.separator + "lineups.bin";
    private static final String TACTICS_FILE = DATA_DIRECTORY + File.separator + "tactics.bin";
    private static final String TEAM_STATS_FILE = DATA_DIRECTORY + File.separator + "teamstats.bin";
    private static final String LINEUPS_DIR = DATA_DIRECTORY + File.separator + "lineups";

    // Initialize data directory
    public static void initializeDataDirectory() {
        // Create main data directory
        createDirectory(DATA_DIRECTORY);

        // Create lineups subdirectory
        createDirectory(LINEUPS_DIR);

        // Check if user.bin exists, if not, create sample data
        File userFile = new File(USER_DATA_FILE);
        if (!userFile.exists()) {
            saveSamplePlayers();
        }
    }

    private static void createDirectory(String dirPath) {
        File directory = new File(dirPath);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (created) {
                System.out.println("Created directory: " + dirPath);
            } else {
                System.err.println("Failed to create directory: " + dirPath);
            }
        }
    }

    private static void saveSamplePlayers() {
        ArrayList<Player> players = new ArrayList<>();
        players.add(new Player("David De Gea", "34", "GK", "95%", "$12M"));
        players.add(new Player("Trent Alexander-Arnold", "24", "RB", "90%", "$45M"));
        players.add(new Player("Virgil van Dijk", "31", "CB", "85%", "$35M"));
        players.add(new Player("Raphael Varane", "29", "CB", "80%", "$30M"));
        players.add(new Player("Andrew Robertson", "28", "LB", "92%", "$40M"));
        players.add(new Player("Rodri", "26", "CDM", "93%", "$50M"));
        players.add(new Player("Kevin De Bruyne", "31", "CM", "88%", "$70M"));
        players.add(new Player("Bruno Fernandes", "28", "CAM", "87%", "$65M"));
        players.add(new Player("Mohamed Salah", "30", "RW", "89%", "$60M"));
        players.add(new Player("Harry Kane", "29", "ST", "86%", "$80M"));
        players.add(new Player("Son Heung-min", "30", "LW", "91%", "$55M"));

        saveObjectToFile(players, USER_DATA_FILE);
        System.out.println("Created sample players data file");
    }

    // Save lineups to file
    public static void saveLineupsToFile(List<String> lineups) {
        saveObjectToFile(new ArrayList<>(lineups), LINEUPS_FILE);
    }

    // Load lineups from file
    @SuppressWarnings("unchecked")
    public static List<String> loadLineupsFromFile() {
        Object obj = loadObjectFromFile(LINEUPS_FILE);
        if (obj instanceof ArrayList<?>) {
            return (ArrayList<String>) obj;
        }
        return new ArrayList<>();
    }

    // Save tactics to file
    public static void saveTacticsToFile(Map<String, Object> tactics) {
        saveObjectToFile(tactics, TACTICS_FILE);
    }

    // Load tactics from file
    @SuppressWarnings("unchecked")
    public static Map<String, Object> loadTacticsFromFile() {
        Object obj = loadObjectFromFile(TACTICS_FILE);
        if (obj instanceof Map<?, ?>) {
            return (Map<String, Object>) obj;
        }
        return new HashMap<>();
    }

    // Save team stats to file
    public static void saveTeamStatsToFile(Object stats) {
        saveObjectToFile(stats, TEAM_STATS_FILE);
    }

    // Load team stats from file
    public static Object loadTeamStatsFromFile() {
        return loadObjectFromFile(TEAM_STATS_FILE);
    }

    // Load players from User.bin
    @SuppressWarnings("unchecked")
    public static ArrayList<Player> loadPlayersFromUserBin() {
        Object obj = loadObjectFromFile(USER_DATA_FILE);
        if (obj instanceof ArrayList<?>) {
            return (ArrayList<Player>) obj;
        }
        return new ArrayList<>();
    }

    // Generic method to save object to file
    private static void saveObjectToFile(Object obj, String filePath) {
        try {
            // Create parent directories if they don't exist
            File file = new File(filePath);
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(obj);
            oos.close();
            fos.close();
            System.out.println("Data saved to: " + filePath);
        } catch (IOException e) {
            System.err.println("Error saving data to " + filePath + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Generic method to load object from file
    private static Object loadObjectFromFile(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                System.out.println("File not found: " + filePath);
                return null;
            }

            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object obj = ois.readObject();
            ois.close();
            fis.close();
            System.out.println("Data loaded from: " + filePath);
            return obj;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading data from " + filePath + ": " + e.getMessage());
            return null;
        }
    }

    // Save lineup configuration
    public static void saveLineupConfiguration(String lineupName, List<HCGoal1.PlayerLineup> players,
                                               String formation, String opponent, String competition,
                                               String venue, Map<String, String> tactics) {
        Map<String, Object> lineupConfig = new HashMap<>();
        lineupConfig.put("players", new ArrayList<>(players));
        lineupConfig.put("formation", formation);
        lineupConfig.put("opponent", opponent);
        lineupConfig.put("competition", competition);
        lineupConfig.put("venue", venue);
        lineupConfig.put("tactics", tactics);

        String configPath = LINEUPS_DIR + File.separator + lineupName.replaceAll("[\\\\/:*?\"<>|]", "_") + ".bin";
        saveObjectToFile(lineupConfig, configPath);
    }
    public static void saveTrainingSessionsToFile(List<HCGoal2.TrainingSession> sessions) {
        try {
            File file = new File(DATA_DIRECTORY, "training_sessions.bin");
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                oos.writeObject(sessions);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<HCGoal2.TrainingSession> loadTrainingSessionsFromFile() {
        try {
            File file = new File(DATA_DIRECTORY, "training_sessions.bin");
            if (!file.exists()) return new ArrayList<>();

            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                @SuppressWarnings("unchecked")
                List<HCGoal2.TrainingSession> sessions = (List<HCGoal2.TrainingSession>) ois.readObject();
                return sessions;
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    // Load lineup configuration
    @SuppressWarnings("unchecked")
    public static Map<String, Object> loadLineupConfiguration(String lineupName) {
        String configPath = LINEUPS_DIR + File.separator + lineupName.replaceAll("[\\\\/:*?\"<>|]", "_") + ".bin";
        Object obj = loadObjectFromFile(configPath);
        if (obj instanceof Map<?, ?>) {
            return (Map<String, Object>) obj;
        }
        return new HashMap<>();
    }
}