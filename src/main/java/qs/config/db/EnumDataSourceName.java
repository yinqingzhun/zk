package qs.config.db;

public enum EnumDataSourceName {
    TICKET_BASE("ticket-base"), TICKET_ORDER("ticket-order"), TICKET_USER("ticket-user");
    private String name;

    EnumDataSourceName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

}