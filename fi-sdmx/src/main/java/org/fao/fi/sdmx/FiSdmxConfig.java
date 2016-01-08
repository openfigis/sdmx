package org.fao.fi.sdmx;

import org.sdmxsource.sdmx.api.factory.ReadableDataLocationFactory;
import org.sdmxsource.util.factory.SdmxSourceReadableDataLocationFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;

@Configuration
@EnableSpringConfigured
@ComponentScan({ "org.sdmxsource" })
public class FiSdmxConfig {

	@Bean
	public SpeciesCsv2XmlConvertor2016 speciesCsv2XmlConvertor2016() {
		return new SpeciesCsv2XmlConvertor2016();
	}

	@Bean
	public ReadableDataLocationFactory readableDataLocationFactory() {
		return new SdmxSourceReadableDataLocationFactory();
	}

	// <bean class="org.sdmxsource.util.factory.SdmxSourceReadableDataLocationFactory" />
	// <bean class="org.sdmxsource.util.factory.SdmxSourceWriteableDataLocationFactory" />

}
