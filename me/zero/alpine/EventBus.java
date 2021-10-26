package me.zero.alpine;

public interface EventBus {

    void subscribe(Object object);

    void subscribeAll(Object... aobject);

    void subscribeAll(Iterable iterable);

    void unsubscribe(Object object);

    void unsubscribeAll(Object... aobject);

    void unsubscribeAll(Iterable iterable);

    void post(Object object);

    void attach(EventBus eventbus);

    void detach(EventBus eventbus);
}
