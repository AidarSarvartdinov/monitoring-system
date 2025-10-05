package com.monitoring.common;

import java.util.Objects;

public class Service {
    private String url;
    private int port;

    public Service(String url, int port) {
        this.url = url;
        this.port = port;
    }

    public String getUrl() {
        return url;
    }

    public int getPort() {
        return port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Service service = (Service) o;
        return url.equals(service.getUrl()) && port == service.getPort();
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, port);
    }

    @Override
    public String toString() {
        return url + ":" + port;
    }

}
