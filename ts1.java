import io.alpaca.AlpacaApi;
import io.alpaca.AlpacaApiException;
import io.alpaca.model.Order;
import io.alpaca.model.Position;

public class TradingAlgorithm {
    public static void main(String[] args) {
        AlpacaApi api = new AlpacaApi("YOUR_API_KEY", "YOUR_API_SECRET", "https://paper-api.alpaca.markets");

        String symbol = "SPY";
        boolean posHeld = false;

        while (true) {
            System.out.println("");
            System.out.println("Checking Price");

            try {
                Order order = api.getOrder(symbol, 5);
                double ma = order.getBars().stream().mapToDouble(bar -> bar.getClose()).average().orElse(0);
                double lastPrice = order.getBars().get(4).getClose();

                System.out.println("Moving Average: " + ma);
                System.out.println("Last Price: " + lastPrice);

                if (ma + 0.1 < lastPrice && !posHeld) {
                    System.out.println("Buy");
                    api.submitOrder(symbol, 1, Order.Side.BUY, Order.Type.MARKET, Order.TimeInForce.GTC);
                    posHeld = true;
                } else if (ma - 0.1 > lastPrice && posHeld) {
                    System.out.println("Sell");
                    api.submitOrder(symbol, 1, Order.Side.SELL, Order.Type.MARKET, Order.TimeInForce.GTC);
                    posHeld = false;
                }
            } catch (AlpacaApiException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}