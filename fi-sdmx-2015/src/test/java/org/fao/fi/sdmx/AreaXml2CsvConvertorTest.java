package org.fao.fi.sdmx;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

public class AreaXml2CsvConvertorTest {

	AreaXml2CsvConvertor c = new AreaXml2CsvConvertor();

	@Test
	public void testProcess() {
		File file = new File(AreaXml2CsvConvertor.csvFileName);
		c.process();
		assertTrue(file.exists());

	}

}
