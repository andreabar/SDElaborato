package refresher;

import com.vaadin.ui.Table;

public abstract class RefreshableTable extends Table{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7155624012292419369L;

	public RefreshableTable(String string) {
		super(string);
	}

	public abstract void refresh();
}
