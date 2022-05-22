package myBootAngularLoginJaas2.myCustomJaas;


import myBootAngularLoginJaas2.config.SecurityConfig;

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

import myBootAngularLoginJaas2.myCustomJaas.LoginConfiguration;
import myBootAngularLoginJaas2.myCustomJaas.LoginConfigurationBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.AppConfigurationEntry.LoginModuleControlFlag;
import javax.security.auth.spi.LoginModule;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
* Default implementation of LoginConfigurationBuilder.
*/
public class DefaultLoginConfigurationBuilder implements LoginConfigurationBuilder {

   private Integer order = null;
   private DefaultLoginConfiguration configuration = new DefaultLoginConfiguration();
   private static final Logger logger = LoggerFactory.getLogger(DefaultLoginConfigurationBuilder.class);
   public DefaultLoginConfigurationBuilder() {
   }
   
   @Override
   public LoginConfigurationBuilder order(int order) {
	   logger.info("[DefaultLoginConfigurationBuilder]:1");
       this.order = order;
       return this;
   }

   @Override
   public DefaultModuleBuilder loginModule(String appName) {
	   logger.info("[DefaultLoginConfigurationBuilder]:2");
       return new DefaultModuleBuilder(this, appName);
   }

   @Override
   public LoginConfiguration build() {
	   logger.info("[build]: order ==> "+this.order);
	   logger.info("[build]: configuration ==> "+this.configuration);
       return this.order != null ? new OrderedLoginConfiguration(this.order, this.configuration) : this.configuration;
   }

   protected void addEntry(String appName, AppConfigurationEntry configEntries) {
	   logger.info("[addEntry]: appName ==> "+appName);
	   logger.info("[addEntry]: configEntries ==> "+configEntries);
	   logger.info("[addEntry]: configEntries. loginModuleName ==> "+configEntries.getLoginModuleName());
	   
       this.configuration.addEntry(appName, configEntries);
   }

   public class DefaultModuleBuilder implements ModuleBuilder {

       private String appName;
       private Class<? extends LoginModule> moduleClass;
       private LoginModuleControlFlag flag = LoginModuleControlFlag.REQUIRED;
       private Map<String, Object> options = new HashMap<>();
       private DefaultLoginConfigurationBuilder confBuilder;

       public DefaultModuleBuilder(DefaultLoginConfigurationBuilder parent, String appName) {
    	   logger.info("[DefaultLoginConfigurationBuilder]:5");
           this.appName = appName;
           this.confBuilder = parent;
       }

       @Override
       public ModuleBuilder moduleClass(Class<? extends LoginModule> moduleClass) {
    	   logger.info("[DefaultLoginConfigurationBuilder]:6");
           this.moduleClass = moduleClass;
           return this;
       }

       @Override
       public ModuleBuilder controlFlag(String flag) {
           String arg = flag.toLowerCase();
           logger.info("[DefaultLoginConfigurationBuilder]:7");
           if ("required".equals(arg)) {
               return controlFlag(LoginModuleControlFlag.REQUIRED);
           } else if ("requisite".equals(arg)) {
               return controlFlag(LoginModuleControlFlag.REQUISITE);
           } else if ("sufficient".equals(arg)) {
               return controlFlag(LoginModuleControlFlag.SUFFICIENT);
           } else if ("optional".equals(arg)) {
               return controlFlag(LoginModuleControlFlag.OPTIONAL);
           } else {
               throw new IllegalArgumentException("Unknown login module control flag: " + flag);
           }
       }

       @Override
       public ModuleBuilder controlFlag(LoginModuleControlFlag flag) {
    	   logger.info("[DefaultLoginConfigurationBuilder]:8");
           this.flag = flag;
           return this;
       }

       @Override
       public ModuleBuilder option(String name, Object value) {
    	   logger.info("[DefaultLoginConfigurationBuilder]:9");
           this.options.put(name, value);
           return this;
       }

       @Override
       public ModuleBuilder options(Map<String, Object> options) {
    	   logger.info("[DefaultLoginConfigurationBuilder]:10");
           this.options.putAll(options);
           return this;
       }

       @Override
       public LoginConfigurationBuilder add() {
    	   logger.info("[DefaultLoginConfigurationBuilder]:11");
           AppConfigurationEntry entry = new AppConfigurationEntry(this.moduleClass.getName(), this.flag, this.options);
           confBuilder.addEntry(this.appName, entry);
           return confBuilder;
       }
   }

   @Order(LoginConfiguration.DEFAULT_ORDER)
   public class DefaultLoginConfiguration implements LoginConfiguration {

       protected Map<String, List<AppConfigurationEntry>> configEntries = new HashMap<>();

       @Override
       public AppConfigurationEntry[] getApplicationEntries(String appName) {
    	   logger.info("[DefaultLoginConfigurationBuilder]:12");
           List<AppConfigurationEntry> list = this.configEntries.get(appName);
           return list != null ? list.toArray(new AppConfigurationEntry[list.size()]) : new AppConfigurationEntry[0];
       }

       @Override
       public Map<String, AppConfigurationEntry[]> getAllApplicationEntries() {
    	   logger.info("[DefaultLoginConfigurationBuilder]:13");
           return this.configEntries.entrySet().stream()
               .collect(Collectors.toMap(e -> e.getKey(),
                                         e -> e.getValue().toArray(new AppConfigurationEntry[e.getValue().size()])));
       }

       protected void addEntry(String appName, AppConfigurationEntry entry) {
    	   logger.info("[DefaultLoginConfigurationBuilder]:14");
           List<AppConfigurationEntry> list = this.configEntries.get(appName);

           if (list == null) {
               list = new ArrayList<>();
               this.configEntries.put(appName, list);
           }

           list.add(entry);
       }
       
       protected Map<String, List<AppConfigurationEntry>> getConfigEntries() {
    	   logger.info("[DefaultLoginConfigurationBuilder]:15");
           return this.configEntries;
       }
   }
   
   public class OrderedLoginConfiguration extends DefaultLoginConfiguration implements Ordered {
       private int order;
       
       public OrderedLoginConfiguration(int order, DefaultLoginConfiguration srcConfig) {
    	   logger.info("[DefaultLoginConfigurationBuilder]: order ==> "+order);
           this.order = order;
           this.configEntries = new HashMap<>(srcConfig.configEntries);
       }
       
       /* (non-Javadoc)
        * @see org.springframework.core.Ordered#getOrder()
        */
       @Override
       public int getOrder() {
    	   logger.info("[DefaultLoginConfigurationBuilder]:17");
           return this.order;
       }
       
       /**
        * @param order the order to set
        */
       public void setOrder(int order) {
    	   logger.info("[DefaultLoginConfigurationBuilder]:18");
           this.order = order;
       }
   }
}
