package org.fao.fi.sdmx;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.sdmx.resources.sdmxml.schemas.v2_0.common.TextType;
import org.sdmx.resources.sdmxml.schemas.v2_0.message.Structure;
import org.sdmx.resources.sdmxml.schemas.v2_0.structure.CodeListType;
import org.sdmx.resources.sdmxml.schemas.v2_0.structure.CodeType;

import au.com.bytecode.opencsv.CSVWriter;

public class AreaXml2CsvConvertor {

	public static final String xmlFileName = "src/main/resources/CL_PRODUCTION_AREA_1_1.xml";
	public static final String csvFileName = "src/main/resources/CL_PRODUCTION_AREA_1_1.csv";

	void process() {
		try {
			JAXBContext context = JAXBContext.newInstance(Structure.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			File xmlFile = new File(xmlFileName);
			Structure structure = (Structure) unmarshaller.unmarshal(xmlFile);

			CodeListType cl = structure.getCodeLists().getCodeLists().get(0);
			List<CodeType> codes = cl.getCodes();

			File csvFile = new File(csvFileName);
			csvFile.createNewFile();

			CSVWriter writer = new CSVWriter(new FileWriter(csvFileName), ',');

			String[] titles = { "AREA", "en", "es", "fr", "ORIGINAL_CODE", "PARENT" };
			writer.writeNext(titles);

			// feed in your array (or convert your data to an array)
			for (CodeType c : codes) {
				List<TextType> ds = c.getDescriptions();
				String[] langs = generateLangs(ds);
				String originalCode = "";
				if (c.getAnnotations() != null) {
					originalCode = c.getAnnotations().getAnnotations().get(0).getAnnotationTexts().get(0).getValue();
				}

				String[] entries = { c.getValue(), langs[0], langs[1], langs[2], originalCode, c.getParentCode() };
				writer.writeNext(entries);
			}
			writer.close();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	private String[] generateLangs(List<TextType> ds) {
		Map<String, String> dMap = new HashMap<String, String>();
		for (TextType t : ds) {
			dMap.put(t.getLang(), t.getValue());
		}
		String langs[] = { "en", "es", "fr" };
		for (int i = 0; i < langs.length; i++) {
			String value = dMap.get(langs[i]);
			if (value == null) {
				langs[i] = "";
			} else {
				langs[i] = value;
			}

		}
		return langs;
	}
}
