package util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TransformerTest {

    @Test
    public void intToBinaryTest1() {
        assertEquals("00000000000000000000000000000010", Transformer.intToBinary("2"));
    }

    @Test
    public void intToBinaryTest2() {
        assertEquals("10000000000000000000000000000000", Transformer.intToBinary("-2147483648"));
    }

    @Test
    public void intToBinaryTest3() {
        assertEquals("00000000000000000000000000001001", Transformer.intToBinary("9"));
    }

    @Test
    public void intToBinaryTest4() {
        assertEquals("00000000000000000000000000101001", Transformer.intToBinary("41"));
    }

    @Test
    public void intToBinaryTest5() {
        assertEquals("11111111111111111111111111111111", Transformer.intToBinary("-1"));
    }

    @Test
    public void binaryToIntTest1() {
        assertEquals("2", Transformer.binaryToInt("00000000000000000000000000000010"));
    }

    @Test
    public void binaryToIntTest2() {
        assertEquals("-2147483648", Transformer.binaryToInt("10000000000000000000000000000000"));
    }

    @Test
    public void binaryToIntTest3() {
        assertEquals("2147483647", Transformer.binaryToInt("01111111111111111111111111111111"));
    }

    @Test
    public void binaryToIntTest4() {
        assertEquals("-1", Transformer.binaryToInt("11111111111111111111111111111111"));
    }

    @Test
    public void decimalToNBCDTest1() {
        assertEquals("11000000000000000000000000010000", Transformer.decimalToNBCD("10"));
    }

    @Test
    public void decimalToNBCDTest2() {
        assertEquals("11000000000010010000100100011001", Transformer.decimalToNBCD("90919"));
    }

    @Test
    public void decimalToNBCDTest3() {
        assertEquals("11000000000100000000000000001001", Transformer.decimalToNBCD("100009"));
    }

    @Test
    public void NBCDToDecimalTest1() {
        assertEquals("10", Transformer.NBCDToDecimal("11000000000000000000000000010000"));
    }

    @Test
    public void NBCDToDecimalTest2() {
        assertEquals("90919", Transformer.NBCDToDecimal("11000000000010010000100100011001"));
    }

    @Test
    public void NBCDToDecimalTest3() {
        assertEquals("-90919", Transformer.NBCDToDecimal("11010000000010010000100100011001"));
    }

    @Test
    public void NBCDToDecimalTest4() {
        assertEquals("100509", Transformer.NBCDToDecimal("11000000000100000000010100001001"));
    }

    @Test
    public void NBCDToDecimalTest5() {
        assertEquals("-9", Transformer.NBCDToDecimal("11010000000000000000000000001001"));
    }

    @Test
    public void NBCDToDecimalTest6() {
        assertEquals("-9000009", Transformer.NBCDToDecimal("11011001000000000000000000001001"));
    }

    @Test
    public void NBCDToDecimalTest7() {
        assertEquals("-9000009", Transformer.NBCDToDecimal("11011001000000000000000000001001"));
    }

    @Test
    public void NBCDToDecimalTest8() {
        assertEquals("0", Transformer.NBCDToDecimal("11010000000000000000000000000000"));
    }

    @Test
    public void floatToBinaryTest1() {
        assertEquals("00000000010000000000000000000000", Transformer.floatToBinary(String.valueOf(Math.pow(2, - 127))));
    }

    @Test
    public void floatToBinaryTest2() {
        assertEquals("+Inf", Transformer.floatToBinary("" + Double.MAX_VALUE)); // 对于float来说溢出
    }

    @Test
    public void floatToBinaryTest3() {
        assertEquals("-Inf", Transformer.floatToBinary("-" + Double.MAX_VALUE)); // 对于float来说溢出
    }

    @Test
    public void floatToBinaryTest4() {
        assertEquals("01111111000000000000000000000000", Transformer.floatToBinary(String.valueOf(Math.pow(2, 127))));
    }

    @Test
    public void floatToBinaryTest5() {
        assertEquals("01000100100000101000101000111101", Transformer.floatToBinary(String.valueOf(1044.32)));
    }

    @Test
    public void floatToBinaryTest6() {
        assertEquals("00111111101010001111010111000011", Transformer.floatToBinary(String.valueOf(1.32)));
    }

    @Test
    public void floatToBinaryTest7() {
        assertEquals("10111111000000000000000000000000", Transformer.floatToBinary(String.valueOf(- 0.5)));
    }

    @Test
    public void floatToBinaryTest8() {
        assertEquals("10000000001000000000000000000000", Transformer.floatToBinary(String.valueOf(- (float) Math.pow(2, - 128))));
    }

    @Test
    public void floatToBinaryTest9() {
        assertEquals("10000000100000000000000000000000", Transformer.floatToBinary(String.valueOf(- (float) Math.pow(2, - 126))));
    }

    @Test
    public void binaryToFloatTest1() {
        assertEquals(String.valueOf((float) Math.pow(2, - 127)), Transformer.binaryToFloat("00000000010000000000000000000000"));
    }

    @Test
    public void binaryToFloatTest2() {
        assertEquals(String.valueOf((float) Math.pow(2, 127)), Transformer.binaryToFloat("01111111000000000000000000000000"));
    }

    @Test
    public void binaryToFloatTest3() {
        assertEquals(String.valueOf(1044.32), Transformer.binaryToFloat("01000100100000101000101000111101"));
    }

    @Test
    public void binaryToFloatTest4() {
        assertEquals(String.valueOf(1.32), Transformer.binaryToFloat("00111111101010001111010111000011"));
    }

    @Test
    public void binaryToFloatTest5() {
        assertEquals(String.valueOf(- 0.5), Transformer.binaryToFloat("10111111000000000000000000000000"));
    }

    @Test
    public void binaryToFloatTest6() {
        assertEquals(String.valueOf(- (float) Math.pow(2, - 128)), Transformer.binaryToFloat("10000000001000000000000000000000"));
    }

    @Test
    public void binaryToFloatTest7() {
        assertEquals(String.valueOf(- (float) Math.pow(2, - 126)), Transformer.binaryToFloat("10000000100000000000000000000000"));
    }

}
