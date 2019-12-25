package com.kjbg.integration.authenticator;


import com.kjbg.integration.IntegrationAuthenticationEntity;


public abstract class AbstractPreparableIntegrationAuthenticator implements IntegrationAuthenticator {

    @Override
    public void prepare(IntegrationAuthenticationEntity entity) {

    }

    @Override
    public void complete(IntegrationAuthenticationEntity entity) {

    }
}
