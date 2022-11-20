package util;


import java.util.Deque;
import java.util.LinkedList;

public class Transformer {

    //十进制整数转化为二进制数（不限制位数）
    public static String intToBinary_2(int num,int n) {

        int temp = Math.abs(num);
        int mod;//余数
        Deque stack = new LinkedList();//栈先进后出弹出余数
        String sign_bit = "";//存储符号位和补充位
        String valid_bit="";//存储有效位

        //除2取余，将余数从栈中弹出并保存到res_temp中
        do {
            mod = temp % 2;
            stack.push(mod);//push到栈中
            temp /= 2;
        } while (temp > 0);
        while (!stack.isEmpty()) {
            valid_bit+=String.valueOf((int) stack.pop());//从栈中弹出（弹出的是对象，转化为int），并转为字符串存储到valid_bit中
        }

        //n=0时不限制二进制位数，可跳过此步；n≠0时有固定的二进制位数
        if (n!=0) {
            //补充0位和1位
            if (num < 0) {
                for (int i = 0; i < n - valid_bit.length(); i++) {
                    sign_bit += "1";
                }
            } else {
                for (int i = 0; i < n - valid_bit.length(); i++) {
                    sign_bit += "0";
                }
            }
        }

        return sign_bit+valid_bit;
    }

    //浮点数规格化（小数部分转二进制）
    public static String normalization(double num) {

        double temp = num;
        String res = "";
        String res_temp = "";

        //乘2取整，将整数保存到res_temp中
        do {
            temp = temp * 2;
            res_temp = String.valueOf(temp);
            res += res_temp.substring(0, String.valueOf(temp).indexOf('.'));
            temp -= Integer.parseInt(String.valueOf(temp).substring(0, String.valueOf(temp).indexOf('.')));
        } while (temp != 0.0);

        return res;
    }

    public static String intToBinary(String numStr) {
        // TODO:
        int num = Integer.parseInt(numStr);
        //两种特殊情况（32位的最大值与最小值）
        if (num>=2147483647) {
            return "01111111111111111111111111111111";
        }else if (num<=-2147483648) {
            return "10000000000000000000000000000000";
        }else {
            String res = intToBinary_2(num, 32);
            return res;
        }
    }

    //十进制转二进制
    public static String binaryToInt_2(String binStr){
        int n=binStr.length();
        int temp=0;
        String res="";
        for (int i=0;i<n;i++){
            temp+=Math.pow(2,i)*(Integer.parseInt(binStr.substring(n-1-i,n-i)));
        }
        res=Integer.toString(temp);
        return res;
    }
    public static String binaryToInt(String binStr) {
        // TODO:
        int n=binStr.length();
        int temp=0;
        String res="";
        //根据补码公式计算，即A=-2^(n-1)*(an-1)+sigma(i=0->i=n-2)2^i*(ai)
        for (int i=0;i<n-1;i++){
            temp+=Math.pow(2,i)*(Integer.parseInt(binStr.substring(n-1-i,n-i)));
        }
        temp-=Math.pow(2,n-1)*(Integer.parseInt(String.valueOf(binStr.charAt(0))));
        //Long型转换为字符串
        res=Integer.toString(temp);
        return res;

    }

    public static String decimalToNBCD(String decimalStr) {
        // TODO:
        int num=Integer.parseInt(decimalStr);
        String res="";
        String res_temp="";
        //设定符号位
        if (num<0){
            res="1101";
            decimalStr=decimalStr.replace("-","");
        }else if (num>0){
            res="1100";
        }else {
            res="0000";
        }

        //数字有效位转换为二进制
        for (int i=0;i<decimalStr.length();i++){
            res_temp+=intToBinary_2(Integer.parseInt(String.valueOf(decimalStr.charAt(i))),4);
        }

        //其余位添加0
        for (int i=0;i<28-res_temp.length();i++){
            res+="0";
        }

        return res+res_temp;
    }

    public static String NBCDToDecimal(String NBCDStr) {
        // TODO:
        boolean flag=false;//判断是否到有效位
        String res="";
        String res_temp="";

        //判断正负
        if (NBCDStr.startsWith("1101")){
            if (NBCDStr.equals("11010000000000000000000000000000")){
                return "0";
            }else {
                res+="-";
            }
        }

        //二进制转十进制
        for (int i=4;i<32;){
            res_temp=binaryToInt_2(NBCDStr.substring(i,i+4));
            if (!(res_temp.equals("0"))){
                flag=true;
            }

            //从第一个非零数开始记录
            if (flag){
                res+=res_temp;
            }
            i+=4;
        }

        return res;
    }

    public static String floatToBinary(String floatStr) {
        // TODO:
        //0：1负0正
        //1~8：移码表示的阶
        //9~31：有效值
        /* 20.59375=10100.10011
        10100.10011=1.010010011*2^4
        得到：s=0,E=4+127=131,M=010010011
        最后得到32位浮点数的二进制格式为：
        S E        M
        0 10000011 0100 1001 1000 0000 0000 000 */
        String res_S="",res_E="",res_M="";
        String integer_part="",decimal_part="",integer_temp="",decimal_temp="";
        Float num=Float.parseFloat(floatStr);
        String res_temp;
        int len;
        int E;

        //三种特殊情况，Float.isNaN用于判断参数是否是一个数字；Float.isInfinite用于判断参数是否正无穷大或负无穷大
        if(Float.isNaN(num)){
            return "NaN";
        }else if(Float.isInfinite(num)){
            if(num > 0){
                return "+Inf";
            }else{
                return "-Inf";
            }
        }

        if (num<0){
            res_S="1";
            floatStr=floatStr.replace("-","");
        }else if (num>0){
            res_S="0";
        }else{
            return "00000000000000000000000000000000";
        }

        integer_part=floatStr.substring(0,floatStr.indexOf("."));//获取整数部分
        System.out.println(integer_part);
        decimal_part="0"+floatStr.substring(floatStr.indexOf("."),floatStr.length());//获取小数部分
        //System.out.println(decimal_part);
        integer_temp=intToBinary_2(Integer.parseInt(integer_part),0);//整数部分转为二进制
        //System.out.println(integer_temp);
        decimal_temp=normalization(Float.parseFloat(decimal_part));//规格化小数部分
        //System.out.println(decimal_temp);
        E=127+integer_temp.length()-1;//阶码
        res_E=intToBinary_2(E,8);
        System.out.println("res_E:"+res_E);
        if (integer_temp!="0") {
            res_M = integer_temp.substring(1, integer_temp.length()) + decimal_temp;
        }else {
            res_M=decimal_temp;
        }
        //补充0位
        len=res_M.length();
        for (int i=0;i<23-len;i++){
            res_M+="0";
        }

        return res_S+res_E+res_M;
    }

    public static String binaryToFloat(String binStr) {
        // TODO:
        //A=(-1)^s*(1.M)*2^(E-127)
        int res_S,res_E;
        double res,res_1M;

        if(binStr.substring(1).equals("0000000000000000000000000000000")){
            return "0.0";
        }else if(binStr.substring(1).equals("1111111100000000000000000000000")){
            if(binStr.charAt(0) == '0'){
                return "+Inf";
            }else{
                return "-Inf";
            }
        }else if(binStr.substring(1, 9).equals("11111111")){
            return "NaN";
        }

        res_S=Integer.parseInt(binStr.substring(0,1));//符号位
        res_E=Integer.parseInt(binaryToInt_2(binStr.substring(1,9)))-127;//阶码
        Double exponent=Math.pow(2,res_E-127);//以2为底求值
        res_1M=Double.parseDouble(binStr.substring(9,32));//尾数
        res=Math.pow(-1,res_S)*res_1M*exponent;

        return String.valueOf(res);
    }

    public static void main(String[] args){
        float f=1.23456789e9f;
        //System.out.println(floatToBinary("-0.5"));
        System.out.println(floatToBinary("0.5625"));
        //System.out.println(intToBinary("0"));
        //System.out.println(binaryToInt_2("10000011"));
    }
}
