package com.crio.starter.exchange;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemeDto {
    @NotBlank(message = "Name cannot be empty")
    private String name;
    @NotBlank(message = "URL cannot be empty")
    private String url;
    @NotBlank(message = "Caption cannot be empty")
    private String caption;
}
