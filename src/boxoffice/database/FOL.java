package boxoffice.database;

public class FOL {
    private int folId;
    private String name;
    private String email;

    // Constructor
    public FOL(int folId, String name, String email) {
        this.folId = folId;
        this.name = name;
        this.email = email;
    }

    // Getters and Setters
    public int getFolId() { return folId; }
    public void setFolId(int folId) { this.folId = folId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
