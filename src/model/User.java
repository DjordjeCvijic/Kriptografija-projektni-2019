package model;

import java.io.File;

public class User {

    private String name;
    private File inboxDirectory;
    private String trustStorePath;
    private String keyStorePath;
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

    public String getTrustStorePath() {
        return trustStorePath;
    }

    public void setTrustStorePath(String trustStore) {
        this.trustStorePath = trustStore;
    }

    public String getKeyStorePath() {
        return keyStorePath;
    }

    public void setKeyStorePath(String keyStore) {
        this.keyStorePath = keyStore;
    }
}
