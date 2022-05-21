package dto;

public class AuthRequest implements BasicRequest {

    public static String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    private static String login;
    private String password;

    public AuthRequest(String login, String password) {
        this.login = login;
        this.password = password;
    }

    @Override
    public String getType() {
        return "auth";
    }
}
