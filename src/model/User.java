package model;

import java.io.File;

public class User {

    private String name;
    private File inboxFile;
    public User(String n,File f){
        name=n;
        inboxFile=f;
        inboxFile.mkdir();
    }

    public File getInboxFile() {
        return inboxFile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void deleteUserData(){
        inboxFile.delete();
        name=null;
        inboxFile=null;
    }

}
