package qs.config.db;

public enum EnumDataSourceName {
    USER("mydb"), WINDS("winds");
    private String name;

    EnumDataSourceName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

}