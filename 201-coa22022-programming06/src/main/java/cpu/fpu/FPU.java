package cpu.fpu;

import util.DataType;
import util.IEEE754Float;

/**
 * floating point unit
 * 执行浮点运算的抽象单元
 * 浮点数精度：使用3位保护位进行计算
 */
public class FPU {

    private final String[][] mulCorner = new String[][]{
            {IEEE754Float.P_ZERO, IEEE754Float.N_ZERO, IEEE754Float.N_ZERO},
            {IEEE754Float.N_ZERO, IEEE754Float.P_ZERO, IEEE754Float.N_ZERO},
            {IEEE754Float.P_ZERO, IEEE754Float.P_ZERO, IEEE754Float.P_ZERO},
            {IEEE754Float.N_ZERO, IEEE754Float.N_ZERO, IEEE754Float.P_ZERO},
            {IEEE754Float.P_ZERO, IEEE754Float.P_INF, IEEE754Float.NaN},
            {IEEE754Float.P_ZERO, IEEE754Float.N_INF, IEEE754Float.NaN},
            {IEEE754Float.N_ZERO, IEEE754Float.P_INF, IEEE754Float.NaN},
            {IEEE754Float.N_ZERO, IEEE754Float.N_INF, IEEE754Float.NaN},
            {IEEE754Float.P_INF, IEEE754Float.P_ZERO, IEEE754Float.NaN},
            {IEEE754Float.P_INF, IEEE754Float.N_ZERO, IEEE754Float.NaN},
            {IEEE754Float.N_INF, IEEE754Float.P_ZERO, IEEE754Float.NaN},
            {IEEE754Float.N_INF, IEEE754Float.N_ZERO, IEEE754Float.NaN}
    };

    private final String[][] divCorner = new String[][]{
            {IEEE754Float.P_ZERO, IEEE754Float.P_ZERO, IEEE754Float.NaN},
            {IEEE754Float.N_ZERO, IEEE754Float.N_ZERO, IEEE754Float.NaN},
            {IEEE754Float.P_ZERO, IEEE754Float.N_ZERO, IEEE754Float.NaN},
            {IEEE754Float.N_ZERO, IEEE754Float.P_ZERO, IEEE754Float.NaN},
            {IEEE754Float.P_INF, IEEE754Float.P_INF, IEEE754Float.NaN},
            {IEEE754Float.N_INF, IEEE754Float.N_INF, IEEE754Float.NaN},
            {IEEE754Float.P_INF, IEEE754Float.N_INF, IEEE754Float.NaN},
            {IEEE754Float.N_INF, IEEE754Float.P_INF, IEEE754Float.NaN},
    };


    /**
     * compute the float mul of dest * src
     */
    public DataType mul(DataType src, DataType dest) {
        // TODO
        String srcStr = src.toString();
        String destStr = dest.toString();
        //1.处理特殊输入
        if (srcStr.matches(IEEE754Float.NaN_Regular) || destStr.matches(IEEE754Float.NaN_Regular)) {
            return new DataType(IEEE754Float.NaN);
        }
        if(srcStr.equals(IEEE754Float.NaN) || srcStr.matches(IEEE754Float.NaN) || destStr.equals(IEEE754Float.NaN) || destStr.matches(IEEE754Float.NaN)){
            return new DataType(IEEE754Float.NaN);
        }
        if((srcStr.equals(IEEE754Float.P_ZERO) || srcStr.equals(IEEE754Float.N_ZERO)) && (destStr.equals(IEEE754Float.P_INF) || destStr.equals(IEEE754Float.N_INF))){
            return new DataType(IEEE754Float.NaN);
        }
        if((destStr.equals(IEEE754Float.P_ZERO) || destStr.equals(IEEE754Float.N_ZERO)) && (srcStr.equals(IEEE754Float.P_INF) || srcStr.equals(IEEE754Float.N_INF))){
            return new DataType(IEEE754Float.NaN);
        }
        if((srcStr.equals(IEEE754Float.P_ZERO) || srcStr.equals(IEEE754Float.N_ZERO)) && srcStr.charAt(0) == destStr.charAt(0)){
            return new DataType(IEEE754Float.P_ZERO);
        }
        if((srcStr.equals(IEEE754Float.P_ZERO) || srcStr.equals(IEEE754Float.N_ZERO)) && srcStr.charAt(0) != destStr.charAt(0)){
            return new DataType(IEEE754Float.N_ZERO);
        }
        if((destStr.equals(IEEE754Float.P_ZERO) || destStr.equals(IEEE754Float.N_ZERO)) && srcStr.charAt(0) == destStr.charAt(0)){
            return new DataType(IEEE754Float.P_ZERO);
        }
        if((destStr.equals(IEEE754Float.P_ZERO) || destStr.equals(IEEE754Float.N_ZERO)) && srcStr.charAt(0) != destStr.charAt(0)){
            return new DataType(IEEE754Float.N_ZERO);
        }
        if((srcStr.equals(IEEE754Float.P_INF) || srcStr.equals(IEEE754Float.N_INF)) && srcStr.charAt(0) == destStr.charAt(0)){
            return new DataType(IEEE754Float.P_INF);
        }
        if((srcStr.equals(IEEE754Float.P_INF) || srcStr.equals(IEEE754Float.N_INF)) && srcStr.charAt(0) != destStr.charAt(0)){
            return new DataType(IEEE754Float.N_INF);
        }
        if((destStr.equals(IEEE754Float.P_INF) || destStr.equals(IEEE754Float.N_INF)) && srcStr.charAt(0) == destStr.charAt(0)){
            return new DataType(IEEE754Float.P_INF);
        }
        if((destStr.equals(IEEE754Float.P_INF) || destStr.equals(IEEE754Float.N_INF)) && srcStr.charAt(0) != destStr.charAt(0)){
            return new DataType(IEEE754Float.N_INF);
        }

        String cornerCondition = cornerCheck(mulCorner, srcStr, destStr);
        if (cornerCondition != null) {
            return new DataType(cornerCondition);
        }

        //2.返回两个浮点数进行乘法运算的结果
        return new DataType(floatMultiplication(srcStr, destStr).substring(1));
    }


    /**
     * compute the float mul of dest / src
     */
    public DataType div(DataType src, DataType dest) {
        // TODO
        String srcStr = src.toString();
        String destStr = dest.toString();
        //1.处理边界情况(NaN, 0, INF)
        if(destStr.equals(IEEE754Float.NaN) || destStr.matches(IEEE754Float.NaN) || srcStr.equals(IEEE754Float.NaN) || srcStr.matches(IEEE754Float.NaN)){
            return new DataType(IEEE754Float.NaN);
        }
        if((destStr.equals(IEEE754Float.P_ZERO) || destStr.equals(IEEE754Float.N_ZERO)) && !(srcStr.equals(IEEE754Float.P_ZERO) || srcStr.equals(IEEE754Float.N_ZERO))){
            if(destStr.charAt(0) == srcStr.charAt(0)){
                return new DataType(IEEE754Float.P_ZERO);
            }
            else{
                return new DataType(IEEE754Float.N_ZERO);
            }
        }
        if((destStr.equals(IEEE754Float.P_ZERO) || destStr.equals(IEEE754Float.N_ZERO)) && (srcStr.equals(IEEE754Float.P_ZERO) || srcStr.equals(IEEE754Float.N_ZERO))){
            return new DataType(IEEE754Float.NaN);
        }
        if((destStr.equals(IEEE754Float.P_INF) || destStr.equals(IEEE754Float.N_INF)) && (srcStr.equals(IEEE754Float.P_INF) || srcStr.equals(IEEE754Float.N_INF))){
            return new DataType(IEEE754Float.NaN);
        }
        if(destStr.equals(IEEE754Float.P_INF) || destStr.equals(IEEE754Float.N_INF)){
            if(destStr.charAt(0) == srcStr.charAt(0)){
                return new DataType(IEEE754Float.P_INF);
            }
            else{
                return new DataType(IEEE754Float.N_INF);
            }
        }
        if(srcStr.equals(IEEE754Float.P_INF) || srcStr.equals(IEEE754Float.N_INF)){
            if(destStr.charAt(0) == srcStr.charAt(0)){
                return new DataType(IEEE754Float.P_ZERO);
            }
            else{
                return new DataType(IEEE754Float.N_ZERO);
            }
        }

        if(!(destStr.equals(IEEE754Float.P_ZERO) || destStr.equals(IEEE754Float.N_ZERO)) && (srcStr.equals(IEEE754Float.P_ZERO) || srcStr.equals(IEEE754Float.N_ZERO))){
            throw new java.lang.ArithmeticException();
        }

        String cornerCondition = cornerCheck(divCorner, destStr, srcStr);
        if (cornerCondition != null) {
            return new DataType(cornerCondition);
        }

        //2.进行浮点数相除的运算
        //2.1 返回当被除数为特殊值的结果
        if (IEEE754Float.P_ZERO.equals(destStr) || IEEE754Float.N_ZERO.equals(destStr)) {
            if (destStr.charAt(0) == srcStr.charAt(0)) {
                return new DataType(IEEE754Float.P_ZERO);
            } else {
                return new DataType(IEEE754Float.N_ZERO);
            }
        }
        //2.2 返回两个浮点数进行除法运算的结果
        return new DataType(floatDivision(destStr, srcStr).substring(1));
    }


    private String cornerCheck(String[][] cornerMatrix, String oprA, String oprB) {
        for (String[] matrix : cornerMatrix) {
            if (oprA.equals(matrix[0]) &&
                    oprB.equals(matrix[1])) {
                return matrix[2];
            }
        }
        return null;
    }

    /**
     * 两个浮点数的乘法，即operand1*operand2
     */
    public String floatMultiplication(String operand1, String operand2) {
        //阶码长度
        int eLength = 8;
        //尾数长度
        int sLength = 23;
        //计算偏移量
        int bias = (int) (((Math.pow(2, eLength) - 1) + 1) / 2 - 1);
        //获取两个操作数的指数部分
        int operand1Exponent = Integer.valueOf(operand1.substring(1, 1 + eLength), 2);
        int operand2Exponent = Integer.valueOf(operand2.substring(1, 1 + eLength), 2);
        if (operand1Exponent == 0) {
            //如果指数是全0，那么指数的真实值为1，因为阶码已经考虑了隐藏位
            operand1Exponent++;
        }
        if (operand2Exponent == 0) {
            operand2Exponent++;
        }
        int exponent = operand1Exponent + operand2Exponent - bias;
        //获取包括隐藏位在内的两个有效位
        String operand1Sig = getSignificand(operand1, eLength, sLength);
        String operand2Sig = getSignificand(operand2, eLength, sLength);
        //计算乘积结果的符号位
        int sign = (operand1.charAt(0) - '0') ^ (operand2.charAt(0) - '0');
        String temp = unsignedMultiplication(operand1Sig, operand2Sig, operand1Sig.length() * 2);
        StringBuilder answer = new StringBuilder();
        //因为前两位为隐藏位，因此要移动小数点
        exponent++;
        while (temp.charAt(0) == '0' && exponent > 0) {
            temp = leftShift(temp, "1");
            exponent--;
        }

        //右移进行规格化数
        while (!allZero(temp.substring(0, 1 + sLength)) && exponent < 0) {
            temp = rightShift(temp, 1);
            exponent++;
        }
        //边界值
        if (exponent >= bias * 2 + 1) {
            answer.append("1");
            answer.append(sign);
            for (int i = 0; i < eLength; i++) {
                answer.append(1);
            }
            answer.append(getAllZeros(sLength));
            return answer.toString();
        }
        //非规格化数
        if (exponent == 0) {
            temp = rightShift(temp, 1);
        }
        //乘法和除法有可能指数小于0，指数下溢，处理成0
        if (exponent < 0) {
            answer.append("0" + sign);
            answer.append(getAllZeros(sLength + eLength));
            return answer.toString();
        }
        String ans_exponent = Integer.toBinaryString(exponent);
        int len = ans_exponent.length();
        //补齐到eLength长度
        for (int i = 0; i < eLength - len; i++) {
            ans_exponent = "0" + ans_exponent;
        }

        return "0" + round((char) (sign + 48), ans_exponent, temp);
    }

    /**
     * 无符号整型数的乘法
     */
    public String unsignedMultiplication(String operand1, String operand2, int length) {
        String X = impleDigits(operand1, length / 2);  //length为寄存器长度，因此操作数长度只能为length的一半
        operand2 = impleDigits(operand2, length / 2);
        StringBuffer productBuffer = new StringBuffer();
        for (int i = 0; i < length / 2; i++) {
            productBuffer = productBuffer.append("0");
        }

        String product = productBuffer+ operand2;

        for (int i = 0; i < length / 2; i++) {
            int Y = product.charAt(length - 1) - '0';
            char carry = '0';
            if (Y == 1) {
                String temp = carry_adder(product.substring(0, length / 2), X, '0', length / 2);  //这里不能用adder，因为length长度不一定为4的倍数
                carry = temp.charAt(0);  //carry_adder的溢出即为有进位
                product = temp.substring(1) + product.substring(length / 2);
            }
            product = carry + product.substring(0, product.length() - 1);  //carry为隐藏进位
        }
        return product;
    }

    /**
     * 两个浮点数的除法，即计算operand1/operand2的商
     */
    public String floatDivision(String operand1, String operand2) {
        //阶码长度
        int eLength = 8;
        //尾数长度
        int sLength = 23;
        int gLength = 3;
        int sign = (operand1.charAt(0) - '0') ^ (operand2.charAt(0) - '0');
        //偏移量
        int bias = (int) (((Math.pow(2,eLength)-1) + 1) / 2 - 1);
        int operand1Exponent = Integer.valueOf(operand1.substring(1, 1 + eLength), 2);
        int operand2Exponent = Integer.valueOf(operand2.substring(1, 1 + eLength), 2);
        if (operand1Exponent == 0){
            //如果指数是全0，那么指数的真实值为1，因为阶码已经考虑了隐藏位
            operand1Exponent++;
        }
        if (operand2Exponent == 0){
            operand2Exponent++;
        }
        String operand1Sig = getSignificand(operand1, eLength, sLength);  //get the two significands including the implicit bit
        String operand2Sig = getSignificand(operand2, eLength, sLength);
        for (int i = 0; i < gLength; i++) {  //add the guard bits
            operand1Sig += "0";
            operand2Sig += "0";
        }
        int exponent = operand1Exponent - operand2Exponent+ bias;
        // 无符号数的除法
        String temp = unsignedDivision(operand1Sig, operand2Sig);
        StringBuilder answer = new StringBuilder();
        //特殊除法，第一位就是隐藏位
        while (temp.charAt(0) == '0' && exponent > 0) {
            temp = leftShift(temp, "1");
            exponent--;
        }
        //右移规格化
        while (!allZero(temp.substring(0, 1 + sLength)) && exponent < 0) {
            temp = rightShift(temp, 1);
            exponent++;
        }
        //边界值
        if (exponent >= bias * 2 + 1) {
            answer.append("1");
            answer.append(sign);
            for (int i = 0; i < eLength; i++) answer.append(1);
            answer.append(getAllZeros(sLength));
            return answer.toString();
        }
        //非规格化数
        if (exponent == 0) {
            temp = rightShift(temp, 1);
        }
        //乘法和除法有可能指数小于0，指数下溢，处理成0
        if (exponent < 0) {
            answer.append("0" + sign);
            answer.append(getAllZeros(sLength + eLength));
            return answer.toString();
        }
        String ans_exponent = Integer.toBinaryString(exponent);
        int len = ans_exponent.length();
        //补齐到eLength长度
        for (int i = 0; i < eLength - len; i++){
            ans_exponent = "0" + ans_exponent;
        }

        return "0" + round((char) (sign + 48), ans_exponent, temp);
    }

    /**
     * 无符号数的除法
     */
    public String unsignedDivision(String operand1, String operand2) {
        String quotient = "";
        String product = operand1;
        //0扩展
        for (int i = 0; i < operand1.length(); i++) {
            product += "0";
        }
        for (int i = 0; i < operand1.length(); i++) {
            String temp = carry_adder(product.substring(0, operand1.length()), negation(operand2), '1', operand2.length()).substring(1);
            if (temp.charAt(0) == '0') {
                product = temp.substring(1) + product.substring(operand1.length()) + "1";
            }else{
                product = leftShift(product, "1");
            }
        }
        quotient = product.substring(operand1.length());
        return quotient;
    }

    /**
     * 得到一个长度为length的字符串
     */
    public String getAllZeros(int length) {
        StringBuffer res = new StringBuffer();
        for (int i = 0; i < length; i++) {
            res.append('0');
        }
        return res.toString();
    }

    /**
     * left shift a num using its string format
     * e.g. "00001001" left shift 2 bits -> "00100100"
     *
     * @param operand to be moved
     * @param n       moving nums of bits
     * @return after moving
     */
    public String leftShift(String operand, String n) {
        StringBuffer result = new StringBuffer(operand.substring(Integer.parseInt(n)));  //保证位数不变
        for (int i = 0; i < Integer.parseInt(n); i++) {
            result = result.append("0");
        }
        return result.toString();
    }

    /**
     * right shift a num without considering its sign using its string format
     *
     * @param operand to be moved
     * @param n       moving nums of bits
     * @return after moving
     */
    private String rightShift(String operand, int n) {
        StringBuilder result = new StringBuilder(operand);  //保证位数不变
        boolean sticky = false;
        for (int i = 0; i < n; i++) {
            sticky = sticky || result.toString().endsWith("1");
            result.insert(0, "0");
            result.deleteCharAt(result.length() - 1);
        }
        if (sticky) {
            result.replace(operand.length() - 1, operand.length(), "1");
        }
        return result.substring(0, operand.length());
    }

    /**
     * 查看字符串是否全部为0
     */
    public boolean allZero(String string) {
        for (char c : string.toCharArray()) {
            if (c != '0') {
                return false;
            }
        }
        return true;
    }

    /**
     * get the significand bits includes the implicit bit considering the subnormal number
     *
     * @param operand number string in the binary format
     * @param eLength exponent's length
     * @param sLength significand's length
     * @return result string including the implicit bit
     */
    public String getSignificand(String operand, int eLength, int sLength) {
        String exponent = operand.substring(1, 1 + eLength);
        if (Integer.valueOf(exponent) == 0) {
            return "0" + operand.substring(1 + eLength);  //subnormal number
        }else{
            return "1" + operand.substring(1 + eLength);
        }
    }

    /**
     * given a length, make operand to that digits considering the sign
     *
     * @param operand given num
     * @param length  make complete
     * @return completed string
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

    /**
     * add two nums with the length of given length which could be divided by 4, and the result's first bit presents the overflow
     * different from the {@code adder} method, the result's first bit presents whether it generates the carry
     *
     * @param operand1 first
     * @param operand2 second
     * @param c        original carray
     * @param length   given length
     * @return result, and the result's first bit presents the carry
     */
    public String carry_adder(String operand1, String operand2, char c, int length) {
        operand1 = impleDigits(operand1, length);
        operand2 = impleDigits(operand2, length);
        String res = "";
        char carry = c;
        for (int i = length - 1; i >= 0; i--) {
            String temp = fullAdder(operand1.charAt(i), operand2.charAt(i), carry);
            carry = temp.charAt(0);
            res = temp.charAt(1) + res;
        }
        return carry + res;
    }

    /**
     * add one to the operand
     *
     * @param operand the operand
     * @return result after adding, the first position means overflow (not equal to the carray to the next) and the remains means the result
     */
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
            res[i] = (char) ('0' + bit);  //显示转化为char
        }
        String result = new StringBuffer(new String(res)).reverse().toString();
        return "" + (result.charAt(0) == operand.charAt(0) ? '0' : '1') + result;  //注意有进位不等于溢出，溢出要另外判断
    }


    /**
     * the 2 bits' full adder
     *
     * @param x first char
     * @param y second char
     * @param c carry from the former bit
     * @return result after adding, the first position means the carry to the next and second means the result in this position
     */
    public String fullAdder(char x, char y, char c) {
        int bit = (x - '0') ^ (y - '0') ^ (c - '0');  //三位异或
        int carry = ((x - '0') & (y - '0')) | ((y - '0') & (c - '0')) | ((x - '0') & (c - '0'));  //有两位为1则产生进位
        return "" + carry + bit;  //第一个空串让后面的加法都变为字符串加法
    }

    /**
     * convert the string's 0 and 1.
     * e.g 00000 to 11111
     *
     * @param operand string to convert (by default, it is 32 bits long)
     * @return string after converting
     */
    public String negation(String operand) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < operand.length(); i++) {
            result = operand.charAt(i) == '1' ? result.append("0") : result.append("1");
        }
        return result.toString();
    }

    /**
     * 对GRS保护位进行舍入
     *
     * @param sign    符号位
     * @param exp     阶码
     * @param sig_grs 带隐藏位和保护位的尾数
     * @return 舍入后的结果
     */
    private String round(char sign, String exp, String sig_grs) {
        int grs = Integer.parseInt(sig_grs.substring(24, 27), 2);
        if ((sig_grs.substring(27).contains("1")) && (grs % 2 == 0)) {
            grs++;
        }
        String sig = sig_grs.substring(0, 24); // 隐藏位+23位
        if (grs > 4 || (grs == 4 && sig.endsWith("1"))) {
            sig = oneAdder(sig);
            if (sig.charAt(0) == '1') {
                exp = oneAdder(exp).substring(1);
                sig = sig.substring(1);
            }
        }

        if (Integer.parseInt(sig.substring(0, sig.length() - 23), 2) > 1) {
            sig = rightShift(sig, 1);
            exp = oneAdder(exp).substring(1);
        }
        if (exp.equals("11111111")) {
            return sign == '0' ? IEEE754Float.P_INF : IEEE754Float.N_INF;
        }

        return sign + exp + sig.substring(sig.length() - 23);
    }
}
