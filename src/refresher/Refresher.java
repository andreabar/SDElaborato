package refresher;

import java.util.Map;

import view.controllers.ResultViewController;

import com.example.sd.widgetset.client.ui.VRefresher;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Label;

/**
 * Server side component for the VRefresher widget.
 */
@com.vaadin.ui.ClientWidget(com.example.sd.widgetset.client.ui.VRefresher.class)
public class Refresher extends AbstractComponent {

/**
	 * 
	 */
	private static final long serialVersionUID = -8914468654734344811L;
	private ResultViewController rvc;
	
	public ResultViewController getRvc() {
		return rvc;
	}

	public void setRvc(ResultViewController rvc) {
		this.rvc = rvc;
	}

	public Refresher() {
	}

	@Override
	public void paintContent(PaintTarget target) throws PaintException {
		super.paintContent(target);

		// Paint any component specific content by setting attributes
		// These attributes can be read in updateFromUIDL in the widget.
		target.addAttribute("clicks", 0);
		System.out.println("Test 0");
//		target.addAttribute("message", message);

		// We could also set variables in which values can be returned
		// but declaring variables here is not required
	}

	/**
	 * Receive and handle events and other variable changes from the client.
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void changeVariables(Object source, Map<String, Object> variables) {
		super.changeVariables(source, variables);

		// Variables set by the widget are returned in the "variables" map.

		if (variables.containsKey("refresh") && null != rvc) {
			
			System.out.println("Refreshing");


			Object id = rvc.getResultView().getFileTable()
					.getCurrentPageFirstItemId();
			Object id2 = rvc.getResultView().getDownloadedFileTable()
					.getCurrentPageFirstItemId();

			rvc.loadResultTable();
			rvc.loadDownloadedFileTable();

			rvc.getResultView().getFileTable().setCurrentPageFirstItemId(id);
			rvc.getResultView().getDownloadedFileTable()
					.setCurrentPageFirstItemId(id2);

			if (rvc.isJunkDataInTable()) {
				rvc.getResultView().getClear().setEnabled(true);
				
			}
		}
	}
	
	
}

