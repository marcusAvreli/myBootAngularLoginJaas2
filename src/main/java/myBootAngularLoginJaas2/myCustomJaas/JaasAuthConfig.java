package myBootAngularLoginJaas2.myCustomJaas;


/*-
* #%L
* thinkbig-security-auth
* %%
* Copyright (C) 2017 ThinkBig Analytics
* %%
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
* #L%
*/

import myBootAngularLoginJaas2.myCustomJaas.DefaultPrincipalAuthorityGranter;
import myBootAngularLoginJaas2.myCustomJaas.GroupPrincipalAuthorityGranter;
import myBootAngularLoginJaas2.myCustomJaas.UserPrincipalAuthorityGranter;
import myBootAngularLoginJaas2.myCustomJaas.DefaultKyloJaasAuthenticationProvider;
import myBootAngularLoginJaas2.myCustomJaas.LoginConfiguration;
import myBootAngularLoginJaas2.myCustomJaas.LoginConfigurationBuilder;
import myBootAngularLoginJaas2.myCustomJaas.UsernameJaasAuthenticationProvider;
import myBootAngularLoginJaas2.myCustomJaas.JaasHttpCallbackHandlerFilter;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.jaas.AbstractJaasAuthenticationProvider;
import org.springframework.security.authentication.jaas.AuthorityGranter;
import org.springframework.security.authentication.jaas.DefaultJaasAuthenticationProvider;
import org.springframework.security.authentication.jaas.JaasAuthenticationCallbackHandler;
import org.springframework.security.authentication.jaas.memory.InMemoryConfiguration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Named;
import javax.security.auth.login.AppConfigurationEntry;

/**
*
*/
@Configuration
public class JaasAuthConfig {

   public static final String JAAS_UI = "UI";
   public static final String JAAS_UI_TOKEN = "UI-Token";
   public static final String JAAS_SERVICES = "Services";
   public static final String JAAS_SERVICES_TOKEN = "Services-Token";

   public static final String SERVICES_AUTH_PROVIDER = "servicesAuthenticationProvider";
   public static final String SERVICES_TOKEN_AUTH_PROVIDER = "servicesTokenAuthenticationProvider";
   public static final String UI_AUTH_PROVIDER = "uiAuthenticationProvider";
   public static final String UI_TOKEN_AUTH_PROVIDER = "uiTokenAuthenticationProvider";

   public static final int DEFAULT_GRANTER_ORDER = Integer.MAX_VALUE - 100;

   private static final JaasAuthenticationCallbackHandler[] CALLBACK_HANDLERS 
       = new JaasAuthenticationCallbackHandler[] { new JaasAuthenticationNameCallbackHandler(), 
                                                   new JaasAuthenticationPasswordCallbackHandler(),
                                                   JaasHttpCallbackHandlerFilter.CALLBACK_HANDLER
                                                 };
   
   private static final Logger logger = LoggerFactory.getLogger(JaasAuthConfig.class);

   @Bean(name = UI_AUTH_PROVIDER)
   public AbstractJaasAuthenticationProvider uiAuthenticationProvider(@Named("jaasConfiguration") javax.security.auth.login.Configuration config,
                                                                      List<AuthorityGranter> authorityGranters) {
	   logger.info("[uiAuthenticationProvider] STARTED");
	   logger.info("[uiAuthenticationProvider] authorityGranters ==> "+authorityGranters);
       DefaultJaasAuthenticationProvider provider = new DefaultKyloJaasAuthenticationProvider();
       logger.info("[uiAuthenticationProvider]: 2");       
       provider.setCallbackHandlers(CALLBACK_HANDLERS);
       logger.info("[uiAuthenticationProvider]: 3");
       provider.setConfiguration(config);
       logger.info("[uiAuthenticationProvider]: 4");
       provider.setAuthorityGranters(authorityGranters.toArray(new AuthorityGranter[authorityGranters.size()]));
       logger.info("[uiAuthenticationProvider]: 5");
       provider.setLoginContextName(JAAS_UI);
       logger.info("[uiAuthenticationProvider] FINISHED");
       return provider;
   }

   @Bean(name = SERVICES_AUTH_PROVIDER)
   public AbstractJaasAuthenticationProvider servicesAuthenticationProvider(@Named("jaasConfiguration") javax.security.auth.login.Configuration config,
                                                                            List<AuthorityGranter> authorityGranters) {
	   logger.info("[servicesAuthenticationProvider] STARTED");
	   logger.info("[servicesAuthenticationProvider] authorityGranters ==> "+authorityGranters);
       DefaultJaasAuthenticationProvider provider = new DefaultKyloJaasAuthenticationProvider();
       logger.info("[servicesAuthenticationProvider] 1");
       provider.setCallbackHandlers(CALLBACK_HANDLERS);
       logger.info("[servicesAuthenticationProvider] 2");
       provider.setConfiguration(config);
       logger.info("[servicesAuthenticationProvider] 3");
       provider.setAuthorityGranters(authorityGranters.toArray(new AuthorityGranter[authorityGranters.size()]));
       logger.info("[servicesAuthenticationProvider] 4");
       provider.setLoginContextName(JAAS_SERVICES);
       logger.info("[servicesAuthenticationProvider] FINISH");
       return provider;
   }

   @Bean(name = UI_TOKEN_AUTH_PROVIDER)
   public AbstractJaasAuthenticationProvider uiTokenAuthenticationProvider(@Named("jaasConfiguration") javax.security.auth.login.Configuration config,
                                                                           List<AuthorityGranter> authorityGranters) {
	   logger.info("[uiTokenAuthenticationProvider] STARTED");
	   logger.info("[uiTokenAuthenticationProvider] authorityGranters ==> "+authorityGranters);
       UsernameJaasAuthenticationProvider provider = new UsernameJaasAuthenticationProvider();
       logger.info("[uiTokenAuthenticationProvider] 1");
       provider.setCallbackHandlers(CALLBACK_HANDLERS);
       logger.info("[uiTokenAuthenticationProvider] 2");
       provider.setConfiguration(config);
       logger.info("[uiTokenAuthenticationProvider] 3");
       provider.setAuthorityGranters(authorityGranters.toArray(new AuthorityGranter[authorityGranters.size()]));
       logger.info("[uiTokenAuthenticationProvider] 4");
       provider.setLoginContextName(JAAS_UI_TOKEN);
       logger.info("[uiTokenAuthenticationProvider] FINISHED");
       return provider;
   }

   @Bean(name = SERVICES_TOKEN_AUTH_PROVIDER)
   public AbstractJaasAuthenticationProvider servicesTokenAuthenticationProvider(@Named("jaasConfiguration") javax.security.auth.login.Configuration config,
                                                                                 List<AuthorityGranter> authorityGranters) {
	   logger.info("[servicesTokenAuthenticationProvider] STARTED");
	   logger.info("[servicesTokenAuthenticationProvider] authorityGranters ==> "+authorityGranters);
       UsernameJaasAuthenticationProvider provider = new UsernameJaasAuthenticationProvider();
       logger.info("[servicesTokenAuthenticationProvider] 1");
       provider.setCallbackHandlers(CALLBACK_HANDLERS);
       logger.info("[servicesTokenAuthenticationProvider] 2");
       provider.setConfiguration(config);
       logger.info("[servicesTokenAuthenticationProvider] 3");
       provider.setAuthorityGranters(authorityGranters.toArray(new AuthorityGranter[authorityGranters.size()]));
       logger.info("[servicesTokenAuthenticationProvider] 4");
       provider.setLoginContextName(JAAS_SERVICES_TOKEN);
       logger.info("[servicesTokenAuthenticationProvider] FINISHED");
       return provider;
   }

   @Bean(name = "jaasConfiguration")
   public javax.security.auth.login.Configuration jaasConfiguration(Optional<List<LoginConfiguration>> loginModuleEntries) {
	   logger.info("[jaasConfiguration] STARTED");
       // Generally the entries will be null only in situations like unit/integration tests.
       if (loginModuleEntries.isPresent()) {
    	   logger.info("[jaasConfiguration] 1");
           List<LoginConfiguration> sorted = new ArrayList<>(loginModuleEntries.get());
           logger.info("[jaasConfiguration] 2");
           sorted.sort(new AnnotationAwareOrderComparator());
           logger.info("[jaasConfiguration] 4");
           Map<String, AppConfigurationEntry[]> merged = sorted.stream()
                           .map(c -> c.getAllApplicationEntries().entrySet())
                           .flatMap(s -> s.stream())
                           .collect(Collectors.toMap(e -> e.getKey(),
                                                     e -> e.getValue(),
                                                     ArrayUtils::addAll));
           logger.info("[jaasConfiguration] mergedMap ==> "+loginModuleEntries);
           logger.info("[jaasConfiguration] FINISHED 1");
           return new InMemoryConfiguration(merged);
       } else {
    	   logger.info("[jaasConfiguration] FINISHED 2");
           return new InMemoryConfiguration(Collections.emptyMap());
       }
   }

   @Bean(name = "groupPrincipalAuthorityGranter")
   @Order(DEFAULT_GRANTER_ORDER - 100)
   public AuthorityGranter groupPrincipalAuthorityGranter() {
	   logger.info("groupPrincipalAuthorityGranter ==> 6");
       return new GroupPrincipalAuthorityGranter();
   }

   @Bean(name = "userPrincipalAuthorityGranter")
   @Order(DEFAULT_GRANTER_ORDER - 100)
   public AuthorityGranter userPrincipalAuthorityGranter() {
	   logger.info("userPrincipalAuthorityGranter ==> 7");
       return new UserPrincipalAuthorityGranter();
   }

   @Bean(name = "defaultAuthorityGranter")
   @Order(DEFAULT_GRANTER_ORDER)
   public AuthorityGranter defaultAuthorityGranter() {
	   logger.info("defaultAuthorityGranter ==> 8");
       return new DefaultPrincipalAuthorityGranter();
   }

   @Bean
   @Scope("prototype")
   public LoginConfigurationBuilder loginConfigurationBuilder() {
	   logger.info("loginConfigurationBuilder ==> 9");
       return new DefaultLoginConfigurationBuilder();
   }
}
