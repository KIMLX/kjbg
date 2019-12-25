package com.kjbg.integration.authenticator;


import com.kjbg.entity.UserPojo;
import com.kjbg.integration.IntegrationAuthenticationEntity;


public interface IntegrationAuthenticator {

    /**
     * 处理集成认证
     * @param entity    集成认证实体
     * @return 用户表实体
     */
    UserPojo authenticate(IntegrationAuthenticationEntity entity);

    /**
     * 预处理
     * @param entity    集成认证实体
     */
    void prepare(IntegrationAuthenticationEntity entity);

    /**
     * 判断是否支持集成认证类型
     * @param entity    集成认证实体
     */
    boolean support(IntegrationAuthenticationEntity entity);

    /**
     * 认证结束后执行
     * @param entity    集成认证实体
     */
    void complete(IntegrationAuthenticationEntity entity);
}
