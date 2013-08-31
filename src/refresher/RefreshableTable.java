package refresher;

import com.vaadin.ui.Table;

public abstract class RefreshableTable extends Table{

	public RefreshableTable(String string) {
		super(string);
	}

	public abstract void refresh();
}
