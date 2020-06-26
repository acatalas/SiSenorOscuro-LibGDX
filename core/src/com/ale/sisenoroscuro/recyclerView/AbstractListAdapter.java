package com.ale.sisenoroscuro.recyclerView;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.HashMap;

public abstract class AbstractListAdapter<ItemT> implements ListAdapter<ItemT>{
    private ItemClickListener<ItemT> clickListener;
    private HashMap<ItemT, Actor> viewMap;
    protected ScrollPane scrollPane;
    protected VerticalGroup main;
    protected ViewHolder viewHolder;

    protected AbstractListAdapter(Skin skin, ViewHolder viewHolder){
        this.viewHolder = viewHolder;
        viewMap = new HashMap<>();
        main = new VerticalGroup();
        scrollPane = new ScrollPane(main, skin);
        main.padRight(scrollPane.getStyle().vScrollKnob.getMinWidth());
    }

    protected AbstractListAdapter(Skin skin, ViewHolder viewHolder, int alignment){
        this(skin, viewHolder);
        main.align(alignment);
    }

    public Actor getView(ItemT item){
        if(viewMap.get(item) != null){
            return viewMap.get(item);
        }
        return createView(item);
    }

    protected Actor createView(ItemT item){
        Actor view = viewHolder.generateView(item);
        if(clickListener != null) setClickListener(view, item);
        viewMap.put(item, view);
        return view;
    }

    protected void updateView(ItemT item) {
        if(viewMap.get(item) == null) {
            throw new IllegalArgumentException(item.toString() + " was not found in list.");
        }

        viewHolder.updateView(viewMap.get(item), item);
    }

    protected boolean removeView(ItemT itemT){
        return viewMap.remove(itemT) != null;
    }

    public void setItemClickListener(ItemClickListener<ItemT> clickListener){
        this.clickListener = clickListener;

        for(ItemT item : viewMap.keySet()){
            Actor view = viewMap.get(item);
            addClickListener(view, item);
        }
    }

    private void addClickListener(Actor view, ItemT item){
        for(EventListener eventListener : view.getListeners()){
            if (eventListener instanceof AbstractListAdapter.ListClickListener) {
                view.removeListener(eventListener);
                break;
            }
        }
        setClickListener(view, item);
    }

    private void setClickListener(Actor view, ItemT item){
        view.setTouchable(Touchable.enabled);
        view.addListener(new ListClickListener(view, item));
    }

    private class ListClickListener extends ClickListener {
        private Actor view;
        private ItemT item;

        public ListClickListener (Actor view, ItemT item) {
            this.view = view;
            this.item = item;
        }

        @Override
        public void clicked (InputEvent event, float x, float y) {
            if (clickListener != null) clickListener.clicked(item);
        }
    }

    public void setScrollPaneConfig(boolean flickScroll, boolean scrollBarsOnTop,
                                    boolean variableSizeKnob,
                                    boolean hasScrollingX, boolean hasScrollingY){
        scrollPane.setFlickScroll(flickScroll);
        scrollPane.setScrollbarsOnTop(false);
        scrollPane.setVariableSizeKnobs(variableSizeKnob);
        scrollPane.setScrollingDisabled(!hasScrollingX, !hasScrollingY);
    }
}
