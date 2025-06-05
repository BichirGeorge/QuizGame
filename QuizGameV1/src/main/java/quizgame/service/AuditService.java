package quizgame.service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuditService {

    private static final AuditService INSTANCE = new AuditService();
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private AuditService() {}

    public static AuditService getInstance() {
        return INSTANCE;
    }

    public void logAction(String actionName) {
        try (FileWriter writer = new FileWriter("audit.csv", true)) {
            String timestamp = LocalDateTime.now().format(FORMATTER);
            writer.write(actionName + "," + timestamp + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
