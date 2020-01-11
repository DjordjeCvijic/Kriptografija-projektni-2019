package main;



import java.nio.file.*;
import java.nio.file.WatchEvent.Kind;
import java.util.logging.Level;
import controller.HomeScreenController;

public class JavaDirectoryChangeListener extends Thread {

    private Path directoryPath;
    private String name;

    public JavaDirectoryChangeListener(Path dir) {
        directoryPath = dir;
    }

    @Override
    public void run() {
        try {
            WatchService watchService = directoryPath.getFileSystem().newWatchService();
            directoryPath.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);

            //Start infinite loop to watch changes on the directory
            while (true) {

                WatchKey watchKey = watchService.take();

                // poll for file system events on the WatchKey
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

            int index=entryCreated.toString().indexOf('_');
            String newUser=entryCreated.toString().substring(0,index);
            HomeScreenController.addActiveUser(newUser);
            System.out.println("dodat "+newUser);


        } else if (kind.equals(StandardWatchEventKinds.ENTRY_DELETE)) {
            Path entryDeleted = (Path) event.context();
            int index=entryDeleted.toString().indexOf('_');
            String newUser=entryDeleted.toString().substring(0,index);
            HomeScreenController.removeActiveUser(newUser);
            System.out.println("obrisan "+newUser);

        } else if (kind.equals(StandardWatchEventKinds.ENTRY_MODIFY)) {
            Path entryModified = (Path) event.context();

        }
    }
}
