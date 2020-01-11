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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void deleteInboxFile(){
        inboxFile.delete();
    }
}
