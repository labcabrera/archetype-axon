package org.labcabrera.sample.archetype.casestep.application.ports;

import java.util.List;
import java.util.Optional;

import org.labcabrera.sample.archetype.casestep.domain.aggregates.CaseStepAggregate;

public interface CaseStepRepository {

    Optional<CaseStepAggregate> findById(String caseStep);

    List<CaseStepAggregate> findByCaseFolderId(String caseFolderId);

    void deleteByCaseFolderId(String caseFolderId);

    CaseStepAggregate save(CaseStepAggregate entity);

}