package Interfaces;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.Serializable;

import javax.swing.JPanel;
import javax.swing.JTextField;

public class HintTextField extends JTextField implements FocusListener, Serializable {
	private static final long serialVersionUID = 1L;
	private final String hint;
    private boolean showingHint;
	public HintTextField(String hint, Font font, Point pos, Dimension dimendion, JPanel parentPane, boolean nullBorder) {
        super(hint);
        this.hint = hint;
        this.setShowingHint(true);
        super.addFocusListener(this);
        setFont(font);
        setBounds(pos.x, pos.y, dimendion.width, dimendion.height);
        if (nullBorder) {
            setBorder(null);
        }
        parentPane.add(this);
    }
    public HintTextField(String hint, Font font, boolean visible, Point pos, Dimension dimendion, JPanel parentPane, boolean nullBorder, Color background, Color foreground) {
        super(hint);
        this.hint = hint;
        setBackground(background);
        setForeground(foreground);
        setVisible(visible);
        this.setShowingHint(true);
        super.addFocusListener(this);
        setFont(font);
        setBounds(pos.x, pos.y, dimendion.width, dimendion.height);
        if (nullBorder) {
            setBorder(null);
        }
        parentPane.add(this);
    }

    public HintTextField(final String hint) {
        super(hint);
        this.hint = hint;
        this.setShowingHint(true);
        super.addFocusListener(this);
    }

    @Override
    public void focusGained(FocusEvent e) {
        if (this.getText().isEmpty()) {
            super.setText("");
            setShowingHint(false);
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        if (this.getText().isEmpty()) {
            super.setText(hint);
            setShowingHint(true);
        }
    }

    @Override
    public String getText() {
        String typed = super.getText();
        return typed;
    }

	public boolean isShowingHint() {
		return showingHint;
	}
	public void setShowingHint(boolean showingHint) {
		this.showingHint = showingHint;
	}
}
