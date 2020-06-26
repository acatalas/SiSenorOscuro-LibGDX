package com.ale.sisenoroscuro.recyclerView;


import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.ArrayList;

public class ArrayListAdapter<ItemT>
        extends AbstractListAdapter<ItemT> {

    private ArrayList<ItemT> arrayList;

    public ArrayListAdapter(Skin skin, ViewHolder viewHolder){
        super(skin, viewHolder);
        this.arrayList = new ArrayList<>();
    }

    public ArrayListAdapter(Skin skin, ArrayList<ItemT> arrayList, ViewHolder viewHolder){
        super(skin, viewHolder);
        this.arrayList = arrayList;
        for(ItemT item : arrayList){
            Actor view = createView(item);
            main.addActor(view);
        }
    }

    public ArrayListAdapter(Skin skin, ViewHolder viewHolder, int alignment){
        super(skin, viewHolder, alignment);
        this.arrayList = new ArrayList<>();
    }

    public ArrayListAdapter(Skin skin, ArrayList<ItemT> arrayList, ViewHolder viewHolder, int alignment){
        super(skin, viewHolder, alignment);
        this.arrayList = arrayList;
        for(ItemT item : arrayList){
            Actor view = createView(item);
            main.addActor(view);
        }
    }

    @Override
    public void add(ItemT itemT) {
        arrayList.add(itemT);
        Actor view = createView(itemT);
        main.addActor(view);
    }

    @Override
    public ItemT get(int index) {
        return arrayList.get(index);
    }

    @Override
    public ItemT remove(int index) {
        removeView(arrayList.get(index));
        return arrayList.remove(index);
    }

    @Override
    public boolean remove(ItemT item) {
        arrayList.remove(item);
        return removeView(item);
    }

    public Actor getView(){
        return scrollPane;
    }

    public void itemChanged(ItemT item){
        updateView(item);
    }
}
