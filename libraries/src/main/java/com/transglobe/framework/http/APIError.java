package com.transglobe.framework.http;

import static org.springframework.util.StringUtils.hasText;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * 錯誤結構
 *
 * @author Matt Ho
 */
@Schema(description = "錯誤結構")
@Data
@Accessors(chain = true)
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
public class APIError {

  /**
   * 錯誤的 Http Status 不會輸出到 body
   *
   * <p>
   * 如果本 error 有指定此欄位會優先採用; 沒指定的話就看 {@codeø GlobalRestControllerExceptionHandler} 所指定的 status
   */
  @Nullable
  @JsonIgnore
  protected HttpStatus status;

  /**
   * 錯誤代碼
   */
  @Schema(description = "錯誤代碼")
  @NonNull
  protected String code;

  /**
   * 錯誤訊息
   */
  @Schema(description = "錯誤訊息")
  protected String message;

  /**
   * 錯誤明細
   */
  @ArraySchema
  @Singular
  protected List<? extends APIErrorDetail> details;

  public String shortSummary() {
    var sb = new StringBuilder(code);
    if (hasText(message)) {
      sb.append(": ").append(message);
    }
    if (!details.isEmpty()) {
      var delimiter = ", ";
      sb.append(": [");
      details.forEach(detail -> sb.append(detail.shortSummary()).append(delimiter));
      sb.replace(sb.length() - delimiter.length(), sb.length(), "]");
    }
    return sb.toString();
  }
}
