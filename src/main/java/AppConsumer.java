import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import vn.com.tma.configs.JMSConfig;

import javax.jms.*;

public class AppConsumer {
    public static void main(String[] args) throws JMSException {

        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
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

        // MessageConsumer is used for receiving (consuming) messages
        MessageConsumer consumer = session.createConsumer(destination);

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
