package contextjms;

import java.util.Date;
import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;

/**
 * 
 *  Generic JMS Subscriber - Chnage Props for ActiveMQ and RobbitMQ
 *  It  publish messages 
 * 
 * @author ismail
 *
 */
public final class SimpleProducer {

	
	public static void main(String[] args) {
		
		// Active MQ Details -- The following values changes dynamically
		String FACTORY_INTIAL_NAME="java.naming.factory.initial";
		String FACTORY_INTITAL_VALUE = "org.apache.activemq.jndi.ActiveMQInitialContextFactory";
		String PROVIDER_URL_NAME="java.naming.provider.url";
		String PROVIDER_URL_VALUE= "tcp://127.0.0.1:61616";
		String Q_NAME="queue.queue";
		String Q_VALUE="testamq";
		
		
		
		Context jndiContext = null;
		ConnectionFactory connectionFactory = null;
		Connection connection = null;
		Session session;
		Destination destination = null;
		MessageProducer producer;
		String destinationName = "queue";
		final int numMsgs;

		System.out.println("Generic JMS Subscriber");

		try {

			Properties prop=new Properties();
			prop.put(FACTORY_INTIAL_NAME, FACTORY_INTITAL_VALUE);
			prop.put(PROVIDER_URL_NAME, PROVIDER_URL_VALUE);
			prop.put(Q_NAME, Q_VALUE);
			
			
			/* * Create a JNDI API InitialContext object */
			jndiContext = new InitialContext(prop);
			connectionFactory = (ConnectionFactory) jndiContext.lookup("ConnectionFactory");
			destination = (Destination) jndiContext.lookup(destinationName);

			/* * Create connection. Create session from connection */
			connection = connectionFactory.createConnection();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			/** Publish Test Message **/
			producer = session.createProducer(destination);
			String pubMsg="Test Message-" + new Date();
			TextMessage message = session.createTextMessage(pubMsg);
			producer.send(message);
			
			System.out.println("Message Successfully Published::"+pubMsg);
		}

		catch (Exception e) {
			System.out.println("Exception occurred: " + e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (JMSException ignored) {
				}
			}

		}
	}
}
