package org.labcabrera.sample.archetype.casefolder.infrastructure.axon;

import java.util.concurrent.Callable;

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
import org.labcabrera.sample.archetype.casefolder.application.ports.CaseFolderRepository;
import org.labcabrera.sample.archetype.casefolder.domain.aggregates.CaseFolderAggregate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StateStoredCaseFolderRepository implements Repository<CaseFolderAggregate> {

    private final CaseFolderRepository caseFolderRepository;
    private final LockFactory lockFactory;
    private final EventBus eventBus;
    private final AggregateModel<CaseFolderAggregate> aggregateModel;

    public StateStoredCaseFolderRepository(CaseFolderRepository caseFolderRepository,
        LockFactory lockFactory,
        EventBus eventBus) {
        this.caseFolderRepository = caseFolderRepository;
        this.lockFactory = lockFactory;
        this.eventBus = eventBus;
        this.aggregateModel = AnnotatedAggregateMetaModelFactory.inspectAggregate(CaseFolderAggregate.class);
    }

    @Override
    public Aggregate<CaseFolderAggregate> newInstance(@Nonnull Callable<CaseFolderAggregate> factoryMethod) throws Exception {
        Lock lock = null;
        try {
            // Initialize the aggregate first, which sets up the Axon scope
            AnnotatedAggregate<CaseFolderAggregate> aggregate = AnnotatedAggregate.initialize(factoryMethod, aggregateModel, eventBus);
            String aggregateIdentifier = aggregate.identifier().toString();
            lock = lockFactory.obtainLock(aggregateIdentifier);
            CurrentUnitOfWork.get().onPrepareCommit(uow -> {
                log.debug("Saving new aggregate: {}", aggregateIdentifier);
                aggregate.invoke(caseFolderRepository::save);
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
    public Aggregate<CaseFolderAggregate> load(@Nonnull String aggregateIdentifier, @Nullable Long expectedVersion) {
        log.debug("Loading aggregate with expected version: {}", aggregateIdentifier);
        return load(aggregateIdentifier);
    }

    @Override
    public Aggregate<CaseFolderAggregate> load(@Nonnull String aggregateIdentifier) {
        Lock lock = null;
        try {
            lock = lockFactory.obtainLock(aggregateIdentifier);

            log.debug("Loading aggregate: {}", aggregateIdentifier);
            CaseFolderAggregate root = caseFolderRepository.findById(aggregateIdentifier)
                .orElseThrow(() -> new AggregateNotFoundException(aggregateIdentifier, "CaseFolder aggregate not found"));
            AnnotatedAggregate<CaseFolderAggregate> aggregate = AnnotatedAggregate.initialize(root, aggregateModel, eventBus);
            CurrentUnitOfWork.get().onPrepareCommit(uow -> {
                if (aggregate.isDeleted()) {
                    log.debug("Deleting aggregate: {}", aggregateIdentifier);
                    caseFolderRepository.deleteById(aggregateIdentifier);
                } else {
                    log.debug("Updating aggregate: {}", aggregateIdentifier);
                    aggregate.invoke(caseFolderRepository::update);
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
}
