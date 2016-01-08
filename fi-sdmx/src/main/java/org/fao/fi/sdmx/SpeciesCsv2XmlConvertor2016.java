package org.fao.fi.sdmx;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.sdmxsource.sdmx.api.constants.STRUCTURE_OUTPUT_FORMAT;
import org.sdmxsource.sdmx.api.manager.output.StructureWriterManager;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.sdmxsource.sdmx.api.model.format.StructureFormat;
import org.sdmxsource.sdmx.api.model.mutable.codelist.CodelistMutableBean;
import org.sdmxsource.sdmx.sdmxbeans.model.SdmxStructureFormat;
import org.sdmxsource.sdmx.sdmxbeans.model.header.HeaderBeanImpl;
import org.sdmxsource.sdmx.sdmxbeans.model.mutable.codelist.CodelistMutableBeanImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * @author Erik van Ingen
 * 
 */
public class SpeciesCsv2XmlConvertor2016 {

	public static final String ID = "CL_SPECIES";
	public static final String SENDER_ID = "FAO";
	public static final String CL_FILE_NAME = "target/fiets.xml";

	@Autowired
	private StructureWriterManager structureWritingManager;

	public void buildCountryCodelist() {
		CodelistBean cl = createCodelist();
		write(cl);
	}

	private CodelistBean createCodelist() {
		CodelistMutableBean clm = new CodelistMutableBeanImpl();
		clm.setAgencyId("FAO");
		clm.setId("CL_SPECIES");
		clm.setVersion("1.0");
		clm.addName("en", "Species");

		clm.createItem("UK", "United Kingdom");
		clm.createItem("FR", "France");
		clm.createItem("DE", "Germany");
		return clm.getImmutableInstance();
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
