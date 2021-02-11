package com.sripiranavan.springboot.security.config;

import javax.naming.CompositeName;
import javax.naming.InitialContext;
import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.NamingException;
import javax.naming.spi.NamingManager;
import javax.sql.DataSource;

import org.jboss.resteasy.plugins.server.servlet.HttpServlet30Dispatcher;
import org.jboss.resteasy.plugins.server.servlet.ResteasyContextParameters;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmbeddedKeycloakConfig {
	@Bean
	ServletRegistrationBean<HttpServlet30Dispatcher> keycloakJaxRsApplication(
			KeycloakServerProperties keycloakServerProperties, DataSource dataSource) throws Exception {
		mockJndiEnvironment(dataSource);
		EmbeddedKeycloakApplication.keycloakServerProperties = keycloakServerProperties;
		ServletRegistrationBean<HttpServlet30Dispatcher> servlet = new ServletRegistrationBean<HttpServlet30Dispatcher>(
				new HttpServlet30Dispatcher());
		servlet.addInitParameter("javax.ws.rs.Application", EmbeddedKeycloakApplication.class.getName());
		servlet.addInitParameter(ResteasyContextParameters.RESTEASY_SERVLET_MAPPING_PREFIX,
				keycloakServerProperties.getContextPath());
		servlet.addInitParameter(ResteasyContextParameters.RESTEASY_USE_CONTAINER_FORM_PARAMS, "true");
		servlet.addUrlMappings(keycloakServerProperties.getContextPath() + "/*");
		servlet.setLoadOnStartup(1);
		servlet.setAsyncSupported(true);

		return servlet;
	}

	@Bean
	FilterRegistrationBean<EmbeddedKeycloakRequestFilter> keyCloakSessionManagement(
			KeycloakServerProperties keycloakServerProperties) {
		FilterRegistrationBean<EmbeddedKeycloakRequestFilter> filter = new FilterRegistrationBean<EmbeddedKeycloakRequestFilter>();
		filter.setName("Keycloak Session Management");
		filter.setFilter(new EmbeddedKeycloakRequestFilter());
		filter.addUrlPatterns(keycloakServerProperties.getContextPath() + "/*");
		return filter;
	}

	private void mockJndiEnvironment(DataSource dataSource) throws NamingException {
		NamingManager.setInitialContextFactoryBuilder((env) -> (environment) -> new InitialContext() {

			@Override
			public Object lookup(String name) throws NamingException {
				if ("spring/datasource".equals(name)) {
					return dataSource;
				}
				return null;
			}

			@Override
			public Object lookup(Name name) throws NamingException {
				return super.lookup(name.toString());
			}

			@Override
			public NameParser getNameParser(String name) throws NamingException {
				return CompositeName::new;
			}

			@Override
			public void close() throws NamingException {
				super.close();
			}

		});
	}
}
