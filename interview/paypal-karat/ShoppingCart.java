import java.math.RoundingMode;
import java.util.concurrent.ConcurrentHashMap;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;
import java.util.Map;
import java.util.stream.Collectors;

class Item {
    private final String id;
    private final String name;
    private long priceInCents; // Store price in cents
    private Currency currency;

    public Item(String id, String name, long priceInCents, Currency currency) {
        this.id = id;
        this.name = name;
        this.currency = currency;
        setPriceInCents(priceInCents);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getPriceInCents() {
        return priceInCents;
    }

    public BigDecimal getPriceInStandardUnits() {
        return BigDecimal.valueOf(priceInCents, 2);
    }

    public void setPriceInCents(long priceInCents) {
        if (priceInCents < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        this.priceInCents = priceInCents;
    }

    public Currency getCurrency() {
        return currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(id, item.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

// Custom exception for cart operations
class CartOperationException extends Exception {
    public CartOperationException(String message) {
        super(message);
    }
}

public class ShoppingCart {
    private final ConcurrentHashMap<String, CartItem> items;
    private static final BigDecimal DISCOUNT = new BigDecimal("0.10");
    private final Currency defaultCurrency;

    private static class CartItem {
        private final Item item;
        private int quantity;

        CartItem(Item item, int quantity) {
            this.item = item;
            this.quantity = quantity;
        }
    }

    public ShoppingCart(Currency defaultCurrency) {
        this.items = new ConcurrentHashMap<>();
        this.defaultCurrency = defaultCurrency;
    }

    public void addItem(Item item, int quantity) throws CartOperationException {
        if (item == null) {
            throw new CartOperationException("Cannot add null item to cart");
        }
        if (quantity <= 0) {
            throw new CartOperationException("Quantity must be positive");
        }
        if (!item.getCurrency().equals(defaultCurrency)) {
            throw new CartOperationException("Item currency doesn't match cart currency");
        }

        items.compute(item.getId(), (key, existingItem) -> {
            if (existingItem == null) {
                return new CartItem(item, quantity);
            } else {
                existingItem.quantity += quantity;
                return existingItem;
            }
        });
    }

    public void removeItem(String itemId, int quantity) throws CartOperationException {
        if (itemId == null) {
            throw new CartOperationException("Item ID cannot be null");
        }
        if (quantity <= 0) {
            throw new CartOperationException("Quantity must be positive");
        }

        items.computeIfPresent(itemId, (key, existingItem) -> {
            existingItem.quantity -= quantity;
            return existingItem.quantity > 0 ? existingItem : null;
        });

        if (!items.containsKey(itemId)) {
            throw new CartOperationException("Item not found in cart: " + itemId);
        }
    }

    public Map<Item, Integer> getItems() {
        return items.values().stream()
                .collect(Collectors.toMap(
                        cartItem -> cartItem.item,
                        cartItem -> cartItem.quantity,
                        (existing, replacement) -> existing,
                        java.util.LinkedHashMap::new
                ));
    }

    public BigDecimal calculateTotal() {
        long totalCents = items.values().stream()
                .mapToLong(cartItem -> cartItem.item.getPriceInCents() * cartItem.quantity)
                .sum();

        BigDecimal total = BigDecimal.valueOf(totalCents, 2);
        return applyDiscount(total);
    }

    private BigDecimal applyDiscount(BigDecimal total) {
        return total.subtract(total.multiply(DISCOUNT))
                .setScale(2, RoundingMode.HALF_UP);
    }

    public static void main(String[] args) {
        try {
            Currency usd = Currency.getInstance("USD");
            ShoppingCart cart = new ShoppingCart(usd);

            // Create items with prices in cents
            Item book = new Item("1", "Book", 2999, usd);  // $29.99
            Item pen = new Item("2", "Pen", 199, usd);     // $1.99

            // Add items with quantities
            cart.addItem(book, 2);  // 2 books
            cart.addItem(pen, 3);   // 3 pens

            System.out.printf("Cart total with discount: %s %s%n",
                    cart.calculateTotal().toString(),
                    cart.defaultCurrency.getSymbol());

            // Print cart contents
            cart.getItems().forEach((item, quantity) ->
                    System.out.printf("%s x%d: %s %s each%n",
                            item.getName(),
                            quantity,
                            item.getPriceInStandardUnits().toString(),
                            item.getCurrency().getSymbol())
            );

        } catch (CartOperationException e) {
            System.err.println("Cart operation failed: " + e.getMessage());
        }
    }
}