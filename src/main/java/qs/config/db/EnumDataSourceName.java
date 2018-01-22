package qs.config.db;

public enum EnumDataSourceName {
    TICKET_BASE("mydb"), TICKET_ORDER("mydb2");
    private String name;

    EnumDataSourceName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

}