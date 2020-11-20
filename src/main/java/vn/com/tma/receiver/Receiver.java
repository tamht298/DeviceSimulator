package vn.com.tma.receiver;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import vn.com.tma.configs.JMSConfig;
import vn.com.tma.documents.Device;

import javax.jms.*;

public class Receiver {
    private ApplicationContext context;

    @Autowired
    public Receiver(ApplicationContext context) {
        this.context = context;
    }


    public void receiveCommandMessage(Device device) throws JMSException {
        JMSConfig jmsConfig = (JMSConfig) context.getBean("jmsConfig");

        /**
         * Initial connection factory
         */
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(jmsConfig.getUrlBrokerConnection());
        Connection connection = connectionFactory.createConnection(jmsConfig.getUsername(), jmsConfig.getPassword());
        connection.start();

        // Creating session for sending messages
        Session session = connection.createSession(false,
                Session.CLIENT_ACKNOWLEDGE);
        Destination destination = session.createQueue("DeviceQueue");
        Destination destinationReplyDeviceQueue = session.createQueue("DeviceReplyQueue");

        // MessageConsumer is used for receiving (consuming) messages
        MessageConsumer consumer = session.createConsumer(destination, "JMSCorrelationID='" + device.getMacAddress() + "'");
        MessageProducer producer = session.createProducer(null);
        String body;

        do {
            // Here we receive the message.
            Message message = consumer.receive();
            message.acknowledge();

            body = ((TextMessage) message).getText();
            System.out.println("Received message: '" + body + "' with macAddress: " + device.getMacAddress());

            TextMessage reply = session.createTextMessage("great");
            reply.setJMSCorrelationID(message.getJMSCorrelationID());

            reply.setJMSReplyTo(destinationReplyDeviceQueue);
            producer.send(destinationReplyDeviceQueue, reply);
            System.out.println("Reply message: " + reply);

        } while (!body.equalsIgnoreCase("SHUTDOWN"));
        connection.close();
        System.out.println("Device " + device.getMacAddress() + " shutdown");
//        System.exit(1);
    }

    public void receiveStatusTopic(Device device) throws JMSException, InterruptedException {
        JMSConfig jmsConfig = (JMSConfig) context.getBean("jmsConfig");

        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(jmsConfig.getUrlBrokerConnection());
        Connection connection = connectionFactory.createConnection(jmsConfig.getUsername(), jmsConfig.getPassword());
        connection.start();

        Session session = connection.createSession(true,
                Session.CLIENT_ACKNOWLEDGE);
        Topic topic = session.createTopic("DeviceStatusTopic");
        MessageConsumer consumer = session.createConsumer(topic, "message_type='request'", false);
        MessageProducer producer = session.createProducer(null);

        while (true) {
            Thread.sleep(5000);
            Message message = consumer.receive();
            System.out.println("message from topic " + ((TextMessage) message).getText());

            Message reply = session.createTextMessage("Up");
            reply.setJMSCorrelationID(device.getMacAddress());
            reply.setStringProperty("message_type", "response");
            reply.setJMSReplyTo(topic);
            producer.send(topic, reply);
            System.out.println("Reply message: " + reply);
        }

    }

}
