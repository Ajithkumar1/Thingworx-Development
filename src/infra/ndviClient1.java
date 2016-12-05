package infra;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thingworx.communications.client.ClientConfigurator;
import com.thingworx.communications.client.ConnectedThingClient;
import com.thingworx.communications.client.things.VirtualThing;
//import com.thingworx.sdk.examples.ExampleClient;

public class ndviClient1 extends ConnectedThingClient {

	/**
	 * @param args
	 */
		private static final Logger LOG = LoggerFactory.getLogger(ndviClient.class);
		
		private static String ThingName = "ndvi";

		public ndviClient1(ClientConfigurator config) throws Exception {
			super(config);
		}

		public static void main(String[] args) {
			ClientConfigurator config = new ClientConfigurator();

			// Set the URI of the server that we are going to connect to.
			// The port is based on configurations set during tomcat setup.
			// Port 80 is the default.
			config.setUri("ws://52.74.108.222:80//Thingworx/WS");

			// Set the Application Key. This will allow the client to authenticate with the server.
			// It will also dictate what the client is authorized to do once connected.
			// This application key should match that of imported default_user User.
			config.setAppKey("a2c66fb7-273c-463b-9794-9696cf692dea");

			// This will allow us to test against a server using a self-signed certificate.
			// This should be removed for production systems.
			config.ignoreSSLErrors(true); // All self signed certs

			try {
				// Create our client that will communication with the ThingWorx composer.
				ndviClient1 client = new ndviClient1(config);

				// Create a new VirtualThing. The name parameter should correspond with the 
				// name of a RemoteThing on the Platform. In this example, the SimpleThing_1 is used.
				VirtualThing thing = new VirtualThing(ThingName, "ndvi thing", client);

				// Bind the VirtualThing to the client. This will tell the Platform that
				// the RemoteThing 'ndviClient1' is now connected and that it is ready to 
				// receive requests.
				client.bindThing(thing);

				// Start the client. The client will connect to the server and authenticate
				// using the ApplicationKey specified above.
				client.start();

				// Lets wait to get connected
				LOG.debug("****************Connecting to ThingWorx Server****************");

				while(!client.getEndpoint().isConnected()) {
					Thread.sleep(5000);
				}

				LOG.debug("****************Connected to ThingWorx Server****************");

				// This will prevent the main thread from exiting. It will be up to another thread
				// of execution to call client.shutdown(), allowing this main thread to exit.
				while (!client.isShutdown()) {
					Thread.sleep(15000);

					// Every 15 seconds we tell the thing to process a scan request. This is
					// an opportunity for the thing to query a data source, update property
					// values, and push new property values to the server.
					//
					// This loop demonstrates how to iterate over multiple VirtualThings
					// that have bound to a client. In this simple example the things
					// collection only contains one VirtualThing.
					for (VirtualThing vt : client.getThings().values()) {
						vt.processScanRequest();
					}
				}
			} catch (Exception e) {
				LOG.error("An exception occured during execution.", e);
			}

			LOG.info("ndviClient1 is done. Exiting");
		}

	

}
