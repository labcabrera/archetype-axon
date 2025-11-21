package org.labcabrera.sample.archetype.casestep.infrastructure.axon;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.axonframework.common.lock.Lock;
import org.axonframework.common.lock.LockFactory;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.messaging.ScopeDescriptor;
import org.axonframework.messaging.unitofwork.CurrentUnitOfWork;
import org.axonframework.modelling.command.Aggregate;
import org.axonframework.modelling.command.AggregateNotFoundException;
import org.axonframework.modelling.command.Repository;
import org.axonframework.modelling.command.inspection.AggregateModel;
import org.axonframework.modelling.command.inspection.AnnotatedAggregate;
import org.axonframework.modelling.command.inspection.AnnotatedAggregateMetaModelFactory;
import org.labcabrera.sample.archetype.casestep.application.ports.CaseStepRepository;
import org.labcabrera.sample.archetype.casestep.domain.aggregates.CaseStep;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StateStoredCaseStepRepository implements Repository<CaseStep> {

    private final CaseStepRepository caseStepRepository;
    private final LockFactory lockFactory;
    private final EventBus eventBus;
    private final AggregateModel<CaseStep> aggregateModel;

    public StateStoredCaseStepRepository(CaseStepRepository caseStepRepository, LockFactory lockFactory, EventBus eventBus) {
        this.caseStepRepository = caseStepRepository;
        this.lockFactory = lockFactory;
        this.eventBus = eventBus;
        this.aggregateModel = AnnotatedAggregateMetaModelFactory.inspectAggregate(CaseStep.class);
    }

    @Override
    public Aggregate<CaseStep> newInstance(@Nonnull java.util.concurrent.Callable<CaseStep> factoryMethod) throws Exception {
        Lock lock = null;
        try {
            AnnotatedAggregate<CaseStep> aggregate = AnnotatedAggregate.initialize(
                factoryMethod, aggregateModel, eventBus);

            String aggregateIdentifier = aggregate.identifier().toString();
            lock = lockFactory.obtainLock(aggregateIdentifier);

            CurrentUnitOfWork.get().onPrepareCommit(uow -> {
                log.debug("Saving new case step aggregate: {}", aggregateIdentifier);
                aggregate.invoke(caseStepRepository::save);
            });

            return aggregate;
        }
        finally {
            if (lock != null) {
                lock.release();
            }
        }
    }

    @Override
    public Aggregate<CaseStep> load(@Nonnull String aggregateIdentifier, @Nullable Long expectedVersion) {
        log.debug("Loading case step aggregate with expected version: {}", aggregateIdentifier);
        return load(aggregateIdentifier);
    }

    @Override
    public Aggregate<CaseStep> load(@Nonnull String aggregateIdentifier) {
        Lock lock = null;
        try {
            lock = lockFactory.obtainLock(aggregateIdentifier);

            log.debug("Loading case step aggregate: {}", aggregateIdentifier);
            CaseStep root = caseStepRepository.findById(aggregateIdentifier)
                .orElseThrow(() -> new AggregateNotFoundException(aggregateIdentifier, "CaseStep aggregate not found"));
            AnnotatedAggregate<CaseStep> aggregate = AnnotatedAggregate.initialize(root, aggregateModel, eventBus);
            CurrentUnitOfWork.get().onPrepareCommit(uow -> {
                if (aggregate.isDeleted()) {
                    log.debug("Deleting case step aggregate: {}", aggregateIdentifier);
                    caseStepRepository.deleteByCaseFolderId(aggregateIdentifier);
                }
                else {
                    log.debug("Updating case step aggregate: {}", aggregateIdentifier);
                    aggregate.invoke(caseStepRepository::save);
                }
            });
            return aggregate;
        }
        finally {
            if (lock != null) {
                lock.release();
            }
        }
    }

    @Override
    public void send(org.axonframework.messaging.Message<?> message, ScopeDescriptor scopeDescription) throws Exception {
        // Not needed for command handling
    }

    @Override
    public boolean canResolve(ScopeDescriptor scopeDescription) {
        return false;
    }

    @Override
    public Aggregate<CaseStep> loadOrCreate(@Nonnull String aggregateIdentifier,
        @Nonnull java.util.concurrent.Callable<CaseStep> factoryMethod)
        throws Exception {
        try {
            return load(aggregateIdentifier);
        }
        catch (AggregateNotFoundException e) {
            log.debug("Aggregate not found, creating new: {}", aggregateIdentifier);
            return newInstance(factoryMethod);
        }
    }
}
