package org.labcabrera.sample.archetype.casefolder.domain.aggregates;

import java.util.List;

import org.springframework.data.domain.PageImpl;

//TODO remove. Hack for Axon issue
public class CaseFolderPage extends PageImpl<CaseFolderAggregate> {

    public CaseFolderPage(List<CaseFolderAggregate> content) {
        super(content);
    }

}
