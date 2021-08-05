package Files;

import FileManager.FileManager;
import FileManager.WalkFile;
import GraphicalIinterface.CommonСlassesGUI.ListFiles.FileInfo;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SearchFiles {

        public List<FileInfo> searchFile(String path, String fileName){

        List<FileInfo> returnListFiles = new ArrayList<>();

        List<WalkFile> walkFiles = new FileManager().getWalkFiles(new File(path));

        for(WalkFile wf: walkFiles) {

            if (wf.getFullName().equalsIgnoreCase(fileName)) {
                returnListFiles.add(new FileInfo(wf.getFile(), true));
            }
        }

        if (returnListFiles.isEmpty()) return null;

        return returnListFiles;
    }


}
