package SayisalImza;

import javax.xml.bind.DatatypeConverter;
import java.security.*;
import java.util.Scanner;

public class SayisalImza {
    private static final String IMZA_ALGORITMASI="SHA256withRSA";
    private static final String RSA="RSA";

    public static byte[] sayisalImzaOlustur(byte[] giris, PrivateKey ozelAnahtar)throws Exception{
        Signature signature=Signature.getInstance(IMZA_ALGORITMASI);
        signature.initSign(ozelAnahtar);
        signature.update(giris);
        return signature.sign();
    }
    public static KeyPair rsaAnahtariOlustur() throws Exception{
        SecureRandom secureRandom=new SecureRandom();
        KeyPairGenerator keyPairGenerator=KeyPairGenerator.getInstance(RSA);
        keyPairGenerator.initialize(1024,secureRandom);
        return keyPairGenerator.generateKeyPair();
    }
    public static boolean sayisalImzaDogrula(byte[] giris,byte[] imzayiDogrula,PublicKey acikAnahtar) throws Exception{
        Signature signature=Signature.getInstance(IMZA_ALGORITMASI);
        signature.initVerify(acikAnahtar);
        signature.update(giris);
        return signature.verify(imzayiDogrula);
    }

    public static void main(String[] args) throws Exception {
        Scanner sc=new Scanner(System.in);
        System.out.println("İmzalamak istediğiniz metni giriniz: ");
        String veri=sc.nextLine();
        KeyPair keyPair=rsaAnahtariOlustur();
        byte[] imza= sayisalImzaOlustur(veri.getBytes(),keyPair.getPrivate());
        System.out.println("Metin başarılı bir şekilde imzalanmıştır...");
        System.out.println("Açık metin: "+veri);
        System.out.println("İmza: "+ DatatypeConverter.printHexBinary(imza));
        while(true) {
            System.out.println("Doğrulama yapmak için metni giriniz: ");
            String dogrulanacakVeri=sc.nextLine();
            System.out.println("Doğrulama yapılacak imzayı giriniz: ");
            String dogrulanacakImza=sc.nextLine();
            byte[] dogrulanacakImzaArray = new byte[imza.length];
            int j=0;
            for (int i=0;i<dogrulanacakImza.length();i+=2){
                String hex=dogrulanacakImza.substring(i,i+2);
                int decimal=Integer.parseInt(hex,16);
                dogrulanacakImzaArray[j]= (byte) decimal;
                j++;
            }
                if (sayisalImzaDogrula(dogrulanacakVeri.getBytes(), dogrulanacakImzaArray, keyPair.getPublic())){
                    System.out.println("İmza kimliği doğrulandı. Veri bütünlüğü sağlanmıştır.");
                    break;
                }
                else {
                    System.out.println("İmza kimliği doğrulanamadı. Veri bütünlüğü sağlanamamıştır.");

                }
        }
    }
}
