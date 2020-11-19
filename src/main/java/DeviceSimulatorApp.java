import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import vn.com.tma.configs.JMSConfig;
import vn.com.tma.connectserver.ClientToServer;
import vn.com.tma.sender.Receiver;

import javax.jms.*;

public class DeviceSimulatorApp {
    public static void main(String[] args) throws JMSException {

        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");

        Receiver receiver = new Receiver(context);
        receiver.receiveCommandMessage();

//        Register device to server
        ClientToServer client = new ClientToServer();
        String deviceInfo = "{\"name\": \"BCB-1\", " +
                "\"address\": \"192.168.20.6\", " +
                "\"macAddress\": \"00-15-E9-2B-99-B5\", " +
                "\"status\": \"Up\", " +
                "\"type\": \"06582-T20\", " +
                "\"version\": \"1.2.2\"}";
        client.registerDevice(deviceInfo);

    }

}
