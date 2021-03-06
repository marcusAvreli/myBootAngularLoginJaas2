package myBootAngularLoginJaas2.myCustomJaas;




/*-
 * #%L
 * UserProvider Authentication
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

import myBootAngularLoginJaas2.myCustomJaas.AbstractLoginModule;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import com.thinkbiganalytics.metadata.api.user.User;
//import com.thinkbiganalytics.metadata.api.user.UserProvider;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.login.AccountLockedException;
import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.CredentialException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

/**
 * A login module that authenticates users using the Kylo user store.  By default
 * a user is authenticated only if a user with the provided username exits in
 * the store.  It may also be configured to validate a provided password against the
 * one associated with the username in the store as well.
 */
public class KyloLoginModule extends AbstractLoginModule implements LoginModule {

	private static final Logger logger = LoggerFactory.getLogger(KyloLoginModule.class);
    /**
     * Option for the {@link MetadataAccess} object
     */
    public static final String METADATA_ACCESS = "metadataAccess";

    /**
     * Option for the {@link PasswordEncoder} object
     */
    public static final String PASSWORD_ENCODER = "passwordEncoder";

    /**
     * Option for the {@link UserProvider} object
     */
    public static final String USER_PROVIDER = "userProvider";

    /**
     * Option that indicates whether password authentication is required
     */
    public static final String REQUIRE_PASSWORD = "requirePassword";

    /**
     * Metadata store
     */
   

    /**
     * Password encoder
     */
    private PasswordEncoder passwordEncoder;

    /**
     * Whether to required password validation for authentication
     */
    private boolean requirePassword = false;

    /**
     * Metadata user provider
     */
 


    @Override
    public void initialize(@Nonnull final Subject subject, @Nonnull final CallbackHandler callbackHandler, @Nonnull final Map<String, ?> sharedState, @Nonnull final Map<String, ?> options) {
        super.initialize(subject, callbackHandler, sharedState, options);
        logger.info("[initialize]");
    //    metadata = (MetadataAccess) getOption(METADATA_ACCESS).orElseThrow(() -> new IllegalArgumentException("The \"" + METADATA_ACCESS + "\" option is required"));
        passwordEncoder = (PasswordEncoder) getOption(PASSWORD_ENCODER).orElseThrow(() -> new IllegalArgumentException("The \"" + PASSWORD_ENCODER + "\" option is required"));
      //  userProvider = (UserProvider) getOption(USER_PROVIDER).orElseThrow(() -> new IllegalArgumentException("The \"" + USER_PROVIDER + "\" option is required"));
        requirePassword = (Boolean) getOption(REQUIRE_PASSWORD).orElse(false);
    }

    @Override
    protected boolean doLogin() throws Exception {
        // Get username and password
    	logger.info("[doLogin]1");
        final NameCallback nameCallback = new NameCallback("Username: ");
        final PasswordCallback passwordCallback = new PasswordCallback("Password: ", false);

        if (requirePassword) {
            handle(nameCallback, passwordCallback);
        } else {
            handle(nameCallback);
        }

        // Authenticate user

        return true;
    }

    @Override
    protected boolean doCommit() throws Exception {
    	logger.info("[doLogin]2");
        getSubject().getPrincipals().addAll(getAllPrincipals());
        return true;
    }

    @Override
    protected boolean doAbort() throws Exception {
        return doLogout();
    }

    @Override
    protected boolean doLogout() throws Exception {
        getSubject().getPrincipals().removeAll(getAllPrincipals());
        return true;
    }

    /**
     * TODO
     */
    @Nonnull
    private LoginException newLoginException(@Nonnull final Throwable cause) {
    	logger.info("[doLogin]3");
        final LoginException loginException = new LoginException("An unexpected login error occurred.");
        loginException.initCause(cause);
        return loginException;
    }
}
