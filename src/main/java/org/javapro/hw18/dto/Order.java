package org.javapro.hw18.dto;

import lombok.*;


import java.util.List;

@Data
@AllArgsConstructor
public class Order {
    private final int id;
    private List<Product> products;
}
