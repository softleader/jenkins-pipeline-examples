package com.transglobe.framework.http;

import static org.springframework.util.StringUtils.hasText;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 錯誤明細
 *
 * @author Matt Ho
 */
@Schema(description = "錯誤明細")
@Data
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
public class APIErrorDetail {

  /** 錯誤代碼 */
  @Schema(description = "錯誤代碼", nullable = true)
  protected String code;

  /** 錯誤訊息 */
  @Schema(description = "錯誤訊息")
  protected String message;

  public String shortSummary() {
    var sb = new StringBuilder();
    if (hasText(code)) {
      sb.append(code);
    }
    if (hasText(message)) {
      if (sb.length() > 0) {
        sb.append("/");
      }
      sb.append(message);
    }
    return sb.toString();
  }
}
