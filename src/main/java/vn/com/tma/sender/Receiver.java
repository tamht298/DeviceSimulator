package vn.com.tma.sender;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.ApplicationContext;
import vn.com.tma.configs.JMSConfig;
import vn.com.tma.documents.Device;

import javax.jms.*;

public class Receiver {
    private ApplicationContext context;

    public Receiver(ApplicationContext context) {
        this.context = context;
    }

    public void receiveCommandMessage() throws JMSException {
        JMSConfig jmsConfig = (JMSConfig) context.getBean("jmsConfig");

        /**
         * Initial connection factory
         */
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(jmsConfig.getUrlBrokerConnection());
        Connection connection = connectionFactory.createConnection(jmsConfig.getUsername(), jmsConfig.getPassword());
        connection.start();

        // Creating session for sending messages
        Session session = connection.createSession(false,
                Session.AUTO_ACKNOWLEDGE);

        Destination destination = session.createQueue("DeviceQueue");

        Device device1 = (Device) context.getBean("device1");
        // MessageConsumer is used for receiving (consuming) messages
        MessageConsumer consumer = session.createConsumer(destination, "JMSCorrelationID='"+device1.getMacAddress()+"'");

        String body;

        do {
            // Here we receive the message.
            Message message = consumer.receive();
            body = ((TextMessage) message).getText();
            System.out.println("Received message '" + body + "'");
        } while (!body.equalsIgnoreCase("SHUTDOWN"));
        connection.close();
        System.exit(1);
    }

}
