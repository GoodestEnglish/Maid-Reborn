package rip.diamond.maid.util.menu.buttons;

import java.util.Map;

/**
 * A big button means it isn't only contain 1 gui slots. It will contain multiple gui slots.
 * Note that the button index must be in the center, and make sure there's no other buttons around it.
 * This button is designed for custom texture usage. ie: ItemsAdder
 */

public abstract class BigButton extends Button {

    public static int[] OUTER_INDEX = new int[]{
            -10,-9,-8,
             -1, 0, 1,
              8, 9, 10
    };

    public BigButton(int index, Map<Integer, Button> buttons) {
        for (int outerIndex : OUTER_INDEX) {
            buttons.put(index + outerIndex, this);
        }
    }

}
