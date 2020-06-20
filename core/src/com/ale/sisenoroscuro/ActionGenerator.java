package com.ale.sisenoroscuro;

import com.ale.sisenoroscuro.actors.CurrentCardStackActor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class ActionGenerator {
    public static final float CARD_SCALING_FACTOR = 5.5f;

    public static Action getShowCardAction(){
        return Actions.scaleBy(CARD_SCALING_FACTOR , CARD_SCALING_FACTOR, 0.3f);
    }

    public static Action getMoveCardToStackAction(final CurrentCardStackActor cardStackActor){
        Vector2 cardStackPosition = cardStackActor.getAbsolutePosition();
        return Actions.sequence(
                Actions.parallel(
                        Actions.moveBy(cardStackPosition.x, cardStackPosition.y, 0.3f),
                        Actions.scaleBy(-CARD_SCALING_FACTOR, -CARD_SCALING_FACTOR, 0.3f)),
                Actions.hide(),
                Actions.run(new Runnable() {
                    public void run() {
                        cardStackActor.setTouchable(Touchable.enabled);
                    }
                }));
    }

    public static Action getPanDownAction(float panDownDistance, float duration){
        return Actions.moveBy(0, panDownDistance, duration);
    }

    public static Action getPanDownAndScaleUpAction(float panDownDistance, float duration){
        return Actions.parallel(
                getPanDownAction(panDownDistance, duration),
                Actions.scaleTo(1, 1, duration));
    }
}
