package SifreliHaberlesme;

import java.math.BigInteger;
import java.util.Scanner;

public class RSA {
    private String paylasilanAnahtar;

    public String getPaylasilanAnahtar() {
        return paylasilanAnahtar;
    }

    public void setPaylasilanAnahtar(String paylasilanAnahtar) {
        this.paylasilanAnahtar = paylasilanAnahtar;
    }


    public int gcd(int e, int z)
    {
        if (e == 0)
            return z;
        else
            return gcd(z % e, e);
    }

    //anahtar üretimi
    public String anahtarUret(){
        int p,q,o,e,d,n;
        p=3;
        q=37;
        n=p*q;
        o=(p-1)*(q-1);

        //Anahtar üretimi

        //1<e<o e ve o aralarında asal
        for (e = 2; e < o; e++) {
            // e açık anahtarı
            if (gcd(e, o) == 1) {
                break;
            }
        }

        d=0;
        for (int i = 0; i <= 9; i++) {
            int x = 1 + (i * o);

            // d özel anahtar
            if (x % e == 0) {
                d = x / e;
                break;
            }
        }

        //Anahtar üretimi bitimi
        return sifrele(n,e,d);
    }
    //şifreleme
    public String sifrele(int n, int e,int d){
        System.out.println("Açık anahtar şifreleme");

        //Sifreleme baslangıcı
        Scanner sc=new Scanner(System.in);
        String message;
        do{
        System.out.print("Anahtar paylaşımı için şifrelemek istediğiniz metni giriniz(8 karakter): ");
        message=sc.nextLine();
        }
        while (message.length()!=8);
        System.out.println("genel anahtarlar n="+n+" ve e="+e);

        //sifrelenecek mesajı ascii formatına dönüştürüp diziye atıyoruz
        char[] ch=new char[message.length()];
        int[] asciiArray=new int[message.length()];
        for(int i=0;i<message.length();i++){
            ch[i]=message.charAt(i);
            asciiArray[i]=(int) ch[i];
        }

        //ascii formatındaki diziyi asciiMessage adlı stringe ekliyoruz
        String asciiMessage = "";
        for (int i=0;i<ch.length;i++){
            asciiMessage+=asciiArray[i];

        }
        //n basamak sayısı hesaplama
        int basamak=0,nbasamak=n;
        while(nbasamak>0){
            nbasamak/=10;
            basamak++;
        }
        int lclear=basamak-1;

        //asciiMessage stringin lclear basamak ayrımında son indisi tek basamaksa sonuna 0 ekliyoruz
        if(asciiMessage.length()%lclear!=0){
            asciiMessage+="0";
        }

        //ascii stringi lclear basamaklarına ayırıyoruz lclearArray dizisinde tutuyoruz
        int asciiDongu=asciiMessage.length()/2;
        int[] lclearArray=new int[asciiDongu];
        int j=0;
        for (int i=0;i<asciiDongu;i++){
            lclearArray[i]= Integer.parseInt(asciiMessage.substring(0+j,lclear+j));
            j+=lclear;
        }

        //lclearArray dizisi değerlerini e üssü mod n işlemi yapıyoruuz
        long[] sifrelenmisAsciiArray=new long[lclearArray.length];
        for(int k=0;k<lclearArray.length;k++) {
            long sonuc = (long) Math.pow(lclearArray[k], e);
            sifrelenmisAsciiArray[k] = sonuc % n;
        }


        //sifrelenmisAsciiArray n basamak sayısına esitleme yapıyoruz sifrelenmisasciiyi veriyoruz
        //basamak degiskeni aynı zamanda lcipher
        String sifrelenmisAscii="";
        for(long i:sifrelenmisAsciiArray){
            int temp= (int) i;
            int indexBasamak=0;
            while (temp>0){
                temp/=10;
                indexBasamak++;
            }
            if(indexBasamak==basamak){
                sifrelenmisAscii+=i;
            }
            else {
                for(int k=0;k<(basamak-indexBasamak);k++){
                    sifrelenmisAscii+="0";
                }
                //sifrelenmisAsciiArray elemanları içerisinde 0 varsa indexBasamak değeri 0 gelecektir bu hatayı gidermek için kullanıldı
                if(indexBasamak!=0){
                    sifrelenmisAscii+=i;
                }
            }
        }
        System.out.print("sifrelenmis metin="+sifrelenmisAscii);
        System.out.println(" ");
        System.out.println("deşifrelemek için özel anahtar giriniz:");
        int ozelAnahtar=sc.nextInt();
        return desifreleme(sifrelenmisAsciiArray,asciiArray,asciiMessage,lclear,n,ozelAnahtar);
    }
    //deşifreleme
    public String desifreleme(long[] sifrelenmisAsciiArray,int[] asciiArray,String asciiMessage, int lclear,int n,int d){
        System.out.println("DESİFRELEME İSLEMİ");

        //yüksek boyutlu değerler için bigint sınıfı kullanıldı
        BigInteger bigInteger;
        BigInteger bigintegerN;
        BigInteger bigIntegerD;
        BigInteger result;
        long[] lcipherArray=new long[sifrelenmisAsciiArray.length];
        for(int k=0;k<sifrelenmisAsciiArray.length;k++) {
            bigInteger= new BigInteger(String.valueOf(sifrelenmisAsciiArray[k]));
            bigintegerN=new BigInteger(String.valueOf(n));
            bigIntegerD=new BigInteger(String.valueOf(d));
            result=bigInteger.modPow(bigIntegerD,bigintegerN);
            lcipherArray[k]=result.intValue();
        }
        //lclear basamagına tamamlama islemi yapıldı desifrelenmisAscii stringine atandı
        String desifrelenmisAscii="";
        for(long i:lcipherArray){
            int temp= (int) i;
            int indexBasamak=0;
            while (temp>0){
                temp/=10;
                indexBasamak++;
            }
            if(indexBasamak==lclear){
                desifrelenmisAscii+=i;
            }
            else {
                for(int k=0;k<(lclear-indexBasamak);k++){
                    desifrelenmisAscii+="0";
                }
                //desifrelenmisAsciiArray elemanları içerisinde 0 varsa indexBasamak değeri 0 gelecektir bu hatayı gidermek için kullanıldı
                if(indexBasamak!=0){
                    desifrelenmisAscii+=i;
                }
            }
        }
        //basta stringin sonuna eklediğimiz 0 ı kaldırıyoruz
        StringBuilder sb=new StringBuilder(desifrelenmisAscii);
        sb.deleteCharAt(desifrelenmisAscii.length()-1);

        int desifrelenmisAsciiDongu=asciiArray.length;
        //desifrelenmis asciiyi basamaklarına ayırıp diziye atıyoruz
        int[] desifrelenmisAsciiArray=new int[desifrelenmisAsciiDongu];

        if(desifrelenmisAscii.equals(asciiMessage)){
            desifrelenmisAsciiArray=asciiArray;
            //desifrelenmisAsciiArrayi desifrelenmis metne ceviriyoruz
            String desifrelenmisMessage="";
            for (int i:desifrelenmisAsciiArray){
                desifrelenmisMessage+=Character.toString((char) i);

            }
            System.out.println("desifreleme basarili mesaj: "+desifrelenmisMessage);
            return desifrelenmisMessage;
        }
        else{
            System.out.println("desifreleme basarisiz...");
            Scanner sc=new Scanner(System.in);
            System.out.println("Özel anahtarı tekrar giriniz: ");
            int tekrarOzelAnahtar=sc.nextInt();
            return desifreleme(sifrelenmisAsciiArray,asciiArray,asciiMessage,lclear,n,tekrarOzelAnahtar);
        }

    }

}
