package cpu.alu;

import util.BinaryIntegers;
import util.DataType;
import util.Transformer;

import java.util.Locale;

import static util.BinaryIntegers.NegativeOne;
import static util.BinaryIntegers.ZERO;

/**
 * Arithmetic Logic Unit
 * ALU封装类
 */
public class ALUTEMP {

    static DataType remainderReg;
    //模拟寄存器的进位标志位
    private static String CF="0";
    //模拟寄存器的溢出标志位
    private static String OF="0";

    /**
     * 返回两个二进制整数的和
     * dest + src
     *
     * @param src  32-bits
     * @param dest 32-bits
     * @return 32-bits
     */
    public static DataType add(DataType src, DataType dest) {
        // TODO
        return new DataType(adder(src.toString(),dest.toString(),'0',32));
    }

    /** 一位加法器 */
    private static String fullAdder(char x, char y, char c){
        int bit=(x-'0')^(y-'0')^(c-'0');
        int carry=((x-'0')|(y-'0'))&(c-'0')|((x-'0')&(y-'0'));
        return ""+carry+bit;
    }

    /** 逐位相加 */
    private static String carry_adder(String operand1, String operand2, char c, int length){
        String res="";
        char carry=c;
        for (int i=length-1;i>=0;i--){
            String temp=fullAdder(operand1.charAt(i),operand2.charAt(i),carry);
            carry=temp.charAt(0);
            res=temp.charAt(1)+res;
        }
        CF=String.valueOf(carry);
        return res;
    }

    /** 溢出判断 */
    private static String addOverFlow(String operand1, String operand2, String result){
        int X=operand1.charAt(0)-'0';
        int Y=operand2.charAt(0)-'0';
        int S=result.charAt(0)-'0';
        return ""+((~X & ~Y & S)|(X & Y & ~S));
    }

    /** 加法器 */
    private static String adder(String operand1, String operand2, char c, int length){
        String res=carry_adder(operand1,operand2,c,length);
        OF=addOverFlow(operand1,operand2,res);
        return res;
    }

    /**
     * 返回两个二进制整数的差
     * dest - src
     *
     * @param src  32-bits
     * @param dest 32-bits
     * @return 32-bits
     */
    public DataType sub(DataType src, DataType dest) {
        // TODO
        return new DataType(adder(dest.toString(),negation(src.toString()),'1',32));
    }

    /** 按位取反 */
    private static String negation(String operand){
        StringBuffer ans=new StringBuffer();
        for (int i=0;i<operand.length();i++) ans.append(operand.charAt(i)=='1' ? '0':'1');
        return ans.toString();
    }

    /**
     * 返回两个二进制整数的乘积(结果低位截取后32位)
     * dest * src
     *
     * @param src  32-bits
     * @param dest 32-bits
     * @return 32-bits
     */
    public static DataType mul(DataType src, DataType dest) {
        // TODO
        /*
        int length=32;
        String X=src.toString();
        String destStr=dest.toString();//被乘数
        String negX=oneAdder(negation(X)).substring(1);//取反加一，去掉第一位溢出位
        String product=ZERO+destStr;//部分积
        int Y1=0;//Y0
        int Y2=product.charAt(2*length-1)-'0';//Y1

        for (int i=0;i<length;i++){
            switch (Y1-Y2){
                case 1:
                    //X
                    product=product.substring(length)+adder(product.substring(0,length),X,'0',length);
                case -1:
                    //negX
                    product=product.substring(length)+adder(product.substring(0,length),negX,'0',length);
            }
            //循环右移
            product=product.substring(0,1)+product.substring(0,product.length()-1);
            //更新Y1
            //更新Y2
        }

        return null;*/
        int length = 32;  // length为数据长度
        String X = src.toString();
        String destStr = dest.toString();
        String negX = oneAdder(negation(X)).substring(1);  //取反加一,去掉第一位溢出位
        String product = BinaryIntegers.ZERO + destStr;
        int Y1 = 0;
        int Y2 = product.charAt(2 * length - 1) - '0';
        for (int i = 0; i < length; i++) {
            switch (Y1 - Y2) {
                case 1:
                    product = adder(product.substring(0, length), X, '0', length) + product.substring(length);
                    break;
                case -1:
                    product = adder(product.substring(0, length), negX, '0', length) + product.substring(length);
                    break;
            }
            product=product.substring(0,1)+product.substring(0,product.length()-1);
            Y1=Y2;
            Y2=product.charAt(2*length-1)-'0';
        }
        String higher=product.substring(0,length);
        String lower=product.substring(length);
        OF="0";
        for (char c:higher.toCharArray()){
            if (c=='1'){
                OF="1";
                break;
            }
        }
        return new DataType(lower);
    }


    private static String oneAdder(String operand){
        int len=operand.length();
        StringBuilder temp=new StringBuilder(operand).reverse();
        int[] num=new int[len];
        for (int i=0;i<len;i++) num[i]=temp.charAt(i)-'0';

        int bit=0;
        int carry=1;
        char[] res=new char[len];
        for (int i=0;i<len;i++){
            bit=num[i]^carry;
            carry=num[i]&carry;
            res[i]=(char) (bit+'0');
        }

        String result=new StringBuilder(new String(res)).reverse().toString();
        return (operand.charAt(0)==result.charAt(0) ? '0':'1')+result;
    }

    /**
     * 返回两个二进制整数的除法结果
     * dest ÷ src
     *
     * @param src  32-bits
     * @param dest 32-bits
     * @return 32-bits
     */
    public static DataType div(DataType src, DataType dest) {
        // TODO
        String srcStr=src.toString();//除数
        String destSrc=dest.toString();//被除数

        int length=64;
        String quotient="";
        String remainder="";

        boolean op1Zero=isZero(destSrc);
        boolean op2Zero=isZero(srcStr);

        //0➗src
        if (op1Zero && !op2Zero){
            remainderReg=new DataType(ZERO);
            return new DataType(ZERO);
        }

        //dest➗0
        if (op2Zero){
            throw new ArithmeticException();
        }

        //(<(-2^31-1))➗(-1) 溢出
        //1000 0000 0000 0000 0000 0000 0000 0000
        if (isZero(destSrc.substring(1)) && destSrc.charAt(0)=='1' && isNegativeOne(srcStr)){
            remainderReg=new DataType(ZERO);
            return new DataType(destSrc);
        }

        String product=impleDigits(destSrc,length);//余数和商寄存器，对被除数做符号扩展

        //第一商的判断
        //除数和被除数符号相同做减法，否则做加法
        if (product.charAt(0)==srcStr.charAt(0)){
            product=adder(product.substring(0,length/2),negation(srcStr),'1',length/2)+product.substring(length/2);
        }else {
            product=adder(product.substring(0,length/2),srcStr,'0',length/2)+product.substring(length/2);
        }

        //后续商
        //余数和除数符号相同则商为1，减除数，否则商为0，加除数
        for (int i=0;i<length/2;i++){
            if (product.charAt(0)==srcStr.charAt(0)){
                quotient+='1';
                product=leftShift(product,1);
                product=adder(product.substring(0,length/2),negation(srcStr),'1',length/2)+product.substring(length/2);
            }else {
                quotient+='0';
                product=leftShift(product,1);
                product=adder(product.substring(0,length/2),srcStr,'0',length/2)+product.substring(length/2);
            }
        }

        //最后1位商
        quotient=quotient.substring(1);
        if (product.charAt(0)==srcStr.charAt(0)) quotient=quotient+"1";
        else quotient=quotient+"0";

        //修正商
        if (quotient.charAt(0)=='1') quotient=oneAdder(quotient).substring(1);

        //余数取高32位
        remainder=product.substring(0,length/2);

        //余数和被除数符号不同，则修正余数
        if (remainder.charAt(0)!=destSrc.charAt(0)){
            //被除数和余数符号相同，余数加除数，否则减除数
            if (destSrc.charAt(0)==srcStr.charAt(0)){
                remainder=adder(remainder,srcStr,'0',length/2);
            }else {
                remainder=adder(remainder,negation(srcStr),'1',length/2);
            }
        }

        //余数修正后，判断余数和除数的绝对值是否相等，对商和余数再做调整
        //余数和除数相加为0，余数为0，商减1
        //余数和除数相等，余数为0，商加1
        if (remainder.equals(srcStr)){
            quotient=oneAdder(quotient).substring(1);
            remainder=ZERO;
            remainderReg=new DataType(remainder);
            return new DataType(quotient);
        }else if (isZero(adder(srcStr,impleDigits(remainder,length/2),'0',length/2))){
            quotient=add(new DataType(impleDigits(quotient,length/2)),new DataType(NegativeOne)).toString();
            remainder=ZERO;
            remainderReg=new DataType(remainder);
            return new DataType(quotient);
        }

        remainderReg=new DataType(impleDigits(remainder,length/2));
        return new DataType(impleDigits(quotient,length/2));

    }

    private static boolean isZero(String operand){
        for (char c:operand.toCharArray()){
            if (c!='0') return false;
        }
        return true;
    }

    private static boolean isNegativeOne(String operand){
        for (char c:operand.toCharArray()){
            if (c!='1') return false;
        }
        return true;
    }

    private static String impleDigits(String operand, int length){
        int len=length-operand.length();
        char imple=operand.charAt(0);//符号扩展位
        StringBuilder res=new StringBuilder(new StringBuilder(operand).reverse());
        for (int i=0;i<len;i++){
            res.append(imple);
        }
        return res.reverse().toString();
    }

    private static String leftShift(String operand, int n){
        StringBuilder res=new StringBuilder(operand.substring(n));
        for (int i=0;i<n;i++){
            res.append("0");
        }
        return res.toString();
    }

    public static void main(String[] args) {
        DataType src = new DataType("00000000000000000000000000001010");
        DataType dest = new DataType("00000000000000000000000000001010");
        //DataType result =div(src, dest);
        System.out.println(Transformer.binaryToInt("10000000000000000000000000000000"));
        System.out.println(-(int) Math.pow(2,31)-1);
    }
}
