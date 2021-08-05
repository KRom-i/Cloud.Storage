package TransportPackage;

import java.io.File;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedHashMap;

public class RunFile {

    private File fileResource;
    private int idPack;

    public RunFile (File fileResource, int idPack) {
        this.fileResource  = fileResource;
        this.idPack = idPack;
    }


    public LinkedHashMap<Integer, TransportedFile> getTransportedFiles(){

        LinkedHashMap<Integer, TransportedFile> linkedHashMap = new LinkedHashMap<>();

        try {
            Files.walkFileTree(fileResource.toPath(), new SimpleFileVisitor<Path>() {

                @Override
                public FileVisitResult visitFile (Path file, BasicFileAttributes attrs) {

                    String pathDirs = getRelativePathDirs(fileResource, file.toFile());

                    int id = linkedHashMap.size() + 1;

                    TransportedFile transportedFile = new TransportedFile(file.toFile(), pathDirs, id, idPack);

                    linkedHashMap.put(transportedFile.getId(), transportedFile);

                    return FileVisitResult.CONTINUE;
                }


            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        return linkedHashMap;
    }


    private String getRelativePathDirs (File fileResource, File fileInside) {

        String[] pathFileResource = fileResource.getAbsolutePath().split("\\\\");

        String[] pathFileInside = fileInside.getAbsolutePath().split("\\\\");

        String relativePath = "";

        if (pathFileResource.length == pathFileInside.length - 1) return null;

        for (int i = pathFileResource.length; i < pathFileInside.length - 1; i++) {

            relativePath += pathFileInside[i];

            if (i != pathFileInside.length - 2) relativePath += File.pathSeparator;

        }

        return relativePath;
    }



}
