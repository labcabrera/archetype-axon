package org.labcabrera.sample.archetype.shared.domain.exceptions;

import lombok.Getter;

public class DomainException extends RuntimeException {

    @Getter
    private int status;

    @Getter
    private Object[] args;

    public DomainException(String code, int status, Object... args) {
        super(code);
        this.status = status;
        this.args = args;
    }

    public DomainException(String code, int status, Throwable cause, Object... args) {
        super(code, cause);
        this.status = status;
        this.args = args;
    }

}
