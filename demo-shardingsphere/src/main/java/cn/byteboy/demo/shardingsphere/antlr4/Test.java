package cn.byteboy.demo.shardingsphere.antlr4;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

/**
 * @author hongshaochuan
 */
public class Test {

    public static void main(String[] args) {
        String query = "3.1 * (6.3 - 4.51) + 5 * 4";
        CalculatorLexer lexer = new CalculatorLexer(new ANTLRInputStream(query));
        CalculatorParser parser = new CalculatorParser(new CommonTokenStream(lexer));
        CalculatorVisitor visitor = new MyCalculatorVisitor();
//        CalculatorVisitor visitor = new CalculatorBaseVisitor();

        System.out.println(visitor.visit(parser.expr()));  // 25.549
    }
}
