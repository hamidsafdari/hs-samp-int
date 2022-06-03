#### testing with
```shell
siege -c 10 -r 10 http://localhost:8080/simple/request1/name2
```

### Synchronous Code
```lang=kotlin
// service
@ServiceActivator(inputChannel = "nameRequestChannel")
fun getRandomName(@Payload name: String): String {
  Thread.sleep(random.nextInt(200).toLong())
  return "${System.currentTimeMillis()}| request: $name | reply: ${names.get(random.nextInt(3))}"
}
```

#### direct channel

```text
Transactions:                    100 hits
Availability:                 100.00 %
Elapsed time:                   3.01 secs
Data transferred:               0.00 MB
Response time:                  0.27 secs
Transaction rate:              33.22 trans/sec
Throughput:                     0.00 MB/sec
Concurrency:                    8.88
Successful transactions:         100
Failed transactions:               0
Longest transaction:            0.52
Shortest transaction:           0.03
```

#### pub-sub executor pool size 4

```text
Transactions:                    100 hits
Availability:                 100.00 %
Elapsed time:                   2.94 secs
Data transferred:               0.00 MB
Response time:                  0.24 secs
Transaction rate:              34.01 trans/sec
Throughput:                     0.00 MB/sec
Concurrency:                    8.04
Successful transactions:         100
Failed transactions:               0
Longest transaction:            0.58
Shortest transaction:           0.00
```

#### pub-sub executor pool size 8

```text
Transactions:                    100 hits
Availability:                 100.00 %
Elapsed time:                   2.74 secs
Data transferred:               0.00 MB
Response time:                  0.25 secs
Transaction rate:              36.50 trans/sec
Throughput:                     0.00 MB/sec
Concurrency:                    9.14
Successful transactions:         100
Failed transactions:               0
Longest transaction:            0.52
Shortest transaction:           0.01
```

### Changing Responses to Mono
```kotlin
// controller
fun request1(@PathVariable name: String): Mono<String> {
  return simpleGateway.getName(name)
}

// service
@ServiceActivator(inputChannel = "nameRequestChannel")
fun getRandomName(@Payload name: String): String {
  Thread.sleep(random.nextInt(200).toLong())
  return "${System.currentTimeMillis()}| request: $name | reply: ${names.get(random.nextInt(3))}"
}

// gateway
@Gateway(requestChannel = "nameRequestChannel", replyTimeout = 3000)
fun getName(name: String): Mono<String>
```

#### direct channel

```text
Transactions:                    100 hits
Availability:                 100.00 %
Elapsed time:                   2.76 secs
Data transferred:               0.00 MB
Response time:                  0.25 secs
Transaction rate:              36.23 trans/sec
Throughput:                     0.00 MB/sec
Concurrency:                    9.10
Successful transactions:         100
Failed transactions:               0
Longest transaction:            0.52
Shortest transaction:           0.05
```

#### pub-sub executor pool size 4

```text
Transactions:                    100 hits
Availability:                 100.00 %
Elapsed time:                   2.53 secs
Data transferred:               0.00 MB
Response time:                  0.24 secs
Transaction rate:              39.53 trans/sec
Throughput:                     0.00 MB/sec
Concurrency:                    9.31
Successful transactions:         100
Failed transactions:               0
Longest transaction:            0.46
Shortest transaction:           0.01
```

#### pub-sub executor pool size 8

```text
Transactions:                    100 hits
Availability:                 100.00 %
Elapsed time:                   1.53 secs
Data transferred:               0.00 MB
Response time:                  0.12 secs
Transaction rate:              65.36 trans/sec
Throughput:                     0.00 MB/sec
Concurrency:                    8.07
Successful transactions:         100
Failed transactions:               0
Longest transaction:            0.25
Shortest transaction:           0.01
```

#### pub-sub executor pool size 16

```text
Transactions:                    100 hits
Availability:                 100.00 %
Elapsed time:                   1.39 secs
Data transferred:               0.00 MB
Response time:                  0.12 secs
Transaction rate:              71.94 trans/sec
Throughput:                     0.00 MB/sec
Concurrency:                    8.29
Successful transactions:         100
Failed transactions:               0
Longest transaction:            0.21
Shortest transaction:           0.00
```
