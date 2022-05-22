package myBootAngularLoginJaas2.myCustomJaas;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.jaas.DefaultJaasAuthenticationProvider;
import org.springframework.security.authentication.jaas.JaasAuthenticationCallbackHandler;
import org.springframework.security.authentication.jaas.JaasAuthenticationToken;
import org.springframework.security.authentication.jaas.JaasGrantedAuthority;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.session.SessionDestroyedEvent;



import myBootAngularLoginJaas2.common.logging.LoggingUtil;
import myBootAngularLoginJaas2.common.logging.LoggingUtil.LogLevel;


import java.io.IOException;
import java.security.AccessController;
import java.security.Principal;
import java.security.acl.Group;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

/**
 *
 */
public class DefaultKyloJaasAuthenticationProvider extends DefaultJaasAuthenticationProvider {
    
	private static final Logger log = LoggerFactory.getLogger(DefaultKyloJaasAuthenticationProvider.class);
    private static final Logger logger = LoggerFactory.getLogger(DefaultKyloJaasAuthenticationProvider.class);

//  public enum LogFields { PERM, ENTITY, RESULT, USER, GROUPS, IP_ADDRESS };
  public enum LogFields { RESULT, USER, GROUPS };

  @org.springframework.beans.factory.annotation.Value("${security.log.auth:false}")
  private boolean logAuthentication;
  
  @org.springframework.beans.factory.annotation.Value("${security.log.auth.level:DEBUG}")
  private String logLevelValue;
  
  @org.springframework.beans.factory.annotation.Value("${security.log.auth.login.format:Authentication attempt: {RESULT}, user: {USER}}")
  private String loginFormat;
  
  @org.springframework.beans.factory.annotation.Value("${security.log.auth.logout.format:Logout attempt: {RESULT}, user: {USER}}")
  private String logoutFormat;
  
  private LoggingUtil.LogLevel logLevel;
  private List<LogFields> loginFields;
  private String loginMessage;
  
  private String loginContextName;
  private JaasAuthenticationCallbackHandler[] callbackHandlers;
  
  @PostConstruct
  protected void initLogging() {
      this.logLevel = LogLevel.level(logLevelValue);
      this.loginFields = LoggingUtil.extractTokens(LogFields.class, loginFormat);
      this.loginMessage = LoggingUtil.toLogMessage(loginFormat);
  }


    /* (non-Javadoc)
     * @see org.springframework.security.authentication.jaas.AbstractJaasAuthenticationProvider#setCallbackHandlers(org.springframework.security.authentication.jaas.JaasAuthenticationCallbackHandler[])
     */
    @Override
    public void setCallbackHandlers(JaasAuthenticationCallbackHandler[] callbackHandlers) {
    	logger.info("[setCallbackHandlers]1");
        super.setCallbackHandlers(callbackHandlers);
        logger.info("[setCallbackHandlers]2");
        this.callbackHandlers = callbackHandlers;
    }
    
    /* (non-Javadoc)
     * @see org.springframework.security.authentication.jaas.AbstractJaasAuthenticationProvider#setLoginContextName(java.lang.String)
     */
    @Override
    public void setLoginContextName(String loginContextName) {
    	logger.info("[setLoginContextName] STARTED");
        super.setLoginContextName(loginContextName);
        logger.info("[setLoginContextName] 1");
        this.loginContextName = loginContextName;
        logger.info("[setLoginContextName] loginContextName ==> "+loginContextName);
        logger.info("[setLoginContextName] FINISHED");
    }
    
    protected String getContextName() {
        return loginContextName;
    }
    
    /* (non-Javadoc)
     * @see org.springframework.security.authentication.jaas.AbstractJaasAuthenticationProvider#handleLogout(org.springframework.security.core.session.SessionDestroyedEvent)
     */
    @Override
    protected void handleLogout(SessionDestroyedEvent event) {
        List<SecurityContext> contexts = event.getSecurityContexts();

        if (contexts.isEmpty()) {
            log.debug("The destroyed session has no SecurityContexts");
            return;
        }

        for (SecurityContext context : contexts) {
            Authentication auth = context.getAuthentication();

            if (auth != null) {
                try {
                    getLoginContext(auth)
                        .ifPresent(loginContext -> { 
                            JaasAuthenticationToken jaasToken = (JaasAuthenticationToken) auth;
                            
                            try {
                                loginContext.logout();
             
                            } catch (LoginException e) {
             
                                log.debug("Error logging out of LoginContext", e);
                            }
                        });
                } catch (LoginException e) {
                    log.warn("Error obtaining LoginContext", e);
                }
            }
        }
    }
    
    protected LoginContext createLoginContext(Subject subject, CallbackHandler handler) throws LoginException {
    	logger.info("[createLoginContext] STARTED");
        return new LoginContext(getContextName(), subject, handler, getConfiguration());
    }
    
    
    /* (non-Javadoc)
     * @see org.springframework.security.authentication.jaas.AbstractJaasAuthenticationProvider#publishFailureEvent(org.springframework.security.authentication.UsernamePasswordAuthenticationToken, org.springframework.security.core.AuthenticationException)
     */
    
    private Function<LogFields, Object> deriveSuccessTokenValues(JaasAuthenticationToken token) {
        return (field) -> {
            switch (field) {
                case RESULT:
                    return "success";
                case USER:
                    return token.getName();
                case GROUPS:
                    return token.getAuthorities().stream()
                                    .map(JaasGrantedAuthority.class::cast)
                                    .map(JaasGrantedAuthority::getPrincipal)
                                    .filter(Group.class::isInstance)
                                    .map(Principal::getName)
                                    .collect(Collectors.joining(", "));
//                  case IP_ADDRESS:
//                  return "";
                default:
                    return "";
            }
        };
    }

    private Function<LogFields, Object> deriveFailedTokenValues(Exception exception, UsernamePasswordAuthenticationToken token) {
        return (field) -> {
            switch (field) {
                case RESULT:
                    return "failed - " + exception.getMessage();
                case USER:
                    return token.getName();
                case GROUPS:
                    return token.getAuthorities().stream()
                            .filter(JaasGrantedAuthority.class::isInstance)
                            .map(JaasGrantedAuthority.class::cast)
                            .map(JaasGrantedAuthority::getPrincipal)
                            .filter(Group.class::isInstance)
                            .map(Principal::getName)
                            .collect(Collectors.joining(", "));
//                case IP_ADDRESS:
//                    return "";
                default:
                    return "";
            }
        };
    }

    private Optional<LoginContext> getLoginContext(Authentication auth) throws LoginException {
    	logger.info("[getLoginContext] STARTED");
        LoginContext loginContext;
        logger.info("[getLoginContext] 1");
        if (auth instanceof JaasAuthenticationToken) {
        	logger.info("[getLoginContext] 2");
            JaasAuthenticationToken token = (JaasAuthenticationToken) auth;
            logger.info("[getLoginContext] 3");
            loginContext = token.getLoginContext();
            logger.info("[getLoginContext] 4");
            if (loginContext == null) {
            	logger.info("[getLoginContext] 5");
                loginContext = createLoginContext(createSubject(auth), new InternalCallbackHandler(auth));
                logger.info("[getLoginContext] 6");
                log.debug("Created LoginContext for auth: {}", auth);
            } else {
            	logger.info("[getLoginContext] 7");
                log.debug("Using LoginContext from token: {}", token);
            }
        } else {
        	logger.info("[getLoginContext] 8");
            loginContext = createLoginContext(createSubject(auth), new InternalCallbackHandler(auth));
            log.debug("Created LoginContext for auth: {}", auth);
        }
        logger.info("[getLoginContext] loginContext ==> "+loginContext);
        logger.info("[getLoginContext] FINISHED");
        return Optional.ofNullable(loginContext);
    }
    

    private Subject createSubject(Authentication auth) {
    //	LoggingUtil.log(log, this.logLevel, LogFields.class, this.logoutFormat, null);
    	return null;
    }


    /**
     * Wrapper class for JAASAuthenticationCallbackHandlers
     */
    private class InternalCallbackHandler implements CallbackHandler {
        private final Authentication authentication;

        public InternalCallbackHandler(Authentication authentication) {
        	logger.info("[InternalCallbackHandler]6");
            this.authentication = authentication;
        }

        public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
            for (JaasAuthenticationCallbackHandler handler : DefaultKyloJaasAuthenticationProvider.this.callbackHandlers) {
                for (Callback callback : callbacks) {
                    handler.handle(callback, this.authentication);
                }
            }
        }
    }

}
