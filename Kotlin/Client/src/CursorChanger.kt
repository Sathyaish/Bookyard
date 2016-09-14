package bookyard.client;

import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.Cursor;
import javax.swing.*;

class CursorChanger(val label : JLabel) : MouseListener {

    override public fun mouseClicked(e : MouseEvent) {
    }

    override public fun mousePressed(e : MouseEvent) {
    }

    override public fun mouseReleased(e : MouseEvent) {
    }

    override public fun mouseEntered(e : MouseEvent) {
        label.setCursor(Cursor(Cursor.HAND_CURSOR));
    }

    override public fun mouseExited(e : MouseEvent) {
        label.setCursor(Cursor.getDefaultCursor());
    }
}
