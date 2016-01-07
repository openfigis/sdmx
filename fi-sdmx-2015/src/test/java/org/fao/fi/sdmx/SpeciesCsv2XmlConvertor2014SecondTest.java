package org.fao.fi.sdmx;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

public class SpeciesCsv2XmlConvertor2014SecondTest {

	SpeciesCsv2XmlConvertor2014Second c = new SpeciesCsv2XmlConvertor2014Second();

	@Test
	public void testProcess() {
		c.process();
		File xmlFile = new File(SpeciesCsv2XmlConvertor2014Second.xmlFileName);
		assertTrue(xmlFile.exists());
	}
}
