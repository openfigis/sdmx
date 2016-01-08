package org.fao.fi.sdmx;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = FiSdmxConfig.class, loader = AnnotationConfigContextLoader.class)
public class SpeciesCsv2XmlConvertor2016Test {

	@Autowired
	private SpeciesCsv2XmlConvertor2016 c;

	@Test
	public void testBuildCountryCodelist() {
		assertNotNull(c);
		c.buildCountryCodelist();
	}

}
