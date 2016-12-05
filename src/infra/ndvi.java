package infra;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thingworx.communications.client.ConnectedThingClient;
import com.thingworx.communications.client.things.VirtualThing;
import com.thingworx.metadata.EventDefinition;
import com.thingworx.metadata.FieldDefinition;
import com.thingworx.metadata.PropertyDefinition;
import com.thingworx.metadata.ServiceDefinition;
import com.thingworx.metadata.annotations.ThingworxBaseTemplateDefinition;
import com.thingworx.metadata.annotations.ThingworxPropertyDefinition;
import com.thingworx.metadata.annotations.ThingworxPropertyDefinitions;
import com.thingworx.metadata.annotations.ThingworxServiceDefinition;
import com.thingworx.metadata.annotations.ThingworxServiceParameter;
import com.thingworx.metadata.annotations.ThingworxServiceResult;
import com.thingworx.metadata.collections.FieldDefinitionCollection;
//import com.thingworx.sdk.simplething.SimpleThing;
import com.thingworx.types.BaseTypes;
import com.thingworx.types.InfoTable;
import com.thingworx.types.collections.AspectCollection;
import com.thingworx.types.collections.ValueCollection;
import com.thingworx.types.constants.Aspects;
import com.thingworx.types.constants.CommonPropertyNames;
import com.thingworx.types.constants.DataChangeType;
import com.thingworx.types.primitives.BooleanPrimitive;
import com.thingworx.types.primitives.IntegerPrimitive;
import com.thingworx.types.primitives.LocationPrimitive;
import com.thingworx.types.primitives.NumberPrimitive;
import com.thingworx.types.primitives.StringPrimitive;
import com.thingworx.types.primitives.structs.Location;
import com.thingworx.types.primitives.ImagePrimitive;

@SuppressWarnings("serial")
@ThingworxBaseTemplateDefinition(name = "ndvi")
@ThingworxPropertyDefinitions(properties = {
		@ThingworxPropertyDefinition(name="img", description="Image", baseType="IMAGE")
})


public class ndvi extends VirtualThing{
	
	private static final Logger LOG = LoggerFactory.getLogger(ndvi.class);
	private String service = "ndvi_java";
	
	//private Image img;
	

	/**
	 * @param args
	 */
	
	
	public ndvi(String name, String description, ConnectedThingClient client)
	{
		super(name, description, client);
		this.initializeFromAnnotations();
		
		

		//Create the service definition with name and description
		ServiceDefinition ndvi_java = new ServiceDefinition(service, "Description for ndvi_java");
		//Create the input parameter to string parameter 'name'
		FieldDefinitionCollection fields = new FieldDefinitionCollection();
        fields.addFieldDefinition(new FieldDefinition("img", BaseTypes.IMAGE));
        ndvi_java.setParameters(fields);
        //Set remote access
        ndvi_java.setLocalOnly(false);
		//Set return type
        ndvi_java.setResultType(new FieldDefinition(CommonPropertyNames.PROP_RESULT, BaseTypes.IMAGE));
        //Add the service definition to the Virtual Thing
        this.defineService(ndvi_java);
        
        
        FieldDefinitionCollection infoFields = new FieldDefinitionCollection();
        infoFields.addFieldDefinition(new FieldDefinition("img", BaseTypes.IMAGE));
        // Add the DataShapeDefinition to the VirtualThing
        this.defineDataShapeDefinition("ImageMap", infoFields);

	}
	@ThingworxServiceDefinition(name="ndvi_java", description="Example string service.")
	@ThingworxServiceResult(name=CommonPropertyNames.PROP_RESULT, description="Result", baseType="IMAGE")
	public ImagePrimitive Service1(ImagePrimitive img) throws Exception {
		
		BufferedImage map = null;
		byte[] img_byte = img.getValue();
		InputStream in = new ByteArrayInputStream(img_byte);
        map = ImageIO.read(in);
        byte[] imageInByte;
        ImagePrimitive result2 = null;
		
	
		
        int[][] mapArray = new int[map.getWidth()][map.getHeight()];
		

		for (int y = 0; y < map.getHeight(); y++)
		{
			for (int x = 0; x < map.getWidth(); x++)
			{
		
		    Color c = new Color(map.getRGB(x, y));
		    float[] basec = c.getRGBColorComponents(null);
		    //int red = c.getRed();
		    //int green = c.getGreen();
		    //int blue = c.getBlue();
		    
		    float red = basec[0];
		    float green = basec[1];
		    float blue = basec[2];
		    
		    
		    float num = red-blue;
		    float denom = red+blue;
		    
		    float ndvi;
		//    ndvi = basec[0];


            if (denom != 0.0F) {
                ndvi = num / denom;
            } else {
                ndvi = 0.0F;
            }

            if (ndvi < 0.0F) {
                ndvi = 0.0F;
            } else if (ndvi > 1.0F) {
                ndvi = 1.0F;
            }

		    
		    int ndvibyte = (int) (255*ndvi);
		    map.setRGB(x, y, ndvibyte);
//		    mapArray[x][y] = ndvibyte;	
//		    try {
//				outputStream.writeByte(ndvibyte);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		    
		    

			}
		}
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(map, "jpg", baos);
		baos.flush();
		imageInByte = baos.toByteArray();
		baos.close();
		
		result2.setValue(imageInByte);
		ImagePrimitive result = result2;
		return result;
	}


}
