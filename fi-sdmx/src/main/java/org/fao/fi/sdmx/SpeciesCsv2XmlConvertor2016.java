package org.fao.fi.sdmx;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import org.sdmxsource.sdmx.api.constants.SDMX_STRUCTURE_TYPE;
import org.sdmxsource.sdmx.api.constants.STRUCTURE_OUTPUT_FORMAT;
import org.sdmxsource.sdmx.api.manager.output.StructureWriterManager;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.sdmxsource.sdmx.api.model.format.StructureFormat;
import org.sdmxsource.sdmx.api.model.mutable.base.AnnotationMutableBean;
import org.sdmxsource.sdmx.api.model.mutable.codelist.CodeMutableBean;
import org.sdmxsource.sdmx.api.model.mutable.codelist.CodelistMutableBean;
import org.sdmxsource.sdmx.sdmxbeans.model.SdmxStructureFormat;
import org.sdmxsource.sdmx.sdmxbeans.model.header.HeaderBeanImpl;
import org.sdmxsource.sdmx.sdmxbeans.model.mutable.base.AnnotationMutableBeanImpl;
import org.sdmxsource.sdmx.sdmxbeans.model.mutable.codelist.CodelistMutableBeanImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.opencsv.CSVReader;

/**
 * 
 * @author Erik van Ingen
 * 
 */
public class SpeciesCsv2XmlConvertor2016 {

	public static final String ID = "CL_SPECIES";
	public static final String SENDER_ID = "FAO";
	public static final String CL_FILE_NAME = "target/CL_SPECIES.xml";
	public static final String CSV_FILE_NAME = "src/main/resources/2016/csv/SPECIES_CL.csv";

	@Autowired
	private StructureWriterManager structureWritingManager;

	/**
	 * 0 FIGIS_ID
	 *
	 * 1 ISCAAP_ID
	 *
	 * 2 NAME_en
	 *
	 * 3 NAME_fr
	 *
	 * 4 NAME_es
	 *
	 * 5 NAME_ar
	 *
	 * 6 NAME_cn
	 *
	 * 7 NAME_ru
	 *
	 * 8 Unit
	 *
	 * 9 Scientific.Name
	 *
	 * 10 Alpha3.Code
	 *
	 * 11 FAOMAP_LAYER_ID
	 *
	 * 12 Author
	 *
	 *
	 * 
	 */
	public void buildCountryCodelist() {
		CodelistBean cl = createCodelist();
		write(cl);
	}

	/**
	 * Using some practices performed here http://data.fao.org/sdmx/registry/codelist/FAO/CL_SPECIES/1.0
	 * 
	 * @return
	 */
	private CodelistBean createCodelist() {
		CodelistMutableBean clm = new CodelistMutableBeanImpl(SDMX_STRUCTURE_TYPE.CODE_LIST);
		clm.setAgencyId("FAO");
		clm.setId("CL_SPECIES");

		// see also http://figisapps.fao.org/FIGISwiki/index.php/Eurostat
		// Should the global version follow up on the CL_SPECIES of Eurostat?
		clm.setVersion("1.5");
		clm.addName("en", "Species");

		try {

			CSVReader reader = new CSVReader(new FileReader(CSV_FILE_NAME));

			// skip header
			reader.readNext();

			// read data
			String[] nextLine;
			while ((nextLine = reader.readNext()) != null) {
				// 0 FIGIS_ID // 1 ISCAAP_ID // 2 NAME_en // 3 NAME_fr // 4 NAME_es // 5 NAME_ar // 6 NAME_cn // 7
				// NAME_ru // 8 Unit // 9 Scientific.Name // 10 Alpha3.Code // 11 FAOMAP_LAYER_ID // 12 Author

				// first code is default english
				String english = nextLine[2];
				if (StringUtils.isEmpty(english)) {
					english = "ERROR";
				}
				System.out.println(nextLine[10]);
				CodeMutableBean cmb = clm.createItem(nextLine[10], english);

				// add descriptions in the FAO languages
				cmb.addDescription("fr", nextLine[3]);
				cmb.addDescription("es", nextLine[4]);
				cmb.addDescription("ar", nextLine[5]);
				cmb.addDescription("zh", nextLine[6]);
				cmb.addDescription("ru", nextLine[7]);

				// add the scientific name as the Latin language.
				cmb.addDescription("la", nextLine[7]);

				// add some annotations
				cmb.addAnnotation(generateAnnotation(nextLine[1], "ISCAAP_ID"));
				cmb.addAnnotation(generateAnnotation(nextLine[12], "Author"));

				clm.addItem(cmb);
			}
			reader.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return clm.getImmutableInstance();
	}

	private AnnotationMutableBean generateAnnotation(String id, String title) {
		AnnotationMutableBean a = new AnnotationMutableBeanImpl();
		a.setId(id);
		a.setTitle(title);
		return a;
	}

	private void write(CodelistBean bean) {
		try {
			FileOutputStream fos = new FileOutputStream(new File(CL_FILE_NAME));
			StructureFormat f = new SdmxStructureFormat(STRUCTURE_OUTPUT_FORMAT.SDMX_V21_STRUCTURE_DOCUMENT);
			structureWritingManager.writeStructure(bean, new HeaderBeanImpl(ID, SENDER_ID), f, fos);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

}
