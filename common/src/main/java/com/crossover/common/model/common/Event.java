package com.crossover.common.model.common;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;
import java.util.Date;

/**
 * This is the event model that represents a doctors conference
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
public class Event implements Serializable {

    /** Start date column name **/
    public static final String START_DATE_COLUMN = "start_date";

    /** Event Id **/
    @DatabaseField(generatedId = true)
    private Integer id;

    /** Event name **/
    @DatabaseField(canBeNull = false)
    private String name;

    /** Event start date **/
    @DatabaseField(canBeNull = false, dataType = DataType.DATE_LONG, columnName = START_DATE_COLUMN)
    private Date startDate;

    /** Event end date **/
    @DatabaseField(dataType = DataType.DATE_LONG)
    private Date endDate;

    /** Attendants Users Ids **/
    @DatabaseField(canBeNull = false, dataType = DataType.SERIALIZABLE)
    private Integer[] attendantsIds;

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @return id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the startDate
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * @return startDate the startDate to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the endDate
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @return endDate the endDate to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * @return the attendantsIds
     */
    public Integer[] getAttendantsIds() {
        return attendantsIds;
    }

    /**
     * @return attendantsIds the attendantsIds to set
     */
    public void setAttendantsIds(Integer[] attendantsIds) {
        this.attendantsIds = attendantsIds;
    }
}
