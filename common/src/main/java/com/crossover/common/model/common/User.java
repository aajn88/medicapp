package com.crossover.common.model.common;

import com.crossover.common.model.constants.Role;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

/**
 * This is the representation of a Medicapp User that contains the basic information for each user
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
public class User {

    /** Name of the username column **/
    public static final String USERNAME_COLUMN = "username";

    /** Name of the password column **/
    public static final String PASSWORD_COLUMN = "password";

    /** Name of the active user column **/
    public static final String ACTIVE_COLUMN = "active_user";

    /** User id **/
    @DatabaseField(generatedId = true)
    private Integer id;

    /** The username **/
    @DatabaseField(canBeNull = false, columnName = USERNAME_COLUMN)
    private String username;

    /** The resulting hash of the password **/
    @DatabaseField(canBeNull = false, columnName = PASSWORD_COLUMN)
    private String password;

    /** The user's name **/
    @DatabaseField(canBeNull = false)
    private String name;

    /** User role **/
    @DatabaseField(canBeNull = false, dataType = DataType.ENUM_STRING)
    private Role role;

    /** Active user **/
    @DatabaseField(canBeNull = false, defaultValue = "false", columnName = ACTIVE_COLUMN)
    private Boolean active;

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
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @return password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
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
     * @return the role
     */
    public Role getRole() {
        return role;
    }

    /**
     * @return role the role to set
     */
    public void setRole(Role role) {
        this.role = role;
    }

    /**
     * @return the active
     */
    public Boolean getActive() {
        return active;
    }

    /**
     * @return active the active to set
     */
    public void setActive(Boolean active) {
        this.active = active;
    }
}
