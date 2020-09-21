package com.fairandsmart.consent.manager.cache;

public interface Cache<T> {

    void put(String id, T data);

    boolean containsKey(String id);

    T lookup(String id);

    void remove(String id);

}
