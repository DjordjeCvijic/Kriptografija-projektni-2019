package listeners;


import java.io.*;
import java.nio.file.*;
import java.nio.file.WatchEvent.Kind;

import controller.HomeScreenController;

public class UserInboxListener extends Thread {

    private Path directoryPath = null;
    private boolean running = false;

    private boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public UserInboxListener(File file) {

        directoryPath = file.toPath();
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
                    takeActionOnChangeEvent(event);
                }

                if (!watchKey.reset()) {
                    watchKey.cancel();
                    watchService.close();
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void takeActionOnChangeEvent(WatchEvent<?> event) {

        Kind<?> kind = event.kind();
        try {
            sleep(300);

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (kind.equals(StandardWatchEventKinds.ENTRY_CREATE)) {
            Path entryCreated = (Path) event.context();
            try {
                byte[] messageInB = Files.readAllBytes(new File(directoryPath.toString() + File.separator + entryCreated).toPath());
                String tmp = new String(messageInB);
                if (tmp.contains("image")) {
                    HomeScreenController.imagePath(tmp);
                } else if (tmp.contains(":reply=yes")) {
                    HomeScreenController.replyIsYes();
                } else if (tmp.contains(":reply=no")) {
                    HomeScreenController.replyIsNo();
                } else {
                    HomeScreenController.messageHasBeenRead(tmp);
                }

                PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(directoryPath.toString() + File.separator + entryCreated)));
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            new File(directoryPath.toString() + File.separator + entryCreated).delete();
        }
    }
}
