package me.david.sploty4.objects.listener;

public interface SimpleListener<T> extends Listener {

    void event(T event);

}
