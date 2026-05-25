package schleifenbauer.dal;

public record DatabaseConfig(
        String host,
        int port,
        String databaseName,
        String user,
        String password) {

    public String jdbcUrl() {
        return "jdbc:mariadb://%s:%d/%s".formatted(host, port, databaseName);
    }
}
