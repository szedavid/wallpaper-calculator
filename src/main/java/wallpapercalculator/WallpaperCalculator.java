package wallpapercalculator;

public class WallpaperCalculator {

    /**
     * @param args array elements:
     *             1.: input file path (required)
     *             2.: delimeter (optional, default: 'x')
     *             3.: calculate with extra amount (optional, default: 'true')
     *             4.: unit of measure (optional, default: 'feet')
     */
    public static void main(String[] args) {
        // the first argument is required and is used as the input file path
        if (args.length == 0) {
            System.out.println("Please provide the path to the .txt file as an argument.");
            return;
        }

        var calculator = Calculator.getInstance(args[0], args.length > 1 ? args[1] : null, args.length > 2 ? Boolean.parseBoolean(args[2]) : null,
                args.length > 3 ? args[3] : null);

        calculator.printResults();
    }

}
