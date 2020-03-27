package listeners;


import java.io.File;
import java.nio.file.*;
import java.nio.file.WatchEvent.Kind;
import java.util.logging.Level;

import controller.HomeScreenController;

public class JavaInboxesListener extends Thread {

    private Path directoryPath = (new File("src" + File.separator + "resources" + File.separator + "inboxes")).toPath();
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

    public JavaInboxesListener(String name) {

        userName = name;
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
            System.out.println(" java zavrsio");


        } catch (InterruptedException e) {

            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void takeActionOnChangeEvent(WatchEvent<?> event) {

        Kind<?> kind = event.kind();

        if (kind.equals(StandardWatchEventKinds.ENTRY_CREATE)) {
            Path entryCreated = (Path) event.context();

            int index = entryCreated.toString().indexOf('_');
            String newUser = entryCreated.toString().substring(0, index);
            HomeScreenController.addActiveUser(newUser);


        } else if (kind.equals(StandardWatchEventKinds.ENTRY_DELETE)) {
            Path entryDeleted = (Path) event.context();
            int index = 0;

            if (!entryDeleted.toString().equals(userName + "_inbox")) {
                index = entryDeleted.toString().indexOf('_');
                String deletedUser = entryDeleted.toString().substring(0, index);
                HomeScreenController.removeActiveUser(deletedUser);

            }

        }
    }
}
