package org.iteso.duplicated.application;

import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Validated
@Data
public class ComparisonCommand {

  @NotNull
  private String leftCode;
  @NotNull
  private String rightCode;
}
