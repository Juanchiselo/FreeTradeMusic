package FreeTradeMusic;

public class DatabaseManager
{
    private static DatabaseManager instance = null;

    protected DatabaseManager()
    {
        // Exists only to defeat instantiation.
    }
    public static DatabaseManager getInstance()
    {
        if(instance == null)
            instance = new DatabaseManager();
        return instance;
    }

    public boolean login(String username, String password)
    {
        // TODO - Rob: Insert database call for user authentication here.

        if(username.equalsIgnoreCase("user")
                && password.equals("1A1DC91C907325C69271DDF0C944BC72"))
            return true;

        return false;
    }
}
