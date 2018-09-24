package qs.config.db;

public enum EnumDataSourceName {
    USER("mydb"), TOPIC("mydb2");
    private String name;

    EnumDataSourceName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

}