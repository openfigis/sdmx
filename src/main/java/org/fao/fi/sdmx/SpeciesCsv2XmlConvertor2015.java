package org.fao.fi.sdmx;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
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

/**
 * 
 * ftp://ftp.fao.org/fi/STAT/CodeList/
 * 
 * 
 * TODO, make this work. I started this one because of the email below. Then Friderike and I spoke on the telephone and
 * it we clarified that this one is needed for next year.
 * 
 * 
 * 
 * Thanks Erik, however it is not an issue of a wrong link, but the list does not seem to contain the 2014 AFSIS
 * updates. I will give you a call at 3.
 * 
 * From: VanIngen, Erik (FIPS) [mailto:Erik.VanIngen@fao.org] Sent: Monday, May 19, 2014 1:53 PM To: OEHLER Friderike
 * (ESTAT) Subject: RE: SDMX codelist
 * 
 * They have been moved here last week: ftp://ftp.fao.org/fi/STAT/CodeList/2014/
 * 
 * I am available from 15:00 – 16:00 to discuss more.
 * 
 * 
 * 
 * 
 * From: Friderike.OEHLER@ec.europa.eu [mailto:Friderike.OEHLER@ec.europa.eu] Sent: 19 May 2014 11:24 To: VanIngen, Erik
 * (FIPS) Subject: RE: SDMX codelist
 * 
 * All right. I would need to know how long a possible update will take. Countries are getting impatient to receive the
 * templates… Thanks a lot, Friderike
 * 
 * 
 * 
 * 
 * 
 * 
 * @author Erik van Ingen
 * 
 */
public class SpeciesCsv2XmlConvertor2015 {

	/**
	 * CL_SPECIES_1_4.csv
	 */
	public static String csvFileName = "src/main/resources/CL_SPECIES_1_4.csv";
	public static String xmlFileName = "src/main/resources/CL_SPECIES_1_4.xml";

	public static String NAME = "CL_SPECIES";

	/**
	 * 
	 * 
	 * 0 ISSCAAP
	 * 
	 * 1 TAXOCODE
	 * 
	 * 2 3A_CODE
	 * 
	 * 3 Scientific_name
	 * 
	 * 4 English_name
	 * 
	 * 5 French_name
	 * 
	 * *6 Spanish_name
	 * 
	 * 7 Author
	 * 
	 * 8 Family
	 * 
	 * 9 Order
	 * 
	 * 10 Stats_data
	 */

	void process() {

		try {

			CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(csvFileName), "Windows-1252"),
					'\t');
			reader.readNext();
			String[] nextLine;
			CodeListType cl = new CodeListType();
			while ((nextLine = reader.readNext()) != null) {
				CodeType code = new CodeType();
				String codeValue = nextLine[2];
				if (!StringUtils.isBlank(codeValue)) {
					code.setValue(codeValue);
					// add2Codes(code.getDescriptions(), "ar", nextLine[1]);
					// add2Codes(code.getDescriptions(), "ca", nextLine[2]);
					add2Codes(code.getDescriptions(), "en", nextLine[4]);

					add2Codes(code.getDescriptions(), "fr", nextLine[5]);
					System.out.println(nextLine[5]);

					// add2Codes(code.getDescriptions(), "ru", nextLine[3]);
					add2Codes(code.getDescriptions(), "es", nextLine[6]);
					add2Codes(code.getDescriptions(), "la", nextLine[3]);
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
		h.setPrepared("2015-05-11T14:56:08");
		h.setTest(false);
		PartyType p = new PartyType();
		p.setId("FAO");
		h.getSenders().add(p);

		h.setID("FAO_CL_SPECIES_1_4");

		s.setHeader(h);

		cl.setId(NAME);
		cl.setAgencyID("FAO");
		cl.setVersion("1.4");
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
