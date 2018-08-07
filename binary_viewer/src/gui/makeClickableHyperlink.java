package gui;

import java.awt.Desktop;

import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

public class makeClickableHyperlink implements HyperlinkListener{
	@Override
    public void hyperlinkUpdate(HyperlinkEvent hle) {
        if (HyperlinkEvent.EventType.ACTIVATED.equals(hle.getEventType())) {
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.browse(hle.getURL().toURI());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
