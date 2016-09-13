package bookyard.client

import java.awt.*;
import java.awt.font.TextAttribute;
import javax.swing.*;
import kotlin.collections.Map;

public class HyperLinkLabel(var url : String, var text : String) : JPanel() {

    val label: JLabel;

    init {

        val gridLayout : GridLayout = GridLayout(0, 1);
        gridLayout.setVgap(7);
        this.setLayout(gridLayout);

        label = JLabel();

        this.setLabel(this.url, this.text);
        this.add(this.label);

        val font : Font = this.label.getFont();
        val attributesMap : Map<TextAttribute, Any?> = font.getAttributes();
        val attributes : MutableMap<TextAttribute, Any?> = attributesMap as MutableMap<TextAttribute, Any?>;
        attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        this.label.setFont(font.deriveFont(attributes));

        this.label.addMouseListener(CursorChanger(this.label));
    }

    public fun setLabel(url: String, title: String) {

        val text : String = "<html><a href=\" " + url + "\">" + title + "</a></html>";
        this.label.setText(text);
    }

}