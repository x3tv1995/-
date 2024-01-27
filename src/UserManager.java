import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


public class UserManager {
    private static final Logger LOGGER = Logger.getLogger(UserManager.class.getName());// создание статического логгера. Логгер - это инструмент для записи различной информации в журнал (лог). Здесь создается логгер для класса UserManager.
    public List<User> getUsers() {
        return users;
    }

    private final List<User> users;
    private final String dataFileName = "user_data.ser"; // Название файла для сохранения данных

    public UserManager() {
        this.users = readUserDataFromFile();
    }

    public void registerUser(User user) {
        users.add(user);
        saveUserDataToFile();
        System.out.println( UserAction.User_registered.getDescription());
        logUserAction(user, UserAction.REGISTER);//вызов метода logUserAction для записи информации о действии пользователя в лог. Передаются объект User и константа UserAction.REGISTER.
    }
    private void logUserAction(User user, UserAction action) {
        LOGGER.info("User " + user.getUsername() + " " + action.getDescription()); //вызов метода логгера info для записи информации. Запись содержит имя пользователя и описание действия из объекта UserAction.
    }
    private void saveUserDataToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dataFileName))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private List<User> readUserDataFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataFileName))) {
            Object obj = ois.readObject();
            if (obj instanceof List<?>) {
                return (List<User>) obj;
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Ошибка в течение файла"+e);
            // В случае ошибки чтения файла, вернем пустой список
        }
        return new ArrayList<>();
    }
    public void performAction(User user, UserAction action) {
        if (hasPermission(user, action)) {
            // Выполнение действия
            switch (action) {
                case ADD_NEW_POKAZANIYA:
                    performAddNewPokazaniya(user);
                    break;
                case DROP_PROFILE:
                    performDropProfile(user);
                    break;
                // Добавьте логику для других действий по мере необходимости
                default:
                    System.out.println("Неподдерживаемое действие: " + action.getDescription());
            }
        } else {
            System.out.println("У вас нет прав на выполнение этого действия.");
        }
    }

    private boolean hasPermission(User user, UserAction action) {
        // Получаем роль пользователя
        String userRole = user.getRole();

        // Проверяем разрешения в зависимости от роли
        switch (userRole) {
            case "ADMIN":
                // Администратор имеет права на все действия
                return true;
            case "USER":
                // Обычный пользователь имеет ограниченные права
                switch (action) {
                    case ADD_NEW_POKAZANIYA:
                        // Разрешено добавление новых показаний
                        return true;
                    // Добавьте другие разрешения для обычного пользователя при необходимости
                    default:
                        return false;
                }
                // Добавьте логику для других ролей при необходимости
            default:
                return false;
        }
    }

    private void performAddNewPokazaniya(User user) {
        System.out.println("Действие: Добавление новых показаний. Пользователь: " + user.getUsername());
        // Ваша логика добавления новых показаний
        // Пример: Предположим, у вас есть объект Reading, который вы хотите добавить
        Reading newReading = new Reading(user.getId(), 150.0, "January");
        ReadingManager readingManager = new ReadingManager();
        readingManager.submitReading(newReading);
        System.out.println("Новые показания успешно добавлены.");
    }

    private void performDropProfile(User user) {
        System.out.println("Действие: Удаление профиля. Пользователь: " + user.getUsername());
        // Ваша логика удаления профиля
        UserManager userManager = new UserManager();
        userManager.getUsers().remove(user);
        System.out.println("Профиль пользователя успешно удален.");
    }
    public User findUserByRole(String role) {
        for (User user : users) {
            if (user.getRole().equals(role)) {
                return user;
            }
        }
        return null;
    }
}


