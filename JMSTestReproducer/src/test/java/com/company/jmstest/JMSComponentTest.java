package com.company.jmstest;

import java.io.File;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.PollingConsumer;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:/META-INF/spring/camel-context.xml" })
public class JMSComponentTest extends CamelTestSupport {
	private static final Logger LOGGER = LoggerFactory.getLogger(JMSComponentTest.class);

	@Autowired
	private CamelContext context;

	@Produce(uri = "jms:queue:inputQueue")
	protected ProducerTemplate template;

	@Test
	public void testJMSRouteSuccess() throws Exception {

		template.sendBody("Test JMS message");
		Endpoint outEndpoint = context.getEndpoint("jms:queue:outputQueue");
		PollingConsumer consumer = outEndpoint.createPollingConsumer();
		Exchange outExchange = consumer.receive(10000);
		String actualMessage = outExchange.getIn().getBody(String.class);
		assertEquals("Test JMS message", actualMessage);
	}

}
