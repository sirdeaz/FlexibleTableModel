/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.sirdeaz.flexibletablemodel;

import be.sirdeaz.flexibletablelmodel.FlexibleTableObject;
import be.sirdeaz.flexibletablelmodel.FlexibleTableColumn;
import be.sirdeaz.flexibletablelmodel.FlexibleTableModel;
import be.sirdeaz.flexibletablelmodel.RefreshAction;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Unit tests for the FlexibleTableModel.
 * @author fdidd
 */
public class FlexibleTableModelTest {

    public FlexibleTableModelTest() {
    }

    private List<TestClass> generateTestList() {
        List<TestClass> dataItems = new ArrayList<TestClass>();
        dataItems.add(new TestClass("1", "1"));
        dataItems.add(new TestClass("2", "2"));
        dataItems.add(new TestClass("3", "3"));
        dataItems.add(new TestClass("4", "4"));
        dataItems.add(new TestClass("5", "5"));
        return dataItems;
    }

    private FlexibleTableModel<TestClass> createTestModel() {
        return new FlexibleTableModel<TestClass>(new RefreshAction<TestClass>() {

            public List<TestClass> refresh() {
                return generateTestList();
            }
        }, TestClass.class, false);
    }

    /**
     * The provided class should be annotated with @FlexibleTableObject.
     */
    @Test(expected = IllegalStateException.class)
    public void testConstructorError() {
        FlexibleTableModel<FalseTestClass> instance = new FlexibleTableModel<FalseTestClass>(new RefreshAction<FalseTestClass>(){

            public List<FalseTestClass> refresh() {
                return new ArrayList<FalseTestClass>();
            }
            
        }, FalseTestClass.class, false);
    }

    /**
     * Test of getRowCount method, of class FlexibleTableModel.
     */
    @Test
    public void testGetRowCount() {
        FlexibleTableModel instance = createTestModel();
        assertTrue("Test sample should contain 5 lines", instance.getRowCount() == 5);
    }

    /**
     * Test of getColumnCount method, of class FlexibleTableModel.
     */
    @Test
    public void testGetColumnCount() {
        FlexibleTableModel instance = createTestModel();
        assertTrue("Test sample should contain 2 columns", instance.getColumnCount() == 2);
        assertEquals("First header should be called column1", "column1", instance.getColumnName(0));
        assertEquals("Second header should be called column2", "column2", instance.getColumnName(1));
    }

    /**
     * Columns names will be created based upon the fields annotated with @FlexibleTableColumn
     */
    @Test
    public void testGetColumnName() {
        FlexibleTableModel instance = createTestModel();
        assertEquals("First header should be called column1", "column1", instance.getColumnName(0));
        assertEquals("Second header should be called column2", "column2", instance.getColumnName(1));
    }

    /**
     * Test of getValueAt method, of class FlexibleTableModel.
     */
    @Test
    public void testGetValueAt() {
        FlexibleTableModel instance = createTestModel();
        assertEquals("Test sample should return 1", instance.getValueAt(0, 0), "1");
        assertEquals("Test sample should return 2", instance.getValueAt(1, 0), "2");
        assertEquals("Test sample should return 3", instance.getValueAt(2, 0), "3");
        assertEquals("Test sample should return 4", instance.getValueAt(3, 0), "4");
        assertEquals("Test sample should return 5", instance.getValueAt(4, 0), "5");
    }

    /**
     * Valid helper class where all the tests are based upon.
     */
    @FlexibleTableObject(name = "TestClass")
    public static class TestClass {

        @FlexibleTableColumn(name = "column1")
        private String column1;
        @FlexibleTableColumn(name = "column2")
        private String column2;

        public TestClass(String column1, String column2) {
            this.column1 = column1;
            this.column2 = column2;
        }

        public String getColumn1() {
            return column1;
        }

        public String getColumn2() {
            return column2;
        }
    }
    
    /**
     * Invalid helper class
     */
    public static class FalseTestClass {}
}
