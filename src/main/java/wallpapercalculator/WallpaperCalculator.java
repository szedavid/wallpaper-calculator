package wallpapercalculator;

public class WallpaperCalculator {

    /**
     * @param args array elements:
     *             1.: input file path (required)
     *             2.: calculate with extra amount (optional, default: 'true')
     *             3.: unit of measure (optional, default: 'feet')
     *             4.: delimeter (optional, default: 'x')
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please provide the path to the .txt file as an argument.");
            return;
        }

        var calculator = Calculator.getInstance(args[0], args.length > 1 ? Boolean.parseBoolean(args[2]) : null,
                args.length > 2 ? args[3] : null, args.length > 3 ? args[1] : null);

        calculator.printResults();
    }

}
