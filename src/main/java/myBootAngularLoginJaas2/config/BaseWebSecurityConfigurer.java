
package myBootAngularLoginJaas2.config;

/*-
 * #%L
 * thinkbig-ui-app
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
/*
import myBootAngularLoginJaas.kyloAuth.config.MultiHandlerLogoutFilter;
import myBootAngularLoginJaas.kyloAuth.jaas.config.JaasAuthConfig;
*/

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.jaasapi.JaasApiIntegrationFilter;



/**
 * Abstract base HTTP authentication configurer for Spring Security.
 */
public abstract class BaseWebSecurityConfigurer extends WebSecurityConfigurerAdapter {

	public static final String UI_LOGOUT_URL = "/logout";


	public static final String UI_LOGOUT_REDIRECT_URL = "/login.html?logout";


	@Autowired
	protected AuthenticationProvider uiAuthenticationProvider;

	@Bean
	public JaasApiIntegrationFilter jaasFilter() {

		JaasApiIntegrationFilter filter = new JaasApiIntegrationFilter();
		filter.setCreateEmptySubject(true);
		return filter;
	}
}
