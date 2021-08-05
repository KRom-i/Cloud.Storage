package DataTransferTools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Serializer {

    public static byte[] serialize(Object obj) {

        try(ByteArrayOutputStream b = new ByteArrayOutputStream()){

            try(ObjectOutputStream o = new ObjectOutputStream(b)){
                o.writeObject(obj);
            }

            return b.toByteArray();

        } catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public static Object deserialize(byte[] bytes)  {

        try(ByteArrayInputStream byteArr = new ByteArrayInputStream(bytes)){

            try(ObjectInputStream object = new ObjectInputStream(byteArr)){

                return object.readObject();
            }

        } catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

}