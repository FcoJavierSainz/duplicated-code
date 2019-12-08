package org.iteso.duplicated.application;

import java.util.Set;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.TokenStream;
import org.iteso.duplicated.model.CloneDetector;
import org.iteso.duplicated.model.CloneDetector.ClonePair;
import org.springframework.stereotype.Service;

@Service
public class ApplicationService {

  public Set<ClonePair> compare(ComparisonCommand command) {
    ParserRuleContext leftCst = getCst(command.getLeftCode());
    ParserRuleContext rightCst = getCst(command.getRightCode());
    CloneDetector detector = new CloneDetector(leftCst, rightCst);
    return detector.getClones();
  }

  private ParserRuleContext getCst(String text) {
    CharStream input = CharStreams.fromString(text);
    org.iteso.duplicated.antlr.Java8Lexer lexer = new org.iteso.duplicated.antlr.Java8Lexer(input);
    TokenStream tokens = new CommonTokenStream(lexer);
    org.iteso.duplicated.antlr.Java8Parser parser = new org.iteso.duplicated.antlr.Java8Parser(
        tokens);
    return parser.methodBody();
  }

}
