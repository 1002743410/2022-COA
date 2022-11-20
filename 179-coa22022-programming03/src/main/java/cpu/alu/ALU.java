package cpu.alu;


import util.DataType;

/**
 * Arithmetic Logic Unit
 * ALU封装类
 */
public class ALU {

    // 模拟寄存器中的进位标志位
    private String CF = "0";

    // 模拟寄存器中的溢出标志位
    private String OF = "0";

    /**
     * 存放32位二进制整数
     */
    public class BinaryIntegers {

        public static final String ZERO = "00000000000000000000000000000000";

        public static final String NaN = "NaN";

        public static final String NegativeOne = "11111111111111111111111111111111";

    }

    /**
     * 存放符合IEEE-754标准的特殊浮点数
     */
    public class IEEE754Float {

        public static final String P_ZERO = "00000000000000000000000000000000";  // 0X0           positive zero

        public static final String N_ZERO = "10000000000000000000000000000000";  // 0X80000000    negative zero

        public static final String P_INF = "01111111100000000000000000000000";  // 0X7f800000    positive infinity

        public static final String N_INF = "11111111100000000000000000000000";  // 0Xff800000    negative infinity

        public static final String NaN = "(0|1){1}1{8}(0+1+|1+0+)(0|1)*";  // Not_A_Number

    }

    String reverse(String s){
        char[] resultChar = s.toCharArray();
        boolean flag = false;
        for (int i = s.length() - 1; i >= 0; i--){
            if(!flag){
                if(resultChar[i] == '1'){
                    flag = true;
                }
            }
            else{
                if(resultChar[i] == '1'){

                    resultChar[i] = '0';

                }
                else{
                    resultChar[i] = '1';
                }
            }
        }
        return String.valueOf(resultChar);
    }

    String add(String src, String dest) {
        StringBuilder result = new StringBuilder();
        CF = "0";
        for(int i = dest.length() - 1; i >= 0; --i){
            int sum = (src.charAt(i) - '0') + (dest.charAt(i) - '0') + (CF.charAt(0) - '0');
            if (sum == 3) {
                result.append("1");
                CF = "1";
            }
            else if (sum == 2) {
                result.append("0");
                CF = "1";
            }
            else if (sum == 1) {
                result.append("1");
                CF = "0";
            }
            else if (sum == 0) {
                result.append("0");
                CF = "0";
            }
        }
        result.reverse();
        if(src.charAt(0) == dest.charAt(0) && result.charAt(0) != dest.charAt(0)){
            OF = "1";
        }
        else{
            OF = "0";
        }
        return result.toString();
    }

    /**
     * 返回两个二进制整数的乘积(结果低位截取后32位)
     * dest * src
     *
     * @param src  32-bits
     * @param dest 32-bits
     * @return 32-bits
     */
    public DataType mul(DataType src, DataType dest) {
        // TODO
        if(src.equals(BinaryIntegers.ZERO) || dest.equals(BinaryIntegers.ZERO)){
            return new DataType(BinaryIntegers.ZERO);
        }
        String x = dest + "000000000000000000000000000000000";
        String _x = reverse(dest.toString()) + "000000000000000000000000000000000";
        String y = "00000000000000000000000000000000" + src + "0";
        for(int i = 0; i < src.toString().length(); ++i){
            if(y.substring(y.length() - 2).equals("10")){
                y = add(y, _x);
            }
            else if(y.substring(y.length() - 2).equals("01")){
                y = add(y, x);
            }
            y = y.substring(0, 1) + y.substring(0, y.length() - 1);
        }
        return new DataType(y.substring(y.length() - 33, y.length() - 1));
    }

}
