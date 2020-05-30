package listeners;


import java.io.*;
import java.nio.file.*;
import java.nio.file.WatchEvent.Kind;
import java.util.logging.Level;

import controller.HomeScreenController;

public class UserInboxListener extends Thread {

    private Path directoryPath = null;//(new File("src" + File.separator + "resources" + File.separator + "inboxes")).toPath();
    private String userName;
    private boolean running = false;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public UserInboxListener(File file) {

       directoryPath=file.toPath();
    }

    @Override
    public void run() {
        try {
            setRunning(true);

            WatchService watchService = directoryPath.getFileSystem().newWatchService();
            directoryPath.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);


            while (isRunning()) {

                WatchKey watchKey = watchService.take();


                for (final WatchEvent<?> event : watchKey.pollEvents()) {
                    //Calling method
                    takeActionOnChangeEvent(event);
                }

                //Break out of the loop if watch directory got deleted
                if (!watchKey.reset()) {
                    watchKey.cancel();
                    watchService.close();
                    //Break out from the loop
                    break;
                }
            }
           // System.out.println("user zavrsio");


        } catch (InterruptedException e) {

            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void takeActionOnChangeEvent(WatchEvent<?> event) {

        Kind<?> kind = event.kind();

        try{
            sleep(300);

        }catch (Exception e){
            e.printStackTrace();
        }

        if (kind.equals(StandardWatchEventKinds.ENTRY_CREATE)) {
            Path entryCreated = (Path) event.context();
            try{
                //BufferedReader in=new BufferedReader(new FileReader(directoryPath.toString()+File.separator+entryCreated));
                //String tmp=in.readLine();
                byte[] messageInB = Files.readAllBytes(new File(directoryPath.toString()+File.separator+entryCreated).toPath());
                String tmp=new String(messageInB);

                System.out.println(directoryPath+" u lisineru poruka     "+tmp);
                if(tmp.contains("request")) {
                    HomeScreenController.request(tmp);
                }else if(tmp.contains(":reply=yes")){
                    HomeScreenController.replyIsYes();
                }else if(tmp.contains(":reply=no")){
                    HomeScreenController.replyIsNo();
                }
                else{
                    HomeScreenController.messageHasBeenRead(tmp);
                }

                PrintWriter out=new PrintWriter(new BufferedWriter(new FileWriter(directoryPath.toString()+File.separator+entryCreated)));
                out.close();
            }catch (Exception e){
                e.printStackTrace();
            }
            new File(directoryPath.toString()+File.separator+entryCreated).delete();




        }
    }
}
