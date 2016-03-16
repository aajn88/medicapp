package com.crossover.medicapp.custom_views.chips_table;

import android.widget.TableRow;

/**
 * @author <a href="mailto:antonio-jimenez@accionplus.com">Antonio A. Jimenez N.</a>
 */
public interface IChipsTableAdapter<T> {

    /**
     * Este método indica si hay una siguiente fila para agregar en la tabla
     *
     * @return True si existe. False de lo contrario
     */
    boolean hasNextRow();

    /**
     * Este método solicita la siguiente fila a mostrar.
     *
     * @return Retorna la siguiente fila a mostrar. En caso de no existir, retorna null.
     */
    TableRow nextRow();

    /**
     * Este método retorna el item en la posición específica
     *
     * @param position
     *         Posición del item solicitado
     *
     * @return Elemento solicitado
     */
    T getItem(int position);

    /**
     * Este método establece un Listener para los items seleccionados
     *
     * @param listener
     *         Listener de selección de item
     */
    void setOnItemSelectedListener(ChipsTable.IOnItemSelectedListener listener);

    /**
     * @return the listener
     */
    ChipsTable.IOnItemSelectedListener getOnItemSelectedListener();

}
