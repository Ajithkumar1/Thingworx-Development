package infra;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thingworx.communications.client.ClientConfigurator;
import com.thingworx.communications.client.ConnectedThingClient;
import com.thingworx.relationships.RelationshipTypes.ThingworxEntityTypes;
//import com.thingworx.sdk.simplething.SimpleThing;
//import com.thingworx.sdk.simplething.SimpleThingClient;
import com.thingworx.types.InfoTable;
import com.thingworx.types.collections.ValueCollection;
import com.thingworx.types.primitives.IntegerPrimitive;
import com.thingworx.types.primitives.LocationPrimitive;
import com.thingworx.types.primitives.StringPrimitive;

public class ndviClient  extends ConnectedThingClient {

	/**
	 * @param args
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ndviClient.class);

	public ndviClient(ClientConfigurator config) throws Exception {
		super(config);
	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ClientConfigurator config = new ClientConfigurator();

		// Set the URI of the server that we are going to connect to.
		// You must include the port number in the URI.
		config.setUri("ws://52.74.108.222:80//Thingworx/WS");

		// Set the Application Key. This will allow the client to authenticate with the server.
		// It will also dictate what the client is authorized to do once connected.
		config.setAppKey("a2c66fb7-273c-463b-9794-9696cf692dea");

		// This will allow us to test against a server using a self-signed certificate.
		// This should be removed for production systems.
		config.ignoreSSLErrors(true); // All self signed certs
		try {
			// Create our client.
			ndviClient client = new ndviClient(config);

			// Start the client. The client will connect to the server and authenticate
			// using the ApplicationKey specified above.
			client.start();

			// Lets wait to get connected
			LOG.debug("****************Connecting to ThingWorx Server****************");

			while(!client.getEndpoint().isConnected()) {
				Thread.sleep(5000);
			}

			LOG.debug("****************Connected to ThingWorx Server****************");

			// Wait for the client to connect.
			if (client.waitForConnection(30000)) {
				LOG.info("The client is now connected.");
				// Reading a property. Not using binding
				///////////////////////////////////////////////////////////////

				// Request a property from a Thing on the Platform. Here we access the 'count'
				// property of a Thing while NOT using binding.
				//InfoTable result = client.readProperty(ThingworxEntityTypes.Things, ThingName, property, 10000);

				// Invoking a service on a Thing
				///////////////////////////////////////////////////////////////


				// Firing an event
				///////////////////////////////////////////////////////////////

				// Create a VirtualThing and bind it to the client
				///////////////////////////////////////////////////////////////

				// Create a new VirtualThing. The name parameter should correspond with the 
				// name of a RemoteThing on the Platform.
//				ndvi thing = new ndvi("ndvi", "ndvi thing", client);
//
//				// Bind the VirtualThing to the client. This will tell the Platform that
//				// the RemoteThing 'SimpleThing_2' is now connected and that it is ready to 
//				// receive requests.
//				client.bindThing(thing);
			} else {
				// Log this as a warning. In production the application could continue
				// to execute, and the client would attempt to reconnect periodically.
				LOG.warn("Client did not connect within 30 seconds. Exiting");
			}

			client.shutdown();
		} catch (Exception e) {
			LOG.error("An exception occured while initializing the client", e);
		}

		LOG.info("ndvi client is done. Exiting");
	}

}
