package org.labcabrera.sample.archetype.casestep.application.services;

import org.labcabrera.sample.archetype.casestep.domain.aggregates.CaseStepAggregate;
import org.labcabrera.sample.archetype.shared.application.Guard;
import org.labcabrera.sample.archetype.shared.application.SecurityPort.AuthenticatedUser;
import org.springframework.stereotype.Component;

@Component
public class CaseStepGuard implements Guard<CaseStepAggregate> {

    @Override
    public void checkRead(CaseStepAggregate domain, AuthenticatedUser user) {
    }

    @Override
    public void checkWrite(CaseStepAggregate domain, AuthenticatedUser user) {
    }

    @Override
    public void checkCreate(AuthenticatedUser user) {
    }

}
