package org.labcabrera.sample.archetype.casefolder.infrastructure.axon;

import org.axonframework.common.lock.Lock;
import org.axonframework.common.lock.LockFactory;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.messaging.unitofwork.CurrentUnitOfWork;
import org.axonframework.modelling.command.Aggregate;
import org.axonframework.modelling.command.AggregateNotFoundException;
import org.axonframework.modelling.command.Repository;
import org.axonframework.modelling.command.RepositoryProvider;
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
    public Aggregate<CaseFolderAggregate> newInstance(java.util.concurrent.Callable<CaseFolderAggregate> factoryMethod) throws Exception {
        Lock lock = null;
        try {
            CaseFolderAggregate root = factoryMethod.call();
            String aggregateIdentifier = aggregateModel.getIdentifier(root).toString();

            lock = lockFactory.obtainLock(aggregateIdentifier);

            AnnotatedAggregate<CaseFolderAggregate> aggregate = AnnotatedAggregate.initialize(
                root, aggregateModel, eventBus);

            // Save on commit
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
    public Aggregate<CaseFolderAggregate> load(String aggregateIdentifier, Long expectedVersion) {
        return load(aggregateIdentifier);
    }

    @Override
    public Aggregate<CaseFolderAggregate> load(String aggregateIdentifier) {
        Lock lock = null;
        try {
            lock = lockFactory.obtainLock(aggregateIdentifier);

            log.debug("Loading aggregate: {}", aggregateIdentifier);
            CaseFolderAggregate root = caseFolderRepository.findById(aggregateIdentifier)
                .orElseThrow(() -> new AggregateNotFoundException(aggregateIdentifier,
                    "CaseFolder aggregate not found"));

            AnnotatedAggregate<CaseFolderAggregate> aggregate = AnnotatedAggregate.initialize(
                root, aggregateModel, eventBus);

            // Update on commit
            CurrentUnitOfWork.get().onPrepareCommit(uow -> {
                log.debug("Updating aggregate: {}", aggregateIdentifier);
                aggregate.invoke(caseFolderRepository::update);
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
    public void send(org.axonframework.messaging.Message<?> message,
        org.axonframework.messaging.ScopeDescriptor scopeDescription) throws Exception {
        // Not needed for command handling
    }

    @Override
    public boolean canResolve(org.axonframework.messaging.ScopeDescriptor scopeDescription) {
        return false;
    }
}
