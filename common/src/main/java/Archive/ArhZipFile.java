package Archive;


import FileManager.*;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Objects;

public class ArhZipFile {

    public ArhZipFile () {
    }

    public File getZipFiles(File file){

        File zip = null;

            File fileSource = file.getAbsoluteFile();

            String absolutePathDir = fileSource.getParent();

            if (fileSource.isDirectory()){

                zip = getZipDirectory(absolutePathDir, null, fileSource.getName(), fileSource.listFiles());

            } else {
                String name = new FileManager().getFileNameNotExtension(fileSource);
                zip = getZipFile(absolutePathDir, name, fileSource);

            }


        return zip;
    }


    public void packUser (File dir, String nameFile, File fileSource) {

        if (fileSource.isDirectory()){
            getZipDirectory(fileSource.getAbsolutePath(),  dir.getAbsolutePath(), nameFile, fileSource.listFiles());

        } else {
            getZipFile(dir.getAbsolutePath(), nameFile, fileSource);
        }

    }


    private File getZipFile (String absolutePathDir, String nameZip, File fileSource) {

        File fileZip = new File(absolutePathDir + File.separator + nameZip + ".ZIP");

        try (ZipOutputStream zOut = new ZipOutputStream(new FileOutputStream(fileZip));
             FileInputStream fis = new FileInputStream(fileSource)) {

            ZipEntry entry1 = new ZipEntry(fileSource.getName());

            zOut.putNextEntry(entry1);

            byte[] buffer = new byte[8 * 1024];
            int length;

            while ((length = fis.read(buffer)) > 0)
                zOut.write(buffer, 0, length);

            zOut.write(buffer);
            zOut.closeEntry();


        } catch (Exception e) {
            e.printStackTrace();
        }

        return fileZip;
    }

    public File getZipDirectory (String absolutePathDir, String pathZip, String nameZip, File[] listFile) {

        File fileZip = new File(absolutePathDir + File.separator + nameZip + ".ZIP");

        if (pathZip != null ) fileZip = new File(pathZip + File.separator + nameZip + ".ZIP");

        try (FileOutputStream fout = new FileOutputStream(fileZip);
             ZipOutputStream zout = new ZipOutputStream(fout);) {

            addZipDirectory(absolutePathDir, zout, listFile);

        } catch (Exception e) {
            e.printStackTrace();
        }


        return fileZip;
    }

    private void addZipDirectory (String absolutePathDir, ZipOutputStream zout, File[] fileArchive) throws Exception {

        for(int i = 0; i < fileArchive.length; i++) {

            if(fileArchive[i].isDirectory()) {

                addZipDirectory(absolutePathDir, zout, Objects.requireNonNull(fileArchive[i].listFiles()));
                continue;
            }

            WalkFile wf = new WalkFile(absolutePathDir, fileArchive[i].getAbsolutePath());

            zout.putNextEntry(new ZipEntry(wf.getRelativePath()));

            FileInputStream fis = new FileInputStream(fileArchive[i]);

            byte[] buffer = new byte[8 * 1024];
            int length;

            while ((length = fis.read(buffer)) > 0)
                zout.write(buffer, 0, length);

            zout.closeEntry();
            fis.close();
        }

    }


}
