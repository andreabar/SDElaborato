package view.controllers;

import com.github.wolfie.refresher.Refresher;
import com.github.wolfie.refresher.Refresher.RefreshListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;

import view.views.TabSheetView;

public class TabViewController {
	
	private TabSheetView tabSheetView;

	
	public TabViewController(){
		setTabSheetView(new TabSheetView());
		tabSheetView.getTabSheet().addListener(new AddRefreshListener(this));
	}

	public TabSheetView getTabSheetView() {
		return tabSheetView;
	}

	public void setTabSheetView(TabSheetView tabSheetView) {
		this.tabSheetView = tabSheetView;
	}


}

class AddRefreshListener implements SelectedTabChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8650102474379135499L;
	private TabViewController tbc;
	
	public AddRefreshListener(TabViewController tbc){
		this.tbc = tbc;
	}

	@Override
	public void selectedTabChange(SelectedTabChangeEvent event) {
		final TabSheet source = (TabSheet) event.getSource();
		
		if(source.getSelectedTab().getCaption().equals("My Results")){
			tbc.getTabSheetView().getMainViewController().getMainView().getSearchButton().removeClickShortcut();
			tbc.getTabSheetView().getResultViewController().loadResultTable();
			tbc.getTabSheetView().getResultViewController().loadDownloadedFileTable();
			tbc.getTabSheetView().getResultViewController().setRefresher(new Refresher());
			tbc.getTabSheetView().getResultViewController().getRefresher().
			addListener(new RefreshTableListener(tbc.getTabSheetView().getResultViewController()));
			tbc.getTabSheetView().getResultViewController().getResultView().
			addComponent(tbc.getTabSheetView().getResultViewController().getRefresher());
			tbc.getTabSheetView().getMainViewController().getMainView().
			getSearchButton().removeClickShortcut();
		} else {
			tbc.getTabSheetView().getResultViewController().getResultView().
			removeComponent(tbc.getTabSheetView().getResultViewController().getRefresher());
			tbc.getTabSheetView().getMainViewController().getMainView().
			getSearchButton().setClickShortcut(KeyCode.ENTER);
		}
		
	}
	
}

class RefreshTableListener implements RefreshListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5765045110932725268L;
	
	private ResultViewController rvc; 
	
	public RefreshTableListener(ResultViewController rvc){
		this.rvc = rvc;
	}

	@Override
	public void refresh(Refresher source) {
		source.setRefreshInterval(10000);
		rvc.loadResultTable();
		rvc.loadDownloadedFileTable();
		if(rvc.isJunkDataInTable()){
			rvc.getResultView().getClear().setEnabled(true);
		}
	}


	
}
