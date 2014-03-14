package org.fao.fi.sdmx;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.opensdmx.controller.publisher.SdmxNamespacePrefixMapper;
import org.sdmx.resources.sdmxml.schemas.v2_0.message.Structure;

public class SpeciesCsv2XmlConvertor {

	public static String csvFileName = "src/main/resources/CL_SPECIES.csv";
	public static String xmlFileName = "src/main/resources/CL_SPECIES.xml";

	void process() {

		try {
			// set up JAXB marshalling context
			JAXBContext context = JAXBContext.newInstance(Structure.class);
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", new SdmxNamespacePrefixMapper());

			Structure s = new Structure();

			marshaller.marshal(s, new File(xmlFileName));

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

}
