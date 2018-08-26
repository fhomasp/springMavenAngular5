package be.peeterst.tester;

import be.peeterst.tester.servlet.LoggableDispatcherServlet;
import org.apache.tomcat.util.descriptor.web.LoginConfig;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.KeycloakDeploymentBuilder;
import org.keycloak.adapters.spi.HttpFacade;
import org.keycloak.adapters.tomcat.KeycloakAuthenticatorValve;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.DispatcherServlet;

import java.io.InputStream;

@SpringBootApplication
public class TesterApplication {


    @Bean
	public ServletRegistrationBean dispatcherRegistration() {
		return new ServletRegistrationBean(dispatcherServlet());
	}

	@Bean(name = DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_BEAN_NAME)
	public DispatcherServlet dispatcherServlet() {
		return new LoggableDispatcherServlet();
	}



	public static void main(String[] args) {
		SpringApplication.run(TesterApplication.class, args);
	}

	@Bean
	public EmbeddedServletContainerCustomizer getKeycloakContainerCustomizer() {
		return configurableEmbeddedServletContainer -> {
            if (configurableEmbeddedServletContainer instanceof TomcatEmbeddedServletContainerFactory) {
                TomcatEmbeddedServletContainerFactory container = (TomcatEmbeddedServletContainerFactory) configurableEmbeddedServletContainer;

                container.addContextValves(new KeycloakAuthenticatorValve());

                container.addContextCustomizers(getKeycloakContextCustomizer());
            }
        };
	}

    @Bean
    public TomcatContextCustomizer getKeycloakContextCustomizer() {
        return context -> {
            LoginConfig loginConfig = new LoginConfig();
            loginConfig.setAuthMethod("KEYCLOAK");
            context.setLoginConfig(loginConfig);

            context.addSecurityRole("ADMIN");
            SecurityConstraint adminConstraint = new SecurityConstraint();
            adminConstraint.addAuthRole("ADMIN");
            SecurityCollection adminCollection = new SecurityCollection();
            adminCollection.addPattern("/overview/write");
            adminCollection.addPattern("/overview/write/*");
            adminConstraint.addCollection(adminCollection);
            context.addConstraint(adminConstraint);

            context.addSecurityRole("USER");
            SecurityConstraint userConstraint = new SecurityConstraint();
            userConstraint.addAuthRole("USER");
            SecurityCollection userCollection = new SecurityCollection();
            userCollection.addPattern("/overview");
            userConstraint.addCollection(userCollection);
            context.addConstraint(userConstraint);

            context.addParameter("keycloak.config.resolver", SpringBootKeycloakConfigResolver.class.getName());
        };
    }

    public static class SpringBootKeycloakConfigResolver implements KeycloakConfigResolver {

        private KeycloakDeployment keycloakDeployment;

        @Override
        public KeycloakDeployment resolve(HttpFacade.Request request) {
            if (keycloakDeployment != null) {
                return keycloakDeployment;
            }

            InputStream configInputStream = getClass().getResourceAsStream("/keycloak.json");
            if (configInputStream == null) {
                keycloakDeployment = new KeycloakDeployment();
            } else {
                keycloakDeployment = KeycloakDeploymentBuilder.build(configInputStream);
            }

            return keycloakDeployment;
        }
    }

}
