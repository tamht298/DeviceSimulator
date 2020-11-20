import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import vn.com.tma.documents.Device;
import vn.com.tma.receiver.Receiver;

import javax.jms.*;

public class DeviceSimulatorApp {
    public static void main(String[] args) throws JMSException {

        final ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");

        Thread t1 = new Thread(new Runnable() {
            public void run() {
                System.out.println("Thread 1 is running ...");
                Device device1 = (Device) context.getBean("device1");
                Receiver receiver = new Receiver(context);
                try {
                    receiver.receiveStatusTopic(device1);
                } catch (JMSException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread t2 = new Thread(new Runnable() {
            public void run() {
                System.out.println("Thread 1 is running ...");
                Device device2 = (Device) context.getBean("device2");
                Receiver receiver = new Receiver(context);
                try {
                    receiver.receiveStatusTopic(device2);
                } catch (JMSException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        t1.start();
        t2.start();

//        Thread thread1 = new Thread() {
//            @Override
//            public void run() {
//                super.run();
//
//                Device device1 = (Device) context.getBean("device1");
//                System.out.println("Thread 1 is running... with macAddress: "+ device1.getMacAddress());
//                Receiver receiver = new Receiver(context);
//                try {
//                    receiver.receiveCommandMessage(device1);
//                } catch (JMSException e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//        Thread thread2 = new Thread() {
//            @Override
//            public void run() {
//                super.run();
//
//                Device device2 = (Device) context.getBean("device2");
//                System.out.println("Thread 2 is running... with macAddress: "+ device2.getMacAddress());
//
//                Receiver receiver = new Receiver(context);
//                try {
//                    receiver.receiveCommandMessage(device2);
//                } catch (JMSException e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//
//        thread1.start();
//        thread2.start();


//        Register device to server
//        ClientToServer client = new ClientToServer();
//        String deviceInfo = "{\"name\": \"BCB-1\", " +
//                "\"address\": \"192.168.20.6\", " +
//                "\"macAddress\": \"00-15-E9-2B-99-B5\", " +
//                "\"status\": \"Up\", " +
//                "\"type\": \"06582-T20\", " +
//                "\"version\": \"1.2.2\"}";
//        client.registerDevice(deviceInfo);

    }

}
