package org.iteso.duplicated.resources;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.Value;
import org.antlr.v4.runtime.ParserRuleContext;
import org.iteso.duplicated.application.ApplicationService;
import org.iteso.duplicated.application.ComparisonCommand;
import org.iteso.duplicated.model.AstSubTree;
import org.iteso.duplicated.model.CloneDetector.ClonePair;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comparisons")
public class ComparisonsResource {

  private ApplicationService service;

  public ComparisonsResource(ApplicationService service) {
    this.service = service;
  }

  @PostMapping
  public List<CloneRepresentation> compare(@Valid @RequestBody ComparisonCommand command) {
    Set<ClonePair> clones = service.compare(command);
    return parse(clones);
  }

  private List<CloneRepresentation> parse(Set<ClonePair> clones) {
    return clones
        .stream()
        .map(CloneRepresentation::new)
        .collect(Collectors.toList());
  }

  @Value
  public static class CloneRepresentation {

    private TextClone left;
    private TextClone right;
    private double similarity;

    public CloneRepresentation(ClonePair pair) {
      left = new TextClone(pair.getLeftPart());
      right = new TextClone(pair.getRightPart());
      similarity = pair.getSimilarity();
    }
  }

  @Value
  public static class TextClone {

    private LinePosition start;
    private LinePosition end;

    public TextClone(AstSubTree tree) {
      ParserRuleContext rule = tree.getData();
      start = new LinePosition(rule.start.getLine(), rule.start.getCharPositionInLine());
      end = new LinePosition(rule.stop.getLine(), rule.stop.getCharPositionInLine());
    }
  }

  @Value
  public static class LinePosition {

    private int line;
    private int position;

  }
}
