package com.ale.sisenoroscuro.recyclerView;


import com.badlogic.gdx.scenes.scene2d.Actor;

public interface ViewHolder<ItemT, ViewT extends Actor> {
    Actor generateView(ItemT itemT);
    Actor updateView(ViewT actor, ItemT itemT);
}
