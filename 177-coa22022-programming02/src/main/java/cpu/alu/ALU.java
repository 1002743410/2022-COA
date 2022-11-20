package cpu.alu;

import util.DataType;
import util.Transformer;

import java.util.Deque;
import java.util.LinkedList;

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
     * 返回两个二进制整数的和
     * dest + src
     *
     * @param src  32-bits
     * @param dest 32-bits
     * @return 32-bits
     */
    public DataType add(DataType src, DataType dest) {
        // TODO
        int src_position, dest_position, result_position;
        Deque stack = new LinkedList();
        String result = "";

        for (int i = 31; i >= 0; i--) {

            src_position = Integer.parseInt(src.toString().substring(i, i + 1));
            dest_position = Integer.parseInt(dest.toString().substring(i, i + 1));

            if (src_position + dest_position + Integer.parseInt(CF) < 2) {
                result_position = src_position + dest_position + Integer.parseInt(CF);
                CF = "0";
            } else {
                result_position = src_position + dest_position + Integer.parseInt(CF) - 2;
                CF = "1";
            }
            stack.push(result_position);
        }

        while (!stack.isEmpty()) {
            result+=String.valueOf(stack.pop());
        }

        CF="0";//还原进位标志位

        return new DataType(result);

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
        String src_negative = "";//(-src)
        String one = "00000000000000000000000000000001";
        //取反
        for (int i = 0; i < 32; i++) {
            if (src.toString().charAt(i) == '1') {
                src_negative += "0";
            } else {
                src_negative += "1";
            }
        }

        //dest-src=dest+(-src)
        return add(dest, add(new DataType(src_negative), new DataType(one)));

    }

}
