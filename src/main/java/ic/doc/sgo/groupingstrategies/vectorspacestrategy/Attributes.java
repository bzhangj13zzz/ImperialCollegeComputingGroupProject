package ic.doc.sgo.groupingstrategies.vectorspacestrategy;

enum Attributes {
    TIMEZONE("timezone"),
    AGE("age"),
    GENDER("gender");

    private final String name;
    Attributes(String name) {
        this.name = name;
    }
}
