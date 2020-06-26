package com.ale.sisenoroscuro.recyclerView;

public interface ListAdapter<ItemT> {

    void add(ItemT itemT);

    ItemT get(int index);

    ItemT remove(int index);

    boolean remove(ItemT item);

    interface ItemClickListener<ItemT> {
        void clicked (ItemT item);
    }
}
