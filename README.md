# tax-calculator
Austria vat taxes calculator

This application runs on spring embedded tomcat.

Endpoint for testing: GET localhost:8080/purchases/calculate
Example body (json payload):

{
    "netAmount" : 20,
    "grossAmount" : 0,
    "vatAmount" : 0,
    "vatRate" : 13
}

or simply


{
    "netAmount" : 20,
    "vatRate" : 13
}
