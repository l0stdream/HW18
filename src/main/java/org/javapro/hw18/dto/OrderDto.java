package org.javapro.hw18.dto;

import lombok.*;


import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto {
    private int id;
    private  String comment;
    private List<ProductDto> products;
}
