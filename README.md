This program performs some validation on data, calculates number of chargeable days, and charges taking discount into account.

An executable jar CF.jar is located in the output directory.
For testing, run this command from the output directory in terminal:
##### java -jar CF.jar code=LADW,numDays=200,discount=10,checkoutDate=10/07/2024

Note: It is a single argument as shown with no spaces.

The names of the input params are provided in the sample above.

discount - is optional. If not provided, a value of 0 is assumed.

