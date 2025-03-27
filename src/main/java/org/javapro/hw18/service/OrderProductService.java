package org.javapro.hw18.service;

import lombok.AllArgsConstructor;
import org.javapro.hw18.converter.ProductConverter;
import org.javapro.hw18.dto.ProductDto;
import org.javapro.hw18.entity.OrderProduct;
import org.javapro.hw18.entity.OrderProductId;
import org.javapro.hw18.repository.OrderProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class OrderProductService {
    private OrderProductRepository ordersProductsRepository;
    private ProductService productService;

    public List<OrderProduct> getProductsByOrderId(int orderId) {
        return ordersProductsRepository.findOrderByOrderId(orderId);
    }

    public Optional<OrderProduct> getProductByOrderProductId(OrderProductId orderProductId) {
        return ordersProductsRepository.findById(orderProductId);
    }

    public void saveProductToOrder(OrderProduct orderProduct) {
        ordersProductsRepository.save(orderProduct);
    }

    public int deleteAllProductsFromOrderById(int orderId) {
        return ordersProductsRepository.deleteAllByOrderId(orderId);
    }


    public void deleteProductFromOrderById(OrderProduct orderProduct) {
        ordersProductsRepository.deleteById(orderProduct.getId());
    }

    public void updateProductInOrderById(OrderProductId orderProductId, ProductDto productDto) {
        productService.validateExistingProducts(List.of(productDto));
        OrderProduct Product = getProductByOrderProductId(orderProductId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Product doesn't exist in this order."));

        Product.setQuantity(productDto.getQuantity());
        ordersProductsRepository.save(Product);
    }
}
