# Coupon Microservice

To run the application, you need to install DBs.

<pre>
docker-compose -f up -d
</pre>

Access the mongodb cli and initiates set with default settings.

<pre>
rs.initiate()
</pre>

Connect the mysql and create a schema.

<pre>
create schema `coupon`;
</pre>

If the commands are executed in order, the application will run.

### MongoDB

* Main database that stores data of coupon microservice.
* Coupon policy can be flexibly registered as an advantage of schemaless.
* It is easy to manage a large amount of issuance data.

### Redis

* Quantity verification can be performed when a large number of coupon issuance requests are made with a huge
  throughput.

### Kafka

* It is used as a data pipeline. Coupon domains can issue messages and take them from other domains, and can process
  data by consuming topics from other domains.
* When a large number of coupons are issued, a message is issued to Kafka for stable processing.

### MySQL

* Required to utilize meta tables in Spring Batch.

## Coupon Type

### Event Coupon

> Issued through events during a specific period. A lot of traffic is generated in a short time.

### Marketing Coupon

> The operator determines coupon code and issues it through that code. Similar to event coupons, A lot of traffic is generated in a short time.

### Mission Coupon

> Issued when a specific action occurs in another domain. It communicates with the mission code. Traffic will vary by domain, but usually moderate traffic is continuous.

### Roulette Coupon

> This is a type of event coupon. Spin the roulette and receive various coupons depending on the probability. Operators create events by specifying coupons and odds. Moderate traffic is generated continuously.

### Sale Coupon

> The operator generates coupon codes of random and issues them the codes. Traffic comes in when coupons are sold, but it's not explosive.

### TimeAttack Coupon

> This is a type of event coupon, and it is opened at specific times. An explosion of traffic is generated in an instant.

### Voucher Coupon

> Can only be used on certain products. There are public coupons exposed on the product page and secret coupons offered to lucky people. Low traffic is generated continuously.