package ic.doc.sgo;

public enum Attribute {
    TIMEZONE("timezone"),
    AGE("age"),
    GENDER("gender");

    private final String name;

    Attribute(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
