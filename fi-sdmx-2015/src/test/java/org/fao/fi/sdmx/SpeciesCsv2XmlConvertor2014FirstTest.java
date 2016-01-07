package org.fao.fi.sdmx;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

public class SpeciesCsv2XmlConvertor2014FirstTest {

	SpeciesCsv2XmlConvertor2014First c = new SpeciesCsv2XmlConvertor2014First();

	@Test
	public void testProcess() {
		c.process();
		File xmlFile = new File(SpeciesCsv2XmlConvertor2014First.xmlFileName);
		assertTrue(xmlFile.exists());
	}

}
