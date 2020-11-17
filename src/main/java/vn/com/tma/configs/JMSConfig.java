package vn.com.tma.configs;

import org.springframework.beans.factory.annotation.Value;


public class JMSConfig {

    private String urlBrokerConnection;
    private String username;
    private String password;
    private String queueName;

    public JMSConfig() {
    }

    public String getUrlBrokerConnection() {
        return urlBrokerConnection;
    }

    public void setUrlBrokerConnection(String urlBrokerConnection) {
        this.urlBrokerConnection = urlBrokerConnection;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }
}
