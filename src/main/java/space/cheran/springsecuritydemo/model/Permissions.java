package space.cheran.springsecuritydemo.model;

public enum Permissions {
    DEVELOPERS_READ("developers.read"),
    DEVELOPERS_WRITE("developers.write");

    private String permission;

    Permissions(String permission) {
        this.permission = permission;
    }
    public String getPermission() {
        return permission;
    }
}
