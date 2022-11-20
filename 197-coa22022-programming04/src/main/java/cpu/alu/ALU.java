package cpu.alu;

import util.DataType;
import util.Transformer;

/**
 * Arithmetic Logic Unit
 * ALU封装类
 */
public class ALU {

    //32余数存储在余数寄存器里
    DataType remainderReg;
    //模拟寄存器中的进位标志位
    private String CF = "0";
    //模拟寄存器中的溢出标志位
    private String OF = "0";

    /**
     * 存放特殊的32位二进制数
     */
    private class BinaryIntegers {

        public static final String ZERO = "00000000000000000000000000000000";

        public static final String NaN = "NaN";

        public static final String NegativeOne = "11111111111111111111111111111111";

    }

    /**
     * operand符号扩展到length长度
     * @param operand
     * @param length
     * @return
     */
    private String impleDigits(String operand, int length) {
        int len = length - operand.length();
        char imple = operand.charAt(0);
        StringBuffer res = new StringBuffer(new StringBuffer(operand).reverse());
        for (int i = 0; i < len; i++) {
            res = res.append(imple);
        }
        return res.reverse().toString();
    }

    private String oneAdder(String operand) {
        int len = operand.length();
        StringBuffer temp = new StringBuffer(operand);
        temp = temp.reverse();
        int[] num = new int[len];
        for (int i = 0; i < len; i++) num[i] = temp.charAt(i) - '0';  //先转化为反转后对应的int数组
        int bit = 0x0;
        int carry = 0x1;
        char[] res = new char[len];
        for (int i = 0; i < len; i++) {
            bit = num[i] ^ carry;
            carry = num[i] & carry;
            res[i] = (char) ('0' + bit);
        }
        String result = new StringBuffer(new String(res)).reverse().toString();
        return "" + (result.charAt(0) == operand.charAt(0) ? '0' : '1') + result;
    }

    private String adder(String operand1, String operand2, char c, int length) {
        operand1 = impleDigits(operand1, length);
        operand2 = impleDigits(operand2, length);
        String res = carry_adder(operand1, operand2, c, length);
        OF = addOverFlow(operand1, operand2, res);
        return res;
    }

    private String addOverFlow(String operand1, String operand2, String result) {
        int X = operand1.charAt(0) - '0';
        int Y = operand2.charAt(0) - '0';
        int S = result.charAt(0) - '0';
        return "" + ((~X & ~Y & S) | (X & Y & ~S));
    }

    private String carry_adder(String operand1, String operand2, char c, int length) {
        operand1 = impleDigits(operand1, length);
        operand2 = impleDigits(operand2, length);
        char carry = c;
        int bit;
        int carrynum;
        String temp;
        String res = "";

        for (int i = length - 1; i >= 0; i--) {
            bit=(operand1.charAt(i) - '0') ^ (operand2.charAt(i) - '0') ^ (carry - '0');
            carrynum = ((operand1.charAt(i) - '0') & (operand2.charAt(i) - '0')) | ((operand2.charAt(i) - '0') & (carry - '0')) | ((operand1.charAt(i) - '0') & (carry - '0'));
            temp = "" + carrynum + bit;
            carry = temp.charAt(0);
            res = temp.charAt(1) + res;
        }
        CF = String.valueOf(carry);
        return res;
    }


    public String negation(String operand) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < operand.length(); i++) {
            result = operand.charAt(i) == '1' ? result.append("0") : result.append("1");
        }
        return result.toString();
    }


    public String leftShift(String operand, int n) {
        StringBuffer result = new StringBuffer(operand.substring(n));
        for (int i = 0; i < n; i++) {
            result = result.append("0");
        }
        return result.toString();
    }


    /**
    String reverse(String s) {
        char[] resultChar = s.toCharArray();
        boolean flag = false;
        for (int i = s.length() - 1; i >= 0; i--) {
            if (!flag) {
                if (resultChar[i] == '1') {
                    flag = true;
                }
            } else {
                if (resultChar[i] == '1') {

                    resultChar[i] = '0';

                } else {
                    resultChar[i] = '1';
                }
            }
        }
        return String.valueOf(resultChar);

    }

    String add(String src, String dest) {
        StringBuilder result = new StringBuilder();
        CF = "0";
        for (int i = dest.length() - 1; i >= 0; --i) {
            int sum = (src.charAt(i) - '0') + (dest.charAt(i) - '0') + (CF.charAt(0) - '0');
            if (sum == 3) {
                result.append("1");
                CF = "1";
            } else if (sum == 2) {
                result.append("0");
                CF = "1";
            } else if (sum == 1) {
                result.append("1");
                CF = "0";
            } else if (sum == 0) {
                result.append("0");
                CF = "0";
            }
        }
        result.reverse();
        if (src.charAt(0) == dest.charAt(0) && result.charAt(0) != dest.charAt(0)) {
            OF = "1";
        } else {
            OF = "0";
        }
        return result.toString();
    }
     */


    /**
     * 返回两个二进制整数的除法结果
     * dest ÷ src
     *
     * @param src  32-bits
     * @param dest 32-bits
     * @return 32-bits
     */
    public DataType div(DataType src, DataType dest) {
        // TODO

        /**存在bug的代码
        if (dest.toString().equals(BinaryIntegers.ZERO) && !src.toString().equals(BinaryIntegers.ZERO)) {
            remainderReg = new DataType("0");
            return new DataType("0");
        }
        if (src.toString().equals(BinaryIntegers.ZERO)) {
            throw new ArithmeticException();
        }
        if (dest.toString().substring(1).equals(BinaryIntegers.ZERO) && dest.toString().charAt(0) == '1' && src.toString().equals(BinaryIntegers.NegativeOne)) {
            remainderReg = new DataType(BinaryIntegers.ZERO);
            return new DataType(dest.toString());
        }

        String quotient = dest.toString();//商
        String divisor = src.toString();//除数
        if (quotient.charAt(0) == '0') {
            remainderReg = new DataType("00000000000000000000000000000000");
        } else {
            remainderReg = new DataType("11111111111111111111111111111111");
        }
        String temp;
        if (dest.toString().charAt(0) == src.toString().charAt(0)) {
            divisor = reverse(divisor);
        }
        for (int i = 0; i < dest.toString().length(); ++i) {
            remainderReg = new DataType(remainderReg.toString().substring(1) + quotient.substring(0, 1));
            temp = add(remainderReg.toString(), divisor);
            if (((remainderReg.toString().charAt(0) == temp.charAt(0)) && !remainderReg.toString().equals("00000000000000000000000000000000")) || temp.equals("00000000000000000000000000000000")) {
                quotient = quotient.substring(1) + "1";
                remainderReg = new DataType(temp);
            } else {
                quotient = quotient.substring(1) + "0";
            }
        }
        if (dest.toString().charAt(0) != src.toString().charAt(0)) {
            quotient = reverse(quotient);
        }

        if (dest.toString().equals("10000000000000000000000000000000") && src.toString().equals("11111111111111111111111111111111")) {
            return new DataType("11000000000000000000000000000000000000000000000000000000000000000");
        }

        return new DataType(quotient);
         */


        /**
         * 重新写的代码
         */
        String srcStr = src.toString();
        String destStr = dest.toString();
        int length = 64;
        String quotient = "";
        String remainder = "";

        /**
         * 处理特殊情况
         */
        // 1.被除数为0，除数不为0，则商为0，余数为0
        if (destStr.equals(BinaryIntegers.ZERO) && !srcStr.equals(BinaryIntegers.ZERO)) {
            remainderReg = new DataType(BinaryIntegers.ZERO);
            return new DataType(BinaryIntegers.ZERO);
        }

        // 2.除数为0，则抛出异常
        if (srcStr.equals(BinaryIntegers.ZERO)) {
            throw new ArithmeticException();
        }

        // 3.若为-2^32 / -1，则溢出
        if (destStr.substring(1).equals(BinaryIntegers.ZERO) && destStr.charAt(0) == '1' && srcStr.equals(BinaryIntegers.NegativeOne)) {
            remainderReg = new DataType(BinaryIntegers.ZERO);
            return new DataType(destStr);
        }

        String product = impleDigits(destStr, length);
        if (product.charAt(0) == srcStr.charAt(0))
            product = adder(product.substring(0, length / 2), negation(srcStr), '1', length / 2) + product.substring(length / 2);
        else
            product = adder(product.substring(0, length / 2), srcStr, '0', length / 2) + product.substring(length / 2);
        for (int i = 0; i < length / 2; i++) {
            if (product.charAt(0) == srcStr.charAt(0)) {
                quotient += "1";
                product = leftShift(product, 1);
                product = adder(product.substring(0, length / 2), negation(srcStr), '1', length / 2) + product.substring(length / 2);
            } else {
                quotient += "0";
                product = leftShift(product, 1);
                product = adder(product.substring(0, length / 2), srcStr, '0', length / 2) + product.substring(length / 2);
            }
        }

        quotient = quotient.substring(1);
        if (product.charAt(0) == srcStr.charAt(0)) quotient = quotient + "1";
        else quotient = quotient + "0";
        if (quotient.charAt(0) == '1') quotient = oneAdder(quotient).substring(1);
        remainder = product.substring(0, length / 2);
        if (remainder.charAt(0) != destStr.charAt(0)) {
            if (destStr.charAt(0) == srcStr.charAt(0)) {
                remainder = adder(remainder, srcStr, '0', length / 2);
            } else {
                remainder = adder(remainder, negation(srcStr), '1', length / 2);
            }
        }

        if (adder(srcStr, impleDigits(remainder, length / 2), '0', length / 2).equals(BinaryIntegers.ZERO)) {
            quotient=adder(new DataType(impleDigits(quotient, length / 2)).toString(), new DataType(BinaryIntegers.NegativeOne).toString(), '0', 32);
            remainder = BinaryIntegers.ZERO;
            remainderReg = new DataType(remainder);
            return new DataType(quotient);
        } else if (remainder.equals(srcStr)) {
            quotient = oneAdder(quotient).substring(1);
            remainder = BinaryIntegers.ZERO;
            remainderReg = new DataType(remainder);
            return new DataType(quotient);
        }

        //保存余数
        remainderReg = new DataType(impleDigits(remainder, length / 2));
        return new DataType(impleDigits(quotient, length / 2));
    }
}
