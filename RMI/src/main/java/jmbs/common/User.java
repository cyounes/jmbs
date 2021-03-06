/**
 * JMBS: Java Micro Blogging System
 *
 * Copyright (C) 2012
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY. See the GNU General Public License for more details. You should
 * have received a copy of the GNU General Public License along with this
 * program. If not, see <http://www.gnu.org/licenses/>.
 *
 * @author Younes CHEIKH http://cyounes.com
 * @author Benjamin Babic http://bbabic.com
 *
 */
package jmbs.common;

import java.io.Serializable;
import java.util.ArrayList;

// TODO: create jdoc explanations like in jmbs.common.Project 
/**
 * Represents a user.
 */
public class User implements Serializable {

    private static final long serialVersionUID = -379020318303370555L;
    private String name;
    private String fname;
    private String mail;
    private int id;
    // these attributes are not created by default because they are mostly
    // unused or could trigger unwanted chained db access and object creation
    private int accesslevel;
    private ArrayList<Project> projects = new ArrayList<Project>();
    private ArrayList<User> follows = new ArrayList<User>();
    private byte[] pic;

    public User() {
        this.id = -1;
    }

    /**
     * Creates a user from given informations. Some attributes are not created
     * by default because they are mostly unused or could trigger unwanted
     * chained database access and chained object creation. Which would make
     * this object to heavy to be transfered in reasonable time.
     *
     * @param n user's name
     * @param f user's fore name
     * @param m user's mail
     */
    public User(String n, String f, String m) {
        this.name = n;
        this.fname = f;
        this.mail = m;
        this.id = 0;
    }

    /**
     * Creates a user with his id.
     *
     * @deprecated pic is no more a string
     * @param n user's name
     * @param f user's fore name
     * @param m user's mail
     * @param id user's id
     */
    public User(String n, String f, String m, int id, String picName,
            int authlvl) {
        this.name = n;
        this.fname = f;
        this.mail = m;
        this.id = id;
        this.pic = null;
        this.accesslevel = authlvl;
    }
    
        /**
     * Creates a user with his id.
     *
     * @param n user's name
     * @param f user's fore name
     * @param m user's mail
     * @param id user's id
     */
    public User(String n, String f, String m, int id, byte[] pic, int authlvl) {
        this.name = n;
        this.fname = f;
        this.mail = m;
        this.id = id;
        this.pic = pic;
        this.accesslevel = authlvl;
    }
    
    /**
     * Creates a user with his id.
     *
     * @param n user's name
     * @param pic byte[] picture
     * @param id user's id
     */
    public User(int id, String n, String f,  byte[] pic) {
        this.name = n;
        this.id = id;
        this.pic = pic;
        this.fname = f;
    }

    /*
     * (non-Javadoc) 2 users are not even comparable if they have no id given by
     * the DB. If one of the 2 users have a id equal to 0, result will be false
     * anyways.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        User other = (User) obj;
        // TODO: more advanced equality check
        if (id != other.id || id <= 0 || other.id == 0) {
            return false;
        }
        return true;
    }

    /**
     * @return the access level
     */
    public int getAccesslevel() {
        return accesslevel;
    }

    /**
     * @return the followed users
     */
    public ArrayList<User> getFollows() {
        return follows;
    }

    /**
     * @return the fore name
     */
    public String getFname() {
        return fname;
    }

    public String getFullName() {
        return (name + " " + fname);
    }

    /**
     * @return the User id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the email
     */
    public String getMail() {
        return mail;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    public byte[] getPic() {
        return pic;
    }

    /**
     * @return the projects // TODO: more advanced equality check
     */
    public ArrayList<Project> getProjects() {
        return projects;
    }

    /**
     * @param accesslevel the access level to set
     */
    public void setAccesslevel(int accesslevel) {
        this.accesslevel = accesslevel;
    }

    /**
     * @param follows the followed users to set
     */
    public void setFollows(ArrayList<User> follows) {
        this.follows = follows;
    }

    /**
     * @param fname the fore name to set
     */
    public void setFname(String fname) {
        this.fname = fname;
    }

    /**
     * @param idUser the User id to set
     */
    public void setId(int idUser) {
        this.id = idUser;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param mail the email to set
     */
    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setPic(byte[] pic) {
        this.pic = pic;
    }

    /**
     * @param projects the projects to set
     */
    public void setProjects(ArrayList<Project> projects) {
        this.projects = projects;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "\nName: " + name + "\nForename: " + fname + "\nEmail: " + mail
                + "\n";
    }
}
