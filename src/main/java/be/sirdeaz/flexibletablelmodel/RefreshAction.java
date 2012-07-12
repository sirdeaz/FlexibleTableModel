/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.sirdeaz.flexibletablelmodel;

import java.util.List;

/**
 *
 * @author fdidd
 */
public interface RefreshAction<T> {
    public List<T> refresh();
}
