    package com.example.order_service.service.impl;

    import com.example.order_service.dto.*;
    import com.example.order_service.entity.Address;
    import com.example.order_service.entity.Order;
    import com.example.order_service.entity.OrderItem;
    import com.example.order_service.entity.OrderStatus;
    import com.example.order_service.entity.PaymentMethod;
    import com.example.order_service.repository.OrderItemRepository;
    import com.example.order_service.repository.OrderRepository;
    import com.example.order_service.service.DeliveryClient;
    import com.example.order_service.service.OrderService;
    import com.example.order_service.service.StripeService;
    import com.example.order_service.service.UserClient;

    import lombok.RequiredArgsConstructor;
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
                deliveryClient.createDelivery(deliveryDTO);
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
            } catch (Exception e) {
                System.err.println("❌ Lỗi khi tạo delivery đơn Stripe: " + e.getMessage());
            }
        }

    }
