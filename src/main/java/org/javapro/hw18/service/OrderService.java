package org.javapro.hw18.service;


import lombok.AllArgsConstructor;
import org.javapro.hw18.converter.OrderConverter;
import org.javapro.hw18.converter.OrderProductConverter;
import org.javapro.hw18.converter.ProductConverter;
import org.javapro.hw18.dto.OrderDto;
import org.javapro.hw18.dto.ProductDto;
import org.javapro.hw18.entity.Order;
import org.javapro.hw18.entity.OrderProduct;
import org.javapro.hw18.entity.OrderProductId;
import org.javapro.hw18.repository.OrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OrderService {

    private OrderRepository orderRepository;
    private ProductService productService;
    private OrderProductService orderProductService;

    public OrderDto getOrderById(int id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order wasn't found."));

        List<OrderProduct> ordersProducts = orderProductService.getProductsByOrderId(id);
        return OrderConverter.toOrderDto(order, ordersProducts);
    }

    public void addOrder(OrderDto orderDto) {
        if (orderDto == null) {
            orderRepository.save(new Order());
        } else {
            Order order = orderRepository.save(OrderConverter.toOrder(orderDto));
            List<ProductDto> products = orderDto.getProducts();
            if (products != null && !products.isEmpty()) {
                addProductsToOrder(order.getId(), products);
            }
        }
    }


    public void addProductsToOrder(int orderId, List<ProductDto> products) {
        if (products == null || products.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Products list can't be empty");
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order wasn't found."));

        productService.validateExistingProducts(products);

        for (ProductDto productDto : products) {
                    OrderProduct ordersProducts = OrderProductConverter.toOrderProduct(
                            order,
                            ProductConverter.toProduct(productDto),
                            productDto.getQuantity());
                    orderProductService.saveProductToOrder(ordersProducts);
        }
    }

    public void deleteOrderByOrderId(int id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order wasn't found."));
        orderRepository.delete(order);
    }

    public void updateOrderById(int id, OrderDto orderDto) {
        if (!orderRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong order in request.");
        }

         orderProductService.deleteAllProductsFromOrderById(id);
         addProductsToOrder(id, orderDto.getProducts());
         orderRepository.updateById(id, orderDto.getComment());
    }

    public void deleteProductFromOrder(int orderId, int productId) {
        OrderProductId orderProductId = setOrderProductId(orderId, productId);
        Optional<OrderProduct> orderProducts = orderProductService.getProductByOrderProductId(orderProductId);

        orderProducts.ifPresentOrElse(orderProductService::deleteProductFromOrderById,
                () -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Product doesn't exist in this order.");
                });
    }

    public void changeProductInOrderByOrderId(int orderId, int productId, ProductDto productDto) {
        if (!orderRepository.existsById(orderId)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order wasn't found.");
        }
        OrderProductId orderProductId = setOrderProductId(orderId, productId);
        orderProductService.updateProductInOrderById(orderProductId, productDto);
    }

    public void deleteAllProductsFromOrderByOrderId(int orderId){
        if( orderProductService.deleteAllProductsFromOrderById(orderId)<= 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Order doesn't exist in this order.");
        }
    }

    public OrderProductId setOrderProductId(int orderId, int productId) {
        OrderProductId id = new OrderProductId();
        id.setOrderId(orderId);
        id.setProductId(productId);

        return id;
    }
}
