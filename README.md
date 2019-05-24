# John Lewis Technical Test

This is a Spring Boot (2.1.4) based webapp, written in Java 8 and built with maven 3

## Building

`$ mvn clean install`

This will run the tests and build the .war file in the `target` directory

## Running

`$ java -jar target/jl-techtest-1.0.0.jar`

This will start the application on the default port of 8080

## Executing

The web app only exposes one page:

[http://localhost:8080/product/discount](http://localhost:8080/product/discount)

One of three labelType parameters can be used:

[http://localhost:8080/product/discount?labelType=ShowWasNow](http://localhost:8080/product/discount?labelType=ShowWasNow)

[http://localhost:8080/product/discount?labelType=ShowWasThenNow](http://localhost:8080/product/discount?labelType=ShowWasThenNow)

[http://localhost:8080/product/discount?labelType=ShowPercDscount](http://localhost:8080/product/discount?labelType=ShowPercDscount)


as per the specification

## Assumptions from the specification

### Percentage discounts
I assumed these rounded down, so e.g. 19.9999% discount is displayed as 19%

### Currencies
The currency is listed in the price JSON, but always seems to be GBP. I've put some multi currency code in there to EUR prices will
display with a € symbol, but this is all I've tested it with. I've done no currency conversion when sorting by discount, so a €101
discount is greater than a £100 discount as 101 > 100, even though its (probably) is a smaller discount due to the
exhange rate.

### Colours
The source of the RGB colo(u)r values was not specified but I've assumed they are from the w3c standard web colour list and provided
a lookup table in a config file accordingly.

### skuid /skuId
The sku uses the field name of skuId on the api, but the spec says to use skuid to produce the JSON output. I assumed this is
a typo so I have used skuId on the output. If this is not the case, then its an easy fix.

### Strange value in JSON for now price
I've seen an instance of the 'now' field in the price to be a JSON node containing a 'from' and a 'to' field rather than the 
expected String. I've just ignored that and left the 'now' as empty rather than try to work out if this has some other use.

## Comments about the code
### Testing
I've included some unit tests, but due to time constraints, not as many as I would like. I would have liked to add more testing
particularly around the parsing of valid / invalid JSON. I would also have liked to add some functional tests that test the 
ProductFetchController / ProductPageFetcher / ProductFetcher working together.

### Configuration
I've pulled some config out into the configuration.properties file embedded in the JAR for things like the endpoint and API key.
I've also added configuration for the ThreadPoolTaskExecutor that allows the Product pages to be downloaded in parallel. I've
chosen some values that work, but given more time these could be tweeked to allow the Product pages to download faster.

### Error handling
If one of the Product page download fails, then this page will be ignored and the rest of the downloads will continue. There is 
no retry stratergy, and the user is not alerted in any way (other than the console log)

### Caching
There is no request caching - each request is sent directly to the API. Even with paralellising the Product Page downloads it 
can still be slow to aggregate the entire response. I've not had chance ot put any caching in. 
