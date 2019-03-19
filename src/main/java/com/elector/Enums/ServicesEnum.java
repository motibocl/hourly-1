package com.elector.Enums;

public enum ServicesEnum {
    election_day_in_memory_service("http://localhost:8788/", true, "api-election-day"),
    voters_in_memory_service("http://localhost:8789/", true, "api-voters"),
    general_jobs_service("http://localhost:8790/", true, "api-jobs"),
    sms_service("http://localhost:8791/", true, "api-sms"),
    elector("http://localhost:8787/", true, "api"),
    reports("http://localhost:8793/", true, "api-reports"),
    excel("http://localhost:8795/", true, "api-excel"),
    inquiries("http://localhost:8794/", true, "api-inquiries"),
    groups_managers("http://localhost:8796/", true, "api-group-manager"),
    db_loader("http://localhost:8797/", true, "api-db-loader");

    private String url;
    private boolean up;
    private String restPath;

    ServicesEnum(String url, boolean up, String restPath) {
        this.url = url;
        this.up = up;
        this.restPath = restPath;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public String getRestPath() {
        return restPath;
    }

    public void setRestPath(String restPath) {
        this.restPath = restPath;
    }
}
