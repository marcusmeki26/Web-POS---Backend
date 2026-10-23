package com.project.webpos.Logging;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder 
public class LoggerFormat {
  @Builder.Default
  private String action_timestamp = LocalDateTime.now().toString();
  private String level;
  private String action;
  private String resourceId;
  private String actorId;
  private String endpoint;
}
