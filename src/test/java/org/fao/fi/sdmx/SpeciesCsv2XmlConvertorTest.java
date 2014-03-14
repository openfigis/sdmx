package org.fao.fi.sdmx;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

public class SpeciesCsv2XmlConvertorTest {

	SpeciesCsv2XmlConvertor c = new SpeciesCsv2XmlConvertor();

	@Test
	public void testProcess() {
		c.process();
		File xmlFile = new File(SpeciesCsv2XmlConvertor.xmlFileName);
		assertTrue(xmlFile.exists());
	}

}
