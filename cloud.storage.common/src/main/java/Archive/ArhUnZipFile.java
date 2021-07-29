package Archive;


import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;

public class ArhUnZipFile {

    private final String SLASH_BACK = "/";

    public File[] getUnzipFiles(String path, File... files){

        path = new File(path).getAbsolutePath();
        File[] unzip = new File[files.length];

        for(int i = 0; i < unzip.length; i++) {
            unzip[i] = getUnzipFile(path, files[i]);
        }

        return unzip;
    }

    public File getUnzipFile (String absolutePath, File file) {

        try (ZipFile zipFile = new ZipFile(file)) {

            Enumeration<?> entries = zipFile.getEntries();

            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                String entryName = entry.getName();

                if(entryName.endsWith(SLASH_BACK)) {
                    createFolder(absolutePath + File.separator + entryName);
                    continue;
                } else

                    checkFolder(absolutePath + File.separator + entryName);

                try (InputStream fis = (InputStream) zipFile.getInputStream(entry);
                     FileOutputStream fos = new FileOutputStream(absolutePath + File.separator + entryName);) {

                    int read = 0;
                    byte[] buffer = new byte[8 * 1024];

                    while ((read = fis.read(buffer)) != -1) {
                        fos.write(buffer, 0, read);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void createDir (final String dir) {
        File file = new File(dir);
        if(!file.exists())
            file.mkdirs();
    }

    private void createFolder (final String dirName) {
        if(dirName.endsWith(SLASH_BACK))
            createDir(dirName.substring(0, dirName.length() - 1));
    }

    private void checkFolder (final String file_path) {
        if(!file_path.endsWith(SLASH_BACK) && file_path.contains(SLASH_BACK)) {
            String dir = file_path.substring(0, file_path.lastIndexOf(SLASH_BACK));
            createDir(dir);
        }
    }
}