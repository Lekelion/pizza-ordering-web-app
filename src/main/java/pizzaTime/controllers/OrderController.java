package pizzaTime.controllers;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import pizzaTime.entities.*;
import pizzaTime.services.LoginService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/order")
public class OrderController {

    LoginService loginService;
    CustomerOrderRepo orderRepo;
    PizzaSizeRepo sizeRepo;
    PizzaCrustRepo crustRepo;

    public OrderController(LoginService loginService, CustomerOrderRepo orderRepo, PizzaSizeRepo sizeRepo, PizzaCrustRepo crustRepo) {
        this.loginService = loginService;
        this.orderRepo = orderRepo;
        this.sizeRepo = sizeRepo;
        this.crustRepo = crustRepo;
    }

    // this is a debugging tool - if you are having trouble getting value to appear in the browser
    // it's helpful to SEE what is actually in the 'Model'
    //
    void dumpModelAttributes(Model model) {
        System.out.println("Model dump:");
        for (Map.Entry<String, Object> entry : model.asMap().entrySet())
            System.out.println("  " + entry.getKey() + " = " + entry.getValue());
    }

    // findOrder & findOrderEdit are helper methods
    // they retrieve CustomerOrder entities from the database, using the orderID
    // some error checking is done to prevent un-authorized changes
    // when an error check fails, an exception is throw,
    // which gets caught by the GlobalExceptionHandler,
    // and is shown to the user using "unhandledException.HTML"
    //
    private CustomerOrder findOrder(Model model, int orderId) throws Exception {
        CustomerOrder order = orderRepo.findById(orderId).orElse(null);
        if (order == null)
            throw new Exception("Order ID not found");
        Customer c = loginService.getCustomer();
        if (order.getCustomer().getId() != c.getId())
            throw new Exception("Invalid Customer ID");
        model.addAttribute("order", order);
        return order;
    }

    private CustomerOrder findOrderEdit(Model model, int orderId) throws Exception {
        CustomerOrder order = findOrder(model, orderId);
        if (order.getOrderStatus() != CustomerOrder.OrderStatus.NEW)
            throw new Exception("Order already Submitted");
        return order;
    }

    @GetMapping("/history")
    public String orderHistory(Model model) {
        Customer c = loginService.getCustomer();
        List<CustomerOrder> orders = orderRepo.findByCustomerId(c.getId());
        System.out.println("#orders = " + orders.size());
        model.addAttribute("orders", orders);
        return "order/history";
    }

    @GetMapping("/{id}")
    public String orderDetails(Model model, @PathVariable int id) throws Exception {
        CustomerOrder order = findOrder(model, id);
        System.out.println("view order = " + order);
        return "order/details";
    }

    @GetMapping("/{id}/edit")
    public String orderEdit(Model model, @PathVariable int id) throws Exception {
        CustomerOrder order = findOrderEdit(model, id);
        System.out.println("get order = " + order);
        return "order/editOrder";
    }

    @GetMapping("/create")
    public String createGet(Model model) {
        model.addAttribute("order", new CustomerOrder());
        model.addAttribute("pizza", new CustomerPizza());
        modelSizeAndCrust(model);
        return "order/editPizza";
    }

    @GetMapping("/{id}/addPizza")
    public String pizzaCreate(Model model, @PathVariable int id) throws Exception {
        CustomerOrder order;
        if (id == 0)
            order = new CustomerOrder();
        else
            order = findOrderEdit(model, id);
        System.out.println("create pizza, o=" + order);
        model.addAttribute("order", order);
        model.addAttribute("pizza", new CustomerPizza());
        modelSizeAndCrust(model);
        return "order/editPizza";
    }

    @GetMapping("/{id}/editPizza/{n}")
    public String pizzaGet(Model model, @PathVariable int id, @PathVariable int n) throws Exception {
        CustomerOrder order = findOrderEdit(model, id);
        CustomerPizza pizza = order.getPizzaByNumber(n);
        if (pizza == null)
            throw new Exception("Pizza # not found");
        model.addAttribute("pizza", pizza);
        modelSizeAndCrust(model);
        System.out.println("edit pizza GET, p=" + pizza);
        return "order/editPizza";
    }

    private void modelSizeAndCrust(Model model) {
        model.addAttribute("PizzaSizes", sizeRepo.findAll());
        model.addAttribute("PizzaCrusts", crustRepo.findAll());
    }

    @PostMapping("/{id}/updatePizza")
    public String pizzaPost(
            Model model,
            @Valid @ModelAttribute("pizza") CustomerPizza pizza,
            BindingResult result,
            @PathVariable int id) throws Exception {

        System.out.println("edit pizza POST, p=" + pizza);

        //TODO: Step 4a - add validation for Quantity >= 1
        if (pizza.getQuantity() < 1) {
            result.rejectValue("quantity", "invalid", "Quantity must be at least 1");
        }

        if (result.hasErrors()) {
            System.out.println("  not valid");
//            for (ObjectError error : result.getAllErrors())
//                System.out.println("  " + error);
            modelSizeAndCrust(model);
            return "order/editPizza";
        }

        Customer c = loginService.getCustomer();
        CustomerOrder order = new CustomerOrder();

        //TODO: Step 4b - handle the 3 distinct case for adding a pizza to an order
        // 1 Adding the first pizza on a new order (OrderID = 0)
        // 2 Adding a new pizza to an existing order (PizzaID = 0)
        // 3 Editing a pizza on an existing order
        if (id == 0) {
            // Case 1: Adding first pizza to new order
            order = new CustomerOrder();
            order.setCustomer(c);
            order.addPizza(pizza);
        } else {
            // Case 2 & 3: Existing order
            order = findOrderEdit(model, id);

            if (pizza.getId() == 0) {
                // Case 2: Adding new pizza to existing order
                order.addPizza(pizza);
            } else {
                // Case 3: Editing existing pizza
                CustomerPizza existingPizza = order.getPizzaById(pizza.getId());
                if (existingPizza != null) {
                    existingPizza.updateFrom(pizza);
                } else {
                    throw new Exception("Pizza not found in order");
                }
            }
        }

        order.computePrice();

        //TODO: Step 4b - un-comment the save() line when code above is DONE

        // we always save through order, to ensure database price fields are accurate
//        orderRepo.save(order);
        orderRepo.save(order);

        // re-direct is required to change URL in address bar
        return "redirect:/order/" + order.getId() + "/edit";
    }

    // here we are doing DELETE without confirmation,
    // so it happens on GET, rather than POST
    //
    @GetMapping("/{id}/deletePizza/{n}")
    public String pizzaDelete(Model model, @PathVariable("id") int orderId, @PathVariable("n") int n) throws Exception {

        CustomerOrder order = findOrderEdit(model, orderId);
        CustomerPizza p = order.getPizzaByNumber(n);
        if (p == null)
            throw new Exception("Pizza # not found");

        //TODO: Step 5 - complete deletePizza() method
        // when the LAST pizza in an order is removed, delete the order
        order.getDetails().remove(p);

        if (order.getDetails().isEmpty()) {
            // Case: Last pizza removed - delete the entire order
            orderRepo.delete(order);
            return "redirect:/";
        } else {
            // Case: Still pizzas remaining - update order
            order.computePrice();
            orderRepo.save(order);

            // re-direct is required to change URL in address bar
            return "redirect:/order/" + order.getId() + "/edit";
        }
    }

    @GetMapping("/{id}/confirm")
    public String confirmGet(Model model, @PathVariable("id") int orderId) throws Exception {
        CustomerOrder order = findOrderEdit(model, orderId);
        model.addAttribute("order", order);
        modelDeliveryAndPayment(model);
        return "order/confirmation";
    }

    private void modelDeliveryAndPayment(Model model) {
        List<CustomerOrder.OrderType> oTypes = new ArrayList<>(Arrays.asList(CustomerOrder.OrderType.values()));
        oTypes.remove(CustomerOrder.OrderType.UNKNOWN);
        model.addAttribute("OrderTypes", oTypes);

        List<CustomerOrder.PaymentType> pTypes = new ArrayList<>(Arrays.asList(CustomerOrder.PaymentType.values()));
        pTypes.remove(CustomerOrder.PaymentType.UNKNOWN);
        model.addAttribute("PaymentTypes", pTypes);
    }

    @PostMapping("/{id}/confirm")
    public String confirmPost(Model model, @Valid @ModelAttribute("order") CustomerOrder order, BindingResult result) throws Exception {

        System.out.println("confirm POST, order=" + order);

        //TODO: Step 6 - add validation checks
        if (order.getTip().compareTo(BigDecimal.ZERO) < 0) {
            result.rejectValue("tip", "negative", "Tip cannot be negative");
        }

        if (order.getOrderType() == CustomerOrder.OrderType.DELIVERY &&
                order.getPaymentType() == CustomerOrder.PaymentType.CASH) {
            result.rejectValue("paymentType", "invalid", "Cannot select CASH & DELIVERY");
        }


        if (result.hasErrors()) {
            System.out.println("  not valid");
//            for (ObjectError error : result.getAllErrors())
//                System.out.println("  " + error);
            modelDeliveryAndPayment(model);
//            dumpModelAttributes(model);
            return "order/confirmation";
        }

        CustomerOrder dbOrder = findOrderEdit(model, order.getId());
        // TODO: Step 6 - update the order in the database
        dbOrder.setOrderStatus(CustomerOrder.OrderStatus.SUBMITTED);
        dbOrder.setTip(order.getTip());
        dbOrder.setOrderType(order.getOrderType());
        dbOrder.setPaymentType(order.getPaymentType());
        dbOrder.setAmountPaid(dbOrder.getGrandTotal()); // Assuming full payment

        dbOrder.computePrice();
        dbOrder.computePickupTime();

        System.out.println("submitting order = " + dbOrder);
        orderRepo.save(dbOrder);

        return "order/details";
    }
}