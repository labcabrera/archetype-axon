package org.labcabrera.sample.archetype.casestep.infrastructure.persistence.jpa.repositories;

import java.util.List;
import java.util.Optional;

import org.labcabrera.sample.archetype.casestep.application.ports.CaseStepRepository;
import org.labcabrera.sample.archetype.casestep.domain.aggregates.CaseStepAggregate;
import org.labcabrera.sample.archetype.casestep.infrastructure.persistence.jpa.mappers.CaseStepMapper;
import org.labcabrera.sample.archetype.shared.domain.exceptions.BadRequestException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CaseStepRepositoryJpaAdapter implements CaseStepRepository {

    private final CaseStepJpaRepository jpaRepository;
    private final CaseStepMapper mapper;

    @Override
    public Optional<CaseStepAggregate> findById(String caseStepId) {
        return jpaRepository.findById(caseStepId).map(entity -> mapper.toDomain(entity));
    }

    @Override
    public List<CaseStepAggregate> findByCaseFolderId(String caseFolderId) {
        var list = jpaRepository.findByCaseFolderId(caseFolderId);
        return list.stream().map(entity -> mapper.toDomain(entity)).toList();
    }

    @Override
    @Transactional
    public CaseStepAggregate save(CaseStepAggregate caseStep) {
        try {
            if (caseStep.getId() != null && jpaRepository.existsById(caseStep.getId())) {
                throw new BadRequestException("case-step.msg.err.already-exists", caseStep.getId());
            }
            var entity = mapper.toEntity(caseStep);
            var savedEntity = jpaRepository.save(entity);
            return mapper.toDomain(savedEntity);
        }
        catch (DataIntegrityViolationException ex) {
            throw new BadRequestException("case-step.msg.err.data-integrity", ex);
        }
    }

    @Override
    @Transactional
    public void deleteByCaseFolderId(String caseFolderId) {
        jpaRepository.deleteByCaseFolderId(caseFolderId);
    }

}
