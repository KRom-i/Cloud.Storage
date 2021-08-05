package FileManager;


import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;


public class FileManager {

    public String getParentCurrentDirectory(String currentDirectory) {

        String parentPath = "";

        if(currentDirectory.length() > 1) {

        String[] paths = currentDirectory.replaceAll("\\\\", ":").split(":");

        if(paths.length > 1) {

            for(int i = 0; i < paths.length - 1; i++) {
                parentPath += paths[i] + File.separator;
            }

        }

    }
        return parentPath;
    }

    public boolean checkDirectory (String path) {
        File dir = new File(path);
        if (dir.exists()) return dir.isDirectory();
        return false;
    }

    public void createFile (String path) {

        String checkFile = checkFile(path);

        if (checkFile != null) {
            try {
                Files.createFile(Paths.get(checkFile));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    public void createDir (String path) {

        String checkFile = checkFile(path);
        if (checkFile != null) {

            try {
                Files.createDirectories(Paths.get(checkFile));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean rename (File srcFile, String newName) {

        String checkFile = checkFile(srcFile.getParent() + File.separator + newName);
        if (checkFile != null) {
            File destFile = new File(checkFile);
            return srcFile.renameTo(destFile);
        }
        return false;
    }

    public void copyFiles (String target, File... files) {

        List<WalkFile> walkFiles = getWalkFiles(files);

        for(WalkFile wf: walkFiles
            ) {

            Path src = Paths.get(wf.getAbsolutePathVisitFile());
            Path tar = Paths.get(target + File.separator + wf.getRelativePath());

            if (tar.toFile().exists()){
                tar = Paths.get(getCopyNameFullPath(wf.getRelativePath(), target));
            };

            copy(src, tar);
        }
    }

    public String checkFile(String path){

        File file = new File(path);

        if (file.getParentFile().exists()) {

            if(file.exists()) return getCopyNameFullPath(file.getName(), file.getParentFile().getAbsolutePath());

            return path;
        }

        return null;
    }

    private String getCopyNameFullPath(String name, String target){

        int numCopy = 1;

        while (true){
            String nameCopy = "Копия-" + numCopy + " " + name;
            Path tar = Paths.get(target + File.separator + nameCopy);
            if (!tar.toFile().exists()){
                return tar.toString();
            }
            numCopy ++;
        }
    }

    public void createFileNotCheckCopyName(String path, String name){

        File file = new File(path);
        if (file.exists()) {
            try {
                Files.createFile(Paths.get(file.getAbsolutePath(), name));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void copy(Path src, Path target){
        try {
            Files.copy(src, target);
        } catch (
                IOException e) {
        }
    }

    public void deleteFiles (File... files) {
        List<WalkFile> wf = getWalkFiles(files);
        for(int i = wf.size() - 1; i > -1; i--) delete(Paths.get(wf.get(i).getAbsolutePathVisitFile()));
    }

    private void delete(Path path){
        try {
            if (path.toFile().exists()) Files.delete(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<WalkFile> getWalkFiles (File... files) {

        List<WalkFile> walkFiles = new ArrayList<>();

        for(File f : files
        ) {

            if(f.isDirectory()) {

                try {
                    Files.walkFileTree(f.toPath(), new SimpleFileVisitor<Path>() {

                        @Override
                        public FileVisitResult visitFile (Path file, BasicFileAttributes attrs) {
                            WalkFile walkFile = new WalkFile(f.getAbsolutePath(), file.toAbsolutePath().toString());
                            walkFiles.add(walkFile);
                            return FileVisitResult.CONTINUE;
                        }

                        @Override
                        public FileVisitResult preVisitDirectory (Path dir, BasicFileAttributes attrs) {
                            WalkFile walkFile = new WalkFile(f.getAbsolutePath(), dir.toAbsolutePath().toString());
                            walkFiles.add(walkFile);
                            return FileVisitResult.CONTINUE;
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                WalkFile walkFile = new WalkFile(f.getAbsolutePath(), f.getAbsolutePath());
                walkFiles.add(walkFile);
            }
        }


        return walkFiles;
    }

    public String[] getListRootsSystem () {

        File[] files = File.listRoots();

        String[] roots = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            roots[i] = files[i].toString();

        }

        return roots;
    }

    public String getFileNameNotExtension (File file) {

        String fileName = file.getName();

        if (fileName.indexOf(".") > 0) {
            return fileName.substring(0, fileName.lastIndexOf("."));
        } else {
            return fileName;
        }

    }

    public String getFileExtension (File file) {

        String fileName = file.getName();

        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0){
            return fileName.substring(fileName.lastIndexOf(".")+1);

        } else {
            return "";
        }

    }


    public long getSizeFile(String path){

        long size = 0;

        List<WalkFile> walkFiles = getWalkFiles(new File(path));

        for (WalkFile wf: walkFiles
             ) {

            if (!wf.isDirectory()) size += wf.getFile().length();
        }

        return size;
    }


    public String getCopyName (String absolutePath) {
        return new File(checkFile(absolutePath)).getName();
    }
}
