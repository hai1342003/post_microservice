    package com.example.order_service.service.impl;

    import com.example.order_service.config.RabbitMQConfig;
    import com.example.order_service.dto.*;
    import com.example.order_service.entity.Address;
    import com.example.order_service.entity.Order;
    import com.example.order_service.entity.OrderItem;
    import com.example.order_service.entity.OrderStatus;
    import com.example.order_service.entity.PaymentMethod;
    import com.example.order_service.repository.OrderItemRepository;
    import com.example.order_service.repository.OrderRepository;
    import com.example.order_service.service.*;

    import lombok.RequiredArgsConstructor;
    import org.springframework.amqp.rabbit.core.RabbitTemplate;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;

    import java.time.LocalDate;
    import java.time.LocalDateTime;
    import java.util.ArrayList;
    import java.util.List;
    import java.util.UUID;


    @Service
    @RequiredArgsConstructor
    public class OrderServiceImpl implements OrderService {



        private final OrderRepository orderRepository;
        private final OrderItemRepository orderItemRepository;

        private final UserClient userClient;

        private final StripeService stripeService;


        private final DeliveryClient deliveryClient;

        private final EmailClient emailClient;

        private RabbitTemplate rabbitTemplate;


        @Autowired
        public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
            this.rabbitTemplate = rabbitTemplate;
        }


        @Override
        public Order datDonHangCOD(OrderDTO request) {
            Order order = new Order();



            UserDTO userDTO = userClient.layThongTinNguoiDungDangNhap();
            order.setUserId(userDTO.getId());
            order.setAmount(request.getAmount());


            AddressDTO addressDTO = request.getAddress();
            Address address = new Address();
            address.setFirstName(addressDTO.getFirstName());
            address.setLastName(addressDTO.getLastName());
            address.setEmail(addressDTO.getEmail());
            address.setStreet(addressDTO.getStreet());
            address.setCity(addressDTO.getCity());
            address.setState(addressDTO.getState());
            address.setZipCode(addressDTO.getZipCode());
            address.setCountry(addressDTO.getCountry());
            address.setPhone(addressDTO.getPhone());
            order.setAddress(address);

            order.setPaymentMethod(PaymentMethod.COD);
            order.setPayment(false);
            order.setStatus(OrderStatus.ORDER_PLACED);
            order.setDate(LocalDateTime.now());




            List<OrderItem> orderItems = new ArrayList<>();
            for (OrderItemDTO itemDTO : request.getItems()) {
                OrderItem item = new OrderItem();
                item.setProductId(itemDTO.getProductId());
                item.setQuantity(itemDTO.getQuantity());
                item.setRam(itemDTO.getRam());
                item.setOrder(order);
                item.setName(itemDTO.getName());
                item.setPrice(itemDTO.getPrice());
                item.setImage1(itemDTO.getImage1());
                orderItems.add(item);
            }

            order.setItems(orderItems);
            orderRepository.save(order);
            orderItemRepository.saveAll(orderItems);

            System.out.println("Received order: " + request);

            // GỌI DELIVERY SERVICE
            DeliveryDTO deliveryDTO = new DeliveryDTO();
            deliveryDTO.setTrackingNumber(UUID.randomUUID().toString().substring(0, 8));
            deliveryDTO.setStatus("PENDING");
            deliveryDTO.setDeliveryDate(LocalDate.now().plusDays(3));
            deliveryDTO.setOriginAddress("Main Warehouse");
            deliveryDTO.setDestinationAddress(address.getStreet() + ", " + address.getCity() + ", " + address.getState());
            deliveryDTO.setShippingCost(10.0);
            deliveryDTO.setDeliveryMethod("COD");
            deliveryDTO.setOrderId(order.getId());



            try {
//                deliveryClient.createDelivery(deliveryDTO);
                DeliveryMessage message = new DeliveryMessage();
                message.setOrderId(order.getId());
                message.setTrackingNumber(UUID.randomUUID().toString().substring(0, 8));
                message.setStatus("PENDING");
                message.setOriginAddress("Main Warehouse");
                message.setDestinationAddress(address.getStreet() + ", " + address.getCity() + ", " + address.getState());
                message.setShippingCost(10.0);
                message.setDeliveryMethod("COD");
                message.setDeliveryDate(LocalDate.now().plusDays(3));

                rabbitTemplate.convertAndSend(
                        RabbitMQConfig.ORDER_EXCHANGE,
                        RabbitMQConfig.ORDER_ROUTING_KEY,
                        message
                );

                OrderEmailRequest emailRequest = new OrderEmailRequest();
                emailRequest.setEmail(userDTO.getEmail());
                emailRequest.setCustomerName(userDTO.getName());
                emailRequest.setOrderId(order.getId());
                emailRequest.setStatus(order.getStatus());
                emailRequest.setItems(request.getItems());
                emailRequest.setTotalAmount(order.getAmount());
                emailRequest.setPaymentMethod(order.getPaymentMethod().name());
                emailRequest.setAddress(address.getStreet() + ", " + address.getCity() + ", " + address.getState());
                emailRequest.setDate(order.getDate().toString());
                emailClient.sendOrderEmail(emailRequest);


                System.out.println("✅ Tạo delivery thành công cho OrderId: " + order.getId());
            } catch (Exception e) {
                System.err.println("❌ Lỗi khi tạo delivery: " + e.getMessage());
            }


            return order;
        }

        @Override
        public Order luuTamThoiDonHang(OrderDTO request) {
            Order order = datDonHangCOD(request); // dùng lại logic cũ
            order.setPaymentMethod(PaymentMethod.STRIPE);
            order.setPayment(false);
            orderRepository.save(order);
            return order;
        }


        @Override
        public String datDonHangStripe(OrderDTO request) {
            Order savedOrder = luuTamThoiDonHang(request); // tạo trước Order có id
            request.setId(savedOrder.getId()); // gán id lại cho DTO
            return stripeService.taoSessionStripe(request);
        }

        @Override
        public Order datDonHangRazorpay(OrderDTO request) {
            // TODO
            return null;
        }

        @Override
        public List<Order> layTatCaDonHang() {
            return orderRepository.findAll();
        }

        @Override
        public List<Order> layDonHangCuaUser(Long userId) {
            return orderRepository.findByUserId(userId);
        }

        @Override
        public void capNhatTrangThai(Long orderId, String status) {
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng với ID: " + orderId));

            order.setStatus(OrderStatus.valueOf(status));
            orderRepository.save(order);
        }


        @Override
        public void xacNhanThanhToanStripe(Long orderId) {
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng với ID: " + orderId));


            System.out.println("Xác nhận thanh toán cho orderId = " + orderId);

            order.setPayment(true);
            order.setStatus(OrderStatus.ORDER_PLACED);
            orderRepository.save(order);


            try {
                DeliveryDTO deliveryDTO = new DeliveryDTO();
                deliveryDTO.setTrackingNumber(UUID.randomUUID().toString().substring(0, 8));
                deliveryDTO.setStatus("PENDING");
                deliveryDTO.setDeliveryDate(LocalDate.now().plusDays(3));
                deliveryDTO.setOriginAddress("Main Warehouse");

                Address address = order.getAddress();
                deliveryDTO.setDestinationAddress(
                        address.getStreet() + ", " + address.getCity() + ", " + address.getState()
                );
                deliveryDTO.setShippingCost(10.0);
                deliveryDTO.setDeliveryMethod("STRIPE");
                deliveryDTO.setOrderId(order.getId());

                deliveryClient.createDelivery(deliveryDTO);
                System.out.println("✅ Tạo delivery thành công cho đơn Stripe OrderId: " + order.getId());

                UserDTO userDTO = userClient.layThongTinNguoiDungDangNhap();

                OrderEmailRequest emailRequest = new OrderEmailRequest();
                emailRequest.setEmail(userDTO.getEmail());
                emailRequest.setCustomerName(userDTO.getName());
                emailRequest.setOrderId(order.getId());
                emailRequest.setStatus(order.getStatus());

                List<OrderItemDTO> itemDTOs = new ArrayList<>();
                for (OrderItem item : order.getItems()) {
                    OrderItemDTO dto = new OrderItemDTO();
                    dto.setProductId(item.getProductId());
                    dto.setQuantity(item.getQuantity());
                    dto.setRam(item.getRam());
                    dto.setName(item.getName());
                    dto.setPrice(item.getPrice());
                    dto.setImage1(item.getImage1());
                    itemDTOs.add(dto);
                }
                emailRequest.setItems(itemDTOs);

                emailRequest.setTotalAmount(order.getAmount());
                emailRequest.setPaymentMethod(order.getPaymentMethod().name());
                emailRequest.setAddress(order.getAddress().getStreet() + ", " + order.getAddress().getCity() + ", " + order.getAddress().getState());
                emailRequest.setDate(order.getDate().toString());

                emailClient.sendOrderEmail(emailRequest);

            } catch (Exception e) {
                System.err.println("❌ Lỗi khi tạo delivery đơn Stripe: " + e.getMessage());
            }
        }

    }
