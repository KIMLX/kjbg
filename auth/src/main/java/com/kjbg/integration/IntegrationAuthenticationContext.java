package com.kjbg.integration;


public class IntegrationAuthenticationContext {
    private static ThreadLocal<IntegrationAuthenticationEntity> holder = new ThreadLocal<>();

    public static void set(IntegrationAuthenticationEntity entity){
        holder.set(entity);
    }

    public static IntegrationAuthenticationEntity get(){
        return holder.get();
    }

    public static void clear(){
        holder.remove();
    }
}
