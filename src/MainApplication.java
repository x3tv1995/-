import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class MainApplication {
    private final ReadingManager readingManager;
    private final UserManager userManager;
    private final UserAction userRole;
    private final Set<UserPermission> userPermissions;
    public MainApplication(UserManager userManager, ReadingManager readingManager, String userRole, Set<?> userPermissions) {
        this.userManager = userManager;
        this.readingManager = readingManager;
        this.userRole = UserAction.valueOf(userRole);
        this.userPermissions = (Set<UserPermission>) userPermissions;
    }
    public UserAction getUserRole() {
        return userRole;
    }
    public static Set<UserAction> getUserPermissions(User user) {
        Set<UserAction> permissions = new HashSet<>();

        switch (user.getRole()) {
            case "ADMIN":
                // Администратор имеет права на все действия
                permissions.addAll(Arrays.asList(UserAction.values()));
                break;
            case "USER":
                // Обычный пользователь имеет ограниченные права
                permissions.add(UserAction.ADD_NEW_POKAZANIYA);
                // Добавьте другие разрешения для обычного пользователя при необходимости
                break;
            // Добавьте логику для других ролей при необходимости
        }

        return permissions;
    }

    // Добавим метод для проверки разрешения пользователя
   public boolean hasPermission(User user, UserAction action) {
        Set<UserAction> permissions = getUserPermissions(user);
        return permissions.contains(action);
    }
    // Метод для обработки эндпоинта ввода данных по счетчику
    public void submitCounterData(User user) {
        // Изменение: Передаем объект User, созданный в методе main
        System.out.println("Введите данные по счетчику:");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.print("Значение счетчика: ");
            double counterValue = Double.parseDouble(reader.readLine());

            System.out.print("Месяц: ");
            String month = reader.readLine();

            Reading newReading = new Reading(user.getId(), counterValue, month);
            readingManager.submitReading(newReading);

            System.out.println("Данные по счетчику успешно введены.");
        } catch (IOException | NumberFormatException e) {
            System.out.println("Ошибка ввода данных по счетчику.");
            e.printStackTrace();
        }
    }

    public void performDropProfile(User user) {
        System.out.println("Действие: Удаление профиля. Пользователь: " + user.getUsername());
        // Ваша логика удаления профиля
        userManager.getUsers().remove(user);
        System.out.println("Профиль пользователя успешно удален.");
    }


    // Метод для обработки эндпоинта получения актуальных показаний
    public List<Reading> getActualReadings(int userId) {
        // Изменение: Добавлена логика обработки полученных данных и их вывода
        List<Reading> actualReadings = readingManager.getLatestReadings(userId, userRole);
        System.out.println("Actual Readings: " + actualReadings);
        return actualReadings;
    }

    // Метод для обработки эндпоинта подачи показаний
    public void submitReading(Reading reading) {
        // Изменение: Передаем объект Reading, созданный в методе main
        readingManager.submitReading(reading);
    }

    // Метод для обработки эндпоинта получения показаний за конкретный месяц
    public void getMonthReadings(int userId, String month) {
        // Изменение: Добавлена логика обработки полученных данных и их вывода
        List<Reading> monthReadings = readingManager.getMonthReadings(userId, month);
        System.out.println("Readings for " + month + ": " + monthReadings);
    }

    // Метод для обработки эндпоинта получения истории показаний
    public List<Reading> getReadingHistory(int userId) {
        // Изменение: Добавлена логика обработки полученных данных и их вывода
        List<Reading> historyReadings = readingManager.getReadingHistory(userId, userRole);
        System.out.println("Reading History: " + historyReadings);
        return historyReadings;
    }




    }
