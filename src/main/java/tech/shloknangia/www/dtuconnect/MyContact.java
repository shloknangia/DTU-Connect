package tech.shloknangia.www.dtuconnect;

import java.io.Serializable;

/**
 * Created by SHLOK on 16-04-2017.
 */
public class MyContact implements Serializable{
    public String doc_id;
    public String name;
    public String email;
    public String branch;
    public String course;
    public String year;
    public String bloodgroup;
    public String phone;
    public String password;

    public String getBranch() {
        return branch;
    }

    public void setBranch(String s) {
        this.branch = s;
    }

    public void setYear(String y) {
        this.year = y;
    }

    public String getYear() {
        return year;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String s) {
        this.course = s;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String s) {
        password = s;
    }

    public String getBlood_Group() {
        return bloodgroup;
    }

    public void setBlood_Group(String s) {
        this.bloodgroup = s;
    }

    public String getDoc_id() {
        return doc_id;
    }

    public void setDoc_id(String doc_id) {
        this.doc_id = doc_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String s) {
        name = s;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
