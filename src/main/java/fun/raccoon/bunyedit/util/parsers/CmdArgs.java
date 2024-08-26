package fun.raccoon.bunyedit.util.parsers;

import java.util.List;

import org.jparsec.Parser;
import org.jparsec.Scanners;
import org.jparsec.pattern.Patterns;

public class CmdArgs {
    public static List<String> parse(String argString) {
        // arg, no parens, no whitespace
        Parser<String> simpleArg =
            Scanners.many1(c ->
                !(Character.isWhitespace(c) || c == '(' || c == ')'))
            .source();

        // arg, surrounded by parens, maybe whitespace inside
        Parser<String> compoundArg =
            Scanners.notAmong("-()")
            .followedBy(
                Scanners.notAmong("()").many())
            .source()
            .between(
                Patterns.isChar('(').toScanner("parens"),
                Patterns.isChar(')').toScanner("parens"));                

        Parser<String> arg = simpleArg.or(compoundArg);

        Parser<List<String>> args =
            // maybe some whitespace
            Scanners.WHITESPACES.optional(null)
            // then a list of args seperated and maybe terminated by whitespace
            .next(arg.sepEndBy(Scanners.WHITESPACES));
        
        return args.parse(argString);
    }
}
