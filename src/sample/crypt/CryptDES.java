package sample.crypt;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;

public class CryptDES {
    public static String encrypt(String text){

        String textToHex = HexStringConverter.getHexStringConverterInstance().stringToHex(text);

        try {
            byte[] theKey = null;
            byte[] theIVp = null;
            byte[] theMsg = null;
            String algorithm = null;

            algorithm = "DES/OFB/NoPadding";
            theKey = hexToBytes("0123456789ABCDEF");
            theIVp = hexToBytes("1234567890ABCDEF");
            theMsg = hexToBytes(
                    textToHex);

            KeySpec ks = new DESKeySpec(theKey);
            SecretKeyFactory kf
                    = SecretKeyFactory.getInstance("DES");
            SecretKey ky = kf.generateSecret(ks);
            Cipher cf = Cipher.getInstance(algorithm);
            if (theIVp == null) {
                cf.init(Cipher.ENCRYPT_MODE, ky);
            } else {
                AlgorithmParameterSpec aps = new IvParameterSpec(theIVp);
                cf.init(Cipher.ENCRYPT_MODE, ky, aps);
            }
            byte[] theCph = cf.doFinal(theMsg);
            return bytesToHex(theCph);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Error";
    }

    public static String decrypt(String text){
        try {
            byte[] theKey = null;
            byte[] theIVp = null;
            byte[] theMsg = null;
            String algorithm = null;

            algorithm = "DES/OFB/NoPadding";
            theKey = hexToBytes("0123456789ABCDEF");
            theIVp = hexToBytes("1234567890ABCDEF");
            theMsg = hexToBytes(
                    text);

            KeySpec ks = new DESKeySpec(theKey);
            SecretKeyFactory kf
                    = SecretKeyFactory.getInstance("DES");
            SecretKey ky = kf.generateSecret(ks);
            Cipher cf = Cipher.getInstance(algorithm);
            if (theIVp == null) {
                cf.init(Cipher.DECRYPT_MODE, ky);
            } else {
                AlgorithmParameterSpec aps = new IvParameterSpec(theIVp);
                cf.init(Cipher.DECRYPT_MODE, ky, aps);
            }
            byte[] theCph = cf.doFinal(theMsg);

            return HexStringConverter.getHexStringConverterInstance().hexToString(bytesToHex(theCph));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Error";
    }

    public static byte[] hexToBytes(String str) {
        if (str==null) {
            return null;
        } else if (str.length() < 2) {
            return null;
        } else {
            int len = str.length() / 2;
            byte[] buffer = new byte[len];
            for (int i=0; i<len; i++) {
                buffer[i] = (byte) Integer.parseInt(
                        str.substring(i*2,i*2+2),16);
            }
            return buffer;
        }
    }
    public static String bytesToHex(byte[] data) {
        if (data==null) {
            return null;
        } else {
            int len = data.length;
            String str = "";
            for (int i=0; i<len; i++) {
                if ((data[i]&0xFF)<16) str = str + "0"
                        + java.lang.Integer.toHexString(data[i]&0xFF);
                else str = str
                        + java.lang.Integer.toHexString(data[i]&0xFF);
            }
            return str.toUpperCase();
        }
    }
}
