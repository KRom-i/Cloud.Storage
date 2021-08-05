package TransportPackage;

import java.io.File;
import java.util.LinkedHashMap;


public class TransportPackageMap {

    private LinkedHashMap<Integer, TransportPackage> packageLinkedHashMap;

    public TransportPackageMap () {
        this.packageLinkedHashMap = new LinkedHashMap<>();
    }

    public void add(TransportPackage transportPackage){
        packageLinkedHashMap.put(transportPackage.getId(), transportPackage);
    }

    public void add(TransportPackage transportPackage, String path){

        initPath(transportPackage, path);

        packageLinkedHashMap.put(transportPackage.getId(), transportPackage);
    }

    public void replace (TransportPackage tranPackDown) {

        int id = tranPackDown.getId();

        TransportPackage tranPack = packageLinkedHashMap.get(id);

        String path = tranPack.getPathDownload();

        initPath(tranPackDown, path);

        packageLinkedHashMap.replace(tranPackDown.getId(), tranPackDown);

    }

    public TransportedFile get(String[] msg){

        int idPack = Integer.parseInt(msg[0]);
        int idFile = Integer.parseInt(msg[1]);

        return packageLinkedHashMap.get(idPack).getTransportedFile(idFile);
    }

    public int getSize () {
        return packageLinkedHashMap.size();
    }

    public TransportPackage getPack (boolean upload) {

        try {

            for (int i = 0; i < packageLinkedHashMap.size(); i++) {

                TransportPackage transportPackageCheck = packageLinkedHashMap.get(i + 1);

                if (!transportPackageCheck.isLoaded()
                        && upload == transportPackageCheck.isUpload()){

                    return transportPackageCheck;
                } ;

            }

        } catch (Exception e){
            e.printStackTrace();
        } finally {
        }

        return null;
    }

    public TransportedFile getNotRead(){

        for (int pack = 0; pack < packageLinkedHashMap.size(); pack ++) {

            for (int file = 0; file < packageLinkedHashMap.get(pack + 1).getNumFiles(); file ++) {

                TransportedFile transportedFile = packageLinkedHashMap.get(pack + 1).getTransportedFile(file + 1);

                if (transportedFile.getSize() > transportedFile.getByteRead()) return transportedFile;

            }

        }

        return null;
    }

    private void initPath (TransportPackage transportPackage, String path) {

        if (transportPackage.isDirectory()){

            for (int i = 0; i < transportPackage.getNumFiles(); i++) {

                TransportedFile transportedFile = transportPackage.getTransportedFile(i + 1);

                if (transportedFile.isMkdirs()){

                    File dir = new File(path + File.separator + transportPackage.getNamePack() +
                                                File.separator + transportedFile.getPathDirs());

                    if (!dir.exists()) dir.mkdirs();


                    transportedFile.setFile(new File(dir.getAbsolutePath() + File.separator + transportedFile.getFileName()));

                } else {
                    File dir = new File(path + File.separator + transportPackage.getNamePack());

                    if (!dir.exists()) dir.mkdirs();

                    transportedFile.setFile(new File(dir.getAbsolutePath()  + File.separator + transportedFile.getFileName()));

                }

            }


        } else {
            TransportedFile transportedFile =transportPackage.getTransportedFile(1);
            transportedFile.setFile(new File(path + File.separator + transportPackage.getNamePack()));
        }

    }


    @Override
    public String toString () {
        return "TransportPackageMap{" +
                ", packageLinkedHashMap=" + packageLinkedHashMap +
                '}';
    }
}
