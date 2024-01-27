import java.io.Serializable;

public enum UserAction implements Serializable {
    USER("Пользователь"),
    ADMIN("Админ"),
    REGISTER("REGISTER"),
    NULL("null"),
    ADD_NEW_POKAZANIYA("добавил показания"),
    DROP_PROFILE("удалил профиль"),

    User_registered("Пользователь зарегестрирован") ;


    private final String description;

    UserAction(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
    public static UserAction getByDescription(String description) {
        for (UserAction action : values()) {
            if (action.description.equals(description)) {
                return action;
            }
        }
        return NULL;
    }
}
