package com.ale.sisenoroscuro;

import com.ale.sisenoroscuro.actors.CurrentCardStackActor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class ActionGenerator {

    public static Action getShowCardAction(){
        return Actions.scaleBy(8.5f , 8.5f, 0.3f);
    }

    public static Action getMoveCardToStackAction(final CurrentCardStackActor cardStackActor){
        Vector2 cardStackPosition = cardStackActor.getAbsolutePosition();
        return Actions.sequence(
                Actions.parallel(
                        Actions.moveBy(cardStackPosition.x, cardStackPosition.y, 0.3f),
                        Actions.scaleBy(-8.5f, -8.5f, 0.3f)),
                Actions.hide(),
                Actions.run(new Runnable() {
                    public void run() {
                        cardStackActor.setTouchable(Touchable.enabled);
                    }
                }));
    }
}
