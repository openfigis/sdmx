package org.fao.fi.sdmx;

import java.io.File;
import java.io.FileReader;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.commons.lang.StringUtils;
import org.opensdmx.controller.publisher.SdmxNamespacePrefixMapper;
import org.sdmx.resources.sdmxml.schemas.v2_0.common.TextType;
import org.sdmx.resources.sdmxml.schemas.v2_0.message.Header;
import org.sdmx.resources.sdmxml.schemas.v2_0.message.PartyType;
import org.sdmx.resources.sdmxml.schemas.v2_0.message.Structure;
import org.sdmx.resources.sdmxml.schemas.v2_0.structure.CodeListType;
import org.sdmx.resources.sdmxml.schemas.v2_0.structure.CodeListsType;
import org.sdmx.resources.sdmxml.schemas.v2_0.structure.CodeType;

import au.com.bytecode.opencsv.CSVReader;

public class SpeciesCsv2XmlConvertor {

	public static String csvFileName = "src/main/resources/CL_SPECIES_1_2.csv";
	public static String xmlFileName = "src/main/resources/CL_SPECIES_1_2.xml";

	public static String NAME = "CL_SPECIES";

	// ALPHA3CODE NAME_A NAME_C NAME_E NAME_F NAME_R NAME_S AUTHOR

	void process() {

		try {

			CSVReader reader = new CSVReader(new FileReader(csvFileName));
			reader.readNext();
			String[] nextLine;
			CodeListType cl = new CodeListType();
			while ((nextLine = reader.readNext()) != null) {
				CodeType code = new CodeType();
				String codeValue = nextLine[0];
				if (!StringUtils.isBlank(codeValue)) {
					code.setValue(codeValue);
					add2Codes(code.getDescriptions(), "ar", nextLine[1]);
					add2Codes(code.getDescriptions(), "ca", nextLine[2]);
					add2Codes(code.getDescriptions(), "en", nextLine[3]);
					add2Codes(code.getDescriptions(), "fr", nextLine[4]);
					add2Codes(code.getDescriptions(), "ru", nextLine[5]);
					add2Codes(code.getDescriptions(), "es", nextLine[6]);
					add2Codes(code.getDescriptions(), "la", nextLine[6]);
					if (code.getDescriptions().size() == 0) {
						add2Codes(code.getDescriptions(), "la", "UNDEFINED");
					}
					cl.getCodes().add(code);
				}
			}
			reader.close();

			// set up JAXB marshalling context
			JAXBContext context = JAXBContext.newInstance(Structure.class);
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", new SdmxNamespacePrefixMapper());

			Structure s = new Structure();
			doHeader(s, cl);
			s.setCodeLists(new CodeListsType());
			s.getCodeLists().getCodeLists().add(cl);

			marshaller.marshal(s, new File(xmlFileName));

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	private void doHeader(Structure s, CodeListType cl) {
		// <message:Header>
		// <message:ID>REGISTRY_RESPONSE</message:ID>
		// <message:Test>false</message:Test>
		// <message:Prepared>2014-03-14T14:56:08</message:Prepared>
		// <message:Sender id="FAO"/>
		// </message:Header>
		// <message:CodeLists>
		// <structure:CodeList id="CL_SPECIES" agencyID="FAO" version="1.1"
		// isFinal="true">
		// <structure:Name xml:lang="en">CL_SPECIES</structure:Name>

		Header h = new Header();
		h.setPrepared("2014-03-14T14:56:08");
		h.setTest(false);
		PartyType p = new PartyType();
		p.setId("FAO");
		h.getSenders().add(p);

		h.setID("FAO_CL_SPECIES_1_2");

		s.setHeader(h);

		cl.setId(NAME);
		cl.setAgencyID("FAO");
		cl.setVersion("1.2");
		TextType name = new TextType();
		name.setLang("en");
		name.setValue(NAME);

		cl.getNames().add(name);

	}

	private void add2Codes(List<TextType> descriptions, String lang, String v) {

		if (!StringUtils.isBlank(v)) {
			TextType t = new TextType();
			t.setLang(lang);
			t.setValue(v);
			descriptions.add(t);
		}
	}
}
