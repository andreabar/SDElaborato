package util;

import java.io.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;

public class XMLFormatter {
	
	
	public static String format(String unformatted){
		//String sanitizedUnformatted = unformatted.replaceAll("&", "&amp;");
		//sanitizedUnformatted = sanitizedUnformatted.replaceAll("<", "&lt;");
		//sanitizedUnformatted = sanitizedUnformatted.replaceAll(">", "&gt;");
		//sanitizedUnformatted = sanitizedUnformatted.replaceAll("\"", "&quot;");
		//sanitizedUnformatted = sanitizedUnformatted.replaceAll("'", "&apos;");


		Source xmlInput = new StreamSource(new StringReader(unformatted));
		StreamResult xmlOutput = new StreamResult(new StringWriter());

		// Configure transformer
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
	        transformerFactory.setAttribute("indent-number", 2);
	        Transformer transformer = transformerFactory.newTransformer(); 
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			transformer.transform(xmlInput, xmlOutput);
			return xmlOutput.getWriter().toString();
		} catch (TransformerFactoryConfigurationError | TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return unformatted;
	}

}
