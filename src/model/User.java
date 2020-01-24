package model;

import java.io.File;

public class User {

    private String name;
    private File inboxDirectory;
    public User(String n,File f){
        name=n;
        inboxDirectory=f;
        inboxDirectory.mkdir();
    }

    public File getInboxDirectory() {
        return inboxDirectory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void deleteUserData(){
        inboxDirectory.delete();
        name=null;
        inboxDirectory=null;
    }

}
