package com.teamtreehouse.techdegrees.exc;

import org.sql2o.Sql2oException;


public class DaoException extends Exception {
    private final Exception originalException;

    public DaoException(Sql2oException originalException, String msg) {
        super(msg);
        this.originalException = originalException;
    }
}
