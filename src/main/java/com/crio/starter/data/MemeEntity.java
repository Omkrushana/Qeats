package com.crio.starter.data;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "memes")
public class MemeEntity {
  @Id
  private String id;
  @NotNull(message = "Name may not be null")
  @NotEmpty(message = "Name may not be null")
  private String name;

  @NotNull(message = "Url may not be null")
  private String url;

  @NotNull(message = "Caption may not be null")
  private String caption;
}
