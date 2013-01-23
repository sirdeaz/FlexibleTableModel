/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.sirdeaz.flexibletablelmodel;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * Test on cloud-ide
 * @author fdidd
 */
public class FlexibleTableModel<T> extends AbstractTableModel {

    private final RefreshAction<T> refreshAction;
    List<T> dataItems = new ArrayList<T>();
    List<String> headers = new ArrayList<String>();
    private final boolean asyncRefresh;

    public FlexibleTableModel(RefreshAction<T> refreshAction, Class<T> c, boolean asyncRefresh) {
        this.refreshAction = refreshAction;
        this.asyncRefresh = asyncRefresh;
        doRefresh();
        setupModel(c);
    }

    private void doAsyncRefresh() {
        // TODO implement async refreshing
    }

    /**
     * Synchronously refreshes the model instance. Needs to be called on the EDT
     */
    public final void doRefresh() {
        dataItems = refreshAction.refresh();
        this.fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return this.dataItems.size();
    }

    @Override
    public int getColumnCount() {
        return this.headers.size();
    }

    @Override
    public String getColumnName(int column) {
        return this.headers.get(column);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        try {
            Field f  = dataItems.get(rowIndex).getClass().getDeclaredField(headers.get(columnIndex));
            f.setAccessible(true);
            return f.get(dataItems.get(rowIndex));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private void setupModel(Class<T> c) {
        if (!c.isAnnotationPresent(FlexibleTableObject.class)) {
            throw new IllegalStateException("Provided class should be annotated with @FlexibleTableObject");
        }

        this.headers.addAll(calculateColumns(c));
    }

    private List<String> calculateColumns(Class<T> c) {
        List<String> headers = new ArrayList<String>();

        for (Field f : c.getDeclaredFields()) {
            if (f.isAnnotationPresent(FlexibleTableColumn.class)) {
                FlexibleTableColumn tableColumn = f.getAnnotation(FlexibleTableColumn.class);
                headers.add(tableColumn.name());
            }
        }

        return headers;
    }
}
