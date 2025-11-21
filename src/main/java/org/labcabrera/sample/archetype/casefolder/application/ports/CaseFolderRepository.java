package org.labcabrera.sample.archetype.casefolder.application.ports;

import org.labcabrera.sample.archetype.casefolder.domain.aggregates.CaseFolderAggregate;
import org.labcabrera.sample.archetype.casefolder.domain.valueobjects.CaseFolderStatus;
import org.labcabrera.sample.archetype.shared.application.SecurityPort.AuthenticatedUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CaseFolderRepository {

    Optional<CaseFolderAggregate> findById(String caseFolder);

    Page<CaseFolderAggregate> findByRsql(String rsql, Pageable pageable, AuthenticatedUser user);

    CaseFolderAggregate save(CaseFolderAggregate entity);

    CaseFolderAggregate update(CaseFolderAggregate entity);

    CaseFolderAggregate updateStatus(String caseFolderId, CaseFolderStatus status);

    void deleteById(String caseFolderId);

}