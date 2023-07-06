# wallpaper-calculator

The application can calculate the amount of required wallpaper to cover multiple rooms, among other data.

My goal was to create an application that goes beyond the minimum requirements outlined in the assignment description,
as this is often advantageous in real-world scenarios.

With this in mind, I made the following enhancements:

- Added support for using command-line arguments with default values:
  1.: Input file path (required)
  2.: Calculate with extra amount (optional, default: true)
  3.: Unit of measure (optional, default: "feet")
  4.: Delimiter (optional, default: "x")

- Used "double" instead of "integer" for more accurate calculations, although integer would suffice for the sample
  data.
- Made calculating extra amount optional (default is "true" to match the assignment description)
- Made unit of length dynamic (default is "feet" to match the assignment description)
- Made delimiter used to read dimension data from data source dynamic (default is "x" to match the received
  sample-inuput.txt)

- Created JUnit tests

You can generate wallpaper-calculator-1.0.jar using Gradle.
The first cli argument is required to set the path to the datasource file.
Example: wallpaper-calculator-1.0.jar sample-input.txt
