package com.example.dao.audit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
/**
* If you expose a bean of type AuditorAware to the ApplicationContext, the auditing infrastructure picks it up automatically and uses it to determine the current username to be set on the fields annotated with @CreatedBy or @LastModifiedBy.
*/
@Slf4j
public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        log.info("Auditor working");
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .map(Object::toString);
    }
}