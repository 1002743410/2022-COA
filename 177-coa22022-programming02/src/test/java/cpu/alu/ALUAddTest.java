package cpu.alu;

import org.junit.Test;
import util.DataType;
import util.Transformer;

import static org.junit.Assert.assertEquals;

public class ALUAddTest {

    private final ALU alu = new ALU();
    private DataType src;
    private DataType dest;
    private DataType result;

    @Test
    public void AddTest1() {
        src = new DataType("00000000000000000000000000000100");
        dest = new DataType("00000000000000000000000000000100");
        result = alu.add(src, dest);
        assertEquals("00000000000000000000000000001000", result.toString());
    }

    @Test
    public void AddTest2() {
        src = new DataType("00000000000000000000000000000100");
        dest = new DataType("00000000000000000000000000000000");
        result = alu.add(src, dest);
        assertEquals("00000000000000000000000000000100", result.toString());
    }

    @Test
    public void AddTest3() {
        src = new DataType("11111111111111111111111111110100");
        dest = new DataType("11111111111111111111111111110100");
        result = alu.add(src, dest);
        assertEquals("11111111111111111111111111101000", result.toString());
    }

}
