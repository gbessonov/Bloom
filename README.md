# Bloom Filter Java Library

A lightweight and extensible Bloom filter implementation in Java, designed with simplicity and correctness in mind.

## Features

- Basic Bloom filter using 32-bit `hashCode()`
- False positive probability (FPP) estimation
- Approximate element count
- Bit set utilization metric
- Serialization to/from byte arrays or streams
- Modular structure for future extension (custom hash functions, better filters)

## Getting Started

### Usage

```java
BloomFilter<String> filter = new BasicBloomFilter<>();

filter.put("hello");
filter.put("world");

boolean mightContain = filter.mightContain("hello"); // true
boolean probablyNot = filter.mightContain("java");   // possibly false

System.out.println("Utilization: " + filter.utilization());
System.out.println("FPP Estimate: " + filter.expectedFpp());
```

### Serialization

```java
// Save to byte array
byte[] data = filter.toBytes();

// Load from byte array
BloomFilter<?> deserialized = BasicBloomFilter.fromBytes(data);
```