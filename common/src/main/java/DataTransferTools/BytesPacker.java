package DataTransferTools;

import Commands.Commands;
import TransportPackage.*;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

import static Address.Address.*;

public class BytesPacker {

    public static byte[] getPack(byte address1, byte address2, String message){

        try(ByteArrayOutputStream byteArray = new ByteArrayOutputStream()){

            byteArray.write(address1);

            byteArray.write(address2);

            byte[] data = message.getBytes(StandardCharsets.UTF_8);

            byteArray.write(BinarySystem.integerEncoder(data.length));

            byteArray.write(data);

            return byteArray.toByteArray();

        } catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public static byte[] getPack(int type, String message){

        try(ByteArrayOutputStream byteArray = new ByteArrayOutputStream()){

            byteArray.write(BinarySystem.integerEncoder(type));

            byte[] data = message.getBytes(StandardCharsets.UTF_8);

            byteArray.write(BinarySystem.integerEncoder(data.length));

            byteArray.write(data);

            return byteArray.toByteArray();

        } catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public static byte[] getPack(byte[] address, byte[] data){

        try(ByteArrayOutputStream byteArray = new ByteArrayOutputStream()){

            byteArray.write(address);

            byteArray.write(BinarySystem.integerEncoder(data.length));

            byteArray.write(data);

            return byteArray.toByteArray();

        } catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public static byte[] getPack(int type, byte[] data){

        try(ByteArrayOutputStream byteArray = new ByteArrayOutputStream()){

            byteArray.write(BinarySystem.integerEncoder(type));

            byteArray.write(BinarySystem.integerEncoder(data.length));

            byteArray.write(data);

            return byteArray.toByteArray();

        } catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public static BytePack getPackUpload(TransportPackage transportPackage) throws CloneNotSupportedException {

       TransportPackage outTransportPackage = transportPackage.getClone();

        byte[] data = Serializer.serialize(outTransportPackage);

        return new BytePack(LOAD, PACK_UP,null, data);

    }

    public static BytePack getPackDownload(TransportPackage transportPackage) throws CloneNotSupportedException {

        TransportPackage outTransportPackage = transportPackage.getClone();

        byte[] data = Serializer.serialize(outTransportPackage);

        return new BytePack(LOAD, PACK_DOWN,null, data);

    }

    public static BytePack getPackUploadFile(TransportedFile transportedFile, byte[] data){

        int idPack = transportedFile.getIdPack();
        int idFile = transportedFile.getId();

        String msg = Commands.BUILD(1, String.valueOf(idPack),String.valueOf(idFile));

        return new BytePack(LOAD, DATA_UP, msg, data);
    }


}
