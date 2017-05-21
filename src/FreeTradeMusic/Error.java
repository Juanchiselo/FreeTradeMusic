package FreeTradeMusic;

public enum Error
{
    /*
        Error Codes:
            0-49: UI
            50-99: Database
            100-149: Blockchain
     */
    NO_ERROR(0, "There are no errors."),
    USERNAME_EMPTY(1, "Username field is empty."),
    PASSWORD_EMPTY(2, "Password field is empty."),
    BOTH_EMPTY(3, "Username and Password fields are empty."),
    CONFIRM_PASSWORD_EMPTY(4, "Confirm Password field is empty."),
    EMAIL_EMPTY(5, "Email field is empty."),
    ALL_EMPTY(6, "All fields are empty."),
    USERNAME_INVALID(7, "Invalid username. Should have only alphanumeric characters."),
    PASSWORD_INVALID(8, "Invalid password. Should be at least 8 characters long."),
    EMAIL_INVALID(9, "Invalid email."),
    PASSWORDS_NO_MATCH(10, "Passwords do not match."),
    TITLE_EMPTY(11, "Title field is empty."),
    GENRE_EMPTY(12, "Genre field is empty."),
    YEAR_EMPTY(13, "Year field is empty."),
    DATABASE_ERROR(50, "Database error."),
    USERNAME_WRONG(51, "Username is wrong."),
    PASSWORD_WRONG(52, "Password is wrong."),
    BOTH_WRONG(53, "Username and Password are wrong."),
    USERNAME_NOT_AVAILABLE(54, "Username is not available."),
    EMAIL_NOT_AVAILABLE(55, "Email is not available.");

    private final int code;
    private final String description;

    Error(int code, String description)
    {
        this.code = code;
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }

    public int getCode()
    {
        return code;
    }

    @Override
    public String toString()
    {
        return code + ": " + description;
    }
}