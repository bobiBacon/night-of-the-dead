package net.bobbacon.entity.block_entity;

import net.minecraft.util.collection.DefaultedList;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class UpdateNotifyingInventory<T> extends DefaultedList<T> {
    ContentUpdatable updatable;
    protected UpdateNotifyingInventory(List<T> delegate, @Nullable T initialElement,ContentUpdatable updatable) {
        super(delegate, initialElement);
        this.updatable =updatable;
    }
    public static <T> UpdateNotifyingInventory<T> create(int size, T defaultValue, ContentUpdatable updatable) {
        Validate.notNull(defaultValue);
        Object[] objects = new Object[size];
        Arrays.fill(objects, defaultValue);
        return (UpdateNotifyingInventory<T>) new UpdateNotifyingInventory<>(Arrays.asList(objects), defaultValue,updatable);
    }



    @Override
    public T set(int index, T element) {
        T t=super.set(index, element);
        updatable.onContentChanged();

        return t;
    }

    @Override
    public void add(int index, T element) {
        super.add(index, element);
        updatable.onContentChanged();
    }

    @Override
    public T remove(int index) {
        T t=super.remove(index);

        updatable.onContentChanged();

        return t;
    }

    @Override
    public void clear() {
        super.clear();
        updatable.onContentChanged();

    }

    @Override
    public boolean add(T t) {
        boolean b=super.add(t);

        updatable.onContentChanged();

        return b;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        boolean b=super.addAll(index, c);
        updatable.onContentChanged();

        return b;
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        super.removeRange(fromIndex, toIndex);
        updatable.onContentChanged();
    }

    @Override
    public boolean remove(Object o) {
        boolean b= super.remove(o);
        updatable.onContentChanged();
        return b;
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends T> c) {
        boolean b=super.addAll(c);
        updatable.onContentChanged();

        return b;
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        boolean b= super.removeAll(c);
        updatable.onContentChanged();

        return b;
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        boolean b=super.retainAll(c);
        updatable.onContentChanged();

        return b;
    }
}
