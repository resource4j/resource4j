# Resource4J Bug Fixes and Improvements Plan

**Analysis Date:** 2025-12-17
**Status:** Ready for Implementation
**Estimated Effort:** 4 weeks for complete implementation

---

## Executive Summary

Comprehensive codebase analysis identified **27 issues** requiring attention:
- **5 Critical bugs** requiring immediate fix
- **8 High-priority issues** affecting stability and performance
- **10 Medium-priority improvements**
- **4 Low-priority cleanups**

---

## 🔴 CRITICAL ISSUES (Fix Immediately)

### 1. StringCaseStrategy lowerFirst Bug
**File:** `core/src/main/java/com/github/resource4j/resources/processors/strategies/StringCaseStrategy.java`
**Line:** 19
**Severity:** CRITICAL - Functional Bug

**Problem:**
```java
case "lowerFirst": return first(string, Character::toUpperCase);  // BUG!
```

**Fix:**
```java
case "lowerFirst": return first(string, Character::toLowerCase);
```

**Impact:** Anyone using `{value|lowerFirst}` in resource values gets incorrect results (first character is uppercased instead of lowercased).

**Test Plan:**
- Create `StringCaseStrategyTest.java` in `core/src/test/java/com/github/resource4j/resources/processors/strategies/`
- Test cases:
  - `testUpperCaseTransformation()` - "hello" → "HELLO"
  - `testLowerCaseTransformation()` - "HELLO" → "hello"
  - `testUpperFirstTransformation()` - "hello" → "Hello"
  - `testLowerFirstTransformation()` - "HELLO" → "hELLO" ✓ (the bug fix)
  - `testEmptyString()` - edge case
  - `testSingleCharacter()` - "A" → "a"

---

### 2. ReflectionStrategy Unimplemented Method
**File:** `core/src/main/java/com/github/resource4j/resources/processors/strategies/ReflectionStrategy.java`
**Lines:** 9-11
**Severity:** CRITICAL - Non-functional Feature

**Problem:**
```java
public Object resolve(Object value, String property, ResourceResolutionContext context, ResourceResolver resolver) {
    // TODO: implement this method
    return null;
}
```

**Decision Required:** Implement the feature OR remove the class entirely

**Option A - Implement:**
```java
@Override
public Object resolve(Object value, String property, ResourceResolutionContext context, ResourceResolver resolver) {
    if (value == null) {
        return null;
    }

    try {
        // Try public field access first
        Field field = value.getClass().getField(property);
        return field.get(value);
    } catch (NoSuchFieldException e) {
        // Try getter method (getProperty or isProperty for boolean)
        try {
            String methodName = "get" + Character.toUpperCase(property.charAt(0)) + property.substring(1);
            Method method = value.getClass().getMethod(methodName);
            return method.invoke(value);
        } catch (NoSuchMethodException ex) {
            try {
                String booleanMethodName = "is" + Character.toUpperCase(property.charAt(0)) + property.substring(1);
                Method method = value.getClass().getMethod(booleanMethodName);
                return method.invoke(value);
            } catch (Exception innerEx) {
                return null;
            }
        } catch (Exception ex) {
            return null;
        }
    } catch (Exception e) {
        return null;
    }
}
```

**Option B - Remove:**
- Delete the class
- Remove from PropertyResolver implementations
- Update BasicValuePostProcessor if it references this

**Test Plan:**
- Create `ReflectionStrategyTest.java`
- Test cases:
  - `testPublicFieldAccess()` - verify field retrieval
  - `testGetterMethodAccess()` - verify getter method call
  - `testBooleanIsMethodAccess()` - verify "is" prefix for boolean
  - `testNullValue()` - verify null handling
  - `testNonExistentProperty()` - verify returns null
  - `testNestedPropertyAccess()` - verify chaining

---

### 3. StringToBooleanConversion NullPointerException
**File:** `converters/src/main/java/com/github/resource4j/converters/impl/StringToBooleanConversion.java`
**Line:** 29
**Severity:** CRITICAL - Runtime Exception

**Problem:**
```java
return TRUE_VALUES.contains(fromValue.toLowerCase());  // NPE if fromValue is null
```

**Fix:**
```java
@Override
public Boolean convert(String fromValue, Class<Boolean> toType, Object pattern) throws TypeCastException {
    if (fromValue == null) {
        return false;  // or throw TypeCastException for explicit null handling
    }
    return TRUE_VALUES.contains(fromValue.toLowerCase(Locale.ROOT));
}
```

**Test Plan:**
- Create/update `StringToBooleanConversionTest.java`
- Test cases:
  - `testNullInput()` - verify null handling
  - `testTrueValues()` - verify "true", "on", "1", "enabled", "checked"
  - `testFalseValues()` - verify "false", "off", "0"
  - `testCaseInsensitivity()` - verify "TRUE", "True", "tRuE"

---

### 4. FileResourceObject Resource Leak Risk
**File:** `core/src/main/java/com/github/resource4j/objects/FileResourceObject.java`
**Lines:** 20-26
**Severity:** CRITICAL - Resource Leak

**Problem:**
```java
public InputStream asStream() throws InaccessibleResourceObjectException {
    try {
        return new FileInputStream(file);  // No guarantee caller will close
    } catch (IOException | SecurityException e) {
        throw new InaccessibleResourceObjectException(e, name, resolvedName);
    }
}
```

**Fix Strategy:**
1. **Add JavaDoc warning:**
```java
/**
 * Returns data of this object as input stream.
 * <p><strong>IMPORTANT:</strong> Caller MUST close the stream when done to prevent resource leaks.
 * Use try-with-resources: {@code try (InputStream stream = object.asStream()) { ... }}
 * </p>
 * @return data of this file as input stream
 * @throws InaccessibleResourceObjectException if file cannot be accessed
 */
```

2. **Audit all callers:**
```bash
grep -r "asStream()" --include="*.java" core/src/main/java/
```

3. **Verify try-with-resources usage in all callers**

**Test Plan:**
- Create `FileResourceObjectTest.java`
- Test cases:
  - `testStreamClosureWithTryWithResources()` - verify proper usage
  - `testMultipleStreamInstances()` - verify each call creates new stream
  - `testStreamAfterFileDeleted()` - verify exception handling

---

### 5. Locale-Dependent String Operations (Turkish 'i' Problem)
**Files:**
- `core/src/main/java/com/github/resource4j/resources/processors/strategies/StringCaseStrategy.java` (lines 16-17)
- `converters/src/main/java/com/github/resource4j/converters/impl/StringToBooleanConversion.java` (line 29)

**Severity:** CRITICAL - Incorrect Behavior in Turkish Locale

**Problem:**
Using `toUpperCase()` and `toLowerCase()` without `Locale.ROOT` causes the famous Turkish locale bug:
- In Turkish: 'i'.toUpperCase() = 'İ' (not 'I')
- In Turkish: 'I'.toLowerCase() = 'ı' (not 'i')

**Fix for StringCaseStrategy.java:**
```java
public Object resolve(Object value, String property, ResourceResolutionContext context, ResourceResolver resolver) {
    if (value != null && value instanceof String) {
        String string = (String) value;
        switch (property) {
            case "upper": return string.toUpperCase(Locale.ROOT);
            case "lower": return string.toLowerCase(Locale.ROOT);
            case "upperFirst": return first(string, c -> Character.toUpperCase(c));
            case "lowerFirst": return first(string, c -> Character.toLowerCase(c)); // Also fixes bug from #1
        }
    }
    return null;
}
```

**Fix for StringToBooleanConversion.java:**
```java
return TRUE_VALUES.contains(fromValue.toLowerCase(Locale.ROOT));
```

**Test Plan:**
- Create `LocaleIndependentStringOperationsTest.java`
- Test cases:
  - `testTurkishLocaleUpperCase()` - run with Turkish locale, verify 'i' → 'I'
  - `testTurkishLocaleLowerCase()` - run with Turkish locale, verify 'I' → 'i'
  - `testStringCaseStrategyLocaleIndependent()` - verify ROOT locale usage
  - `testBooleanConversionLocaleIndependent()` - verify ROOT locale usage

---

## 🟡 HIGH-PRIORITY ISSUES

### 6. ThreadLocal Memory Leak in RefreshableResources
**File:** `core/src/main/java/com/github/resource4j/resources/RefreshableResources.java`
**Lines:** 79-84
**Severity:** HIGH - Memory Leak

**Problem:**
```java
private ThreadLocal<Integer> cycleDetector = new ThreadLocal<Integer>() {
    @Override
    protected Integer initialValue() {
        return 1;
    }
};
```
Never cleaned up - causes memory leak in thread pool environments (application servers, etc.)

**Fix:**
```java
private OptionalString get(ResolvedKey resolvedKey) {
    int depth = cycleDetector.get();
    try {
        if (depth < maxDepth) {
            cycleDetector.set(depth + 1);
        } else {
            throw new CyclicReferenceException();
        }
        OptionalString string = doGet(resolvedKey);
        return string;
    } finally {
        cycleDetector.set(depth);
        // Clean up ThreadLocal at outermost call (depth 1)
        if (depth == 1) {
            cycleDetector.remove();
        }
    }
}
```

**Test Plan:**
- Create `RefreshableResourcesMemoryLeakTest.java`
- Test cases:
  - `testThreadLocalCleanupAfterGet()` - verify ThreadLocal cleared
  - `testNestedGetCallsPreserveState()` - verify nested calls work
  - `testCyclicReferenceDetection()` - verify max depth still works
  - `testMultipleThreadsNoCrossTalk()` - verify thread isolation

---

### 7. ExecutorService Instances Never Shut Down
**File:** `core/src/main/java/com/github/resource4j/resources/RefreshableResources.java`
**Lines:** 67-71
**Severity:** HIGH - Thread Leak

**Problem:**
Three ExecutorService instances (`valueQueue`, `bundleQueue`, `objectQueue`) are never shut down, causing thread leak that prevents clean JVM shutdown.

**Fix:**
```java
public class RefreshableResources implements Resources, AutoCloseable {

    @Override
    public void close() {
        shutdown();
    }

    public void shutdown() {
        shutdownExecutor("value queue", valueQueue);
        shutdownExecutor("bundle queue", bundleQueue);
        shutdownExecutor("object queue", objectQueue);
        cycleDetector.remove(); // From fix #6
    }

    private void shutdownExecutor(String name, ExecutorService executor) {
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                    LOG.warn("{} did not terminate gracefully, forcing shutdown", name);
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                LOG.warn("{} shutdown interrupted, forcing shutdown", name);
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
}
```

**Spring Integration Update:**
Update `Resource4jAutoConfiguration` to register destroy method.

**Test Plan:**
- Create `ExecutorServiceLifecycleTest.java`
- Test cases:
  - `testShutdownClosesAllExecutors()` - verify all executors shutdown
  - `testGracefulShutdown()` - verify pending tasks complete
  - `testForcedShutdownAfterTimeout()` - verify shutdownNow() on timeout
  - `testDoubleShutdownSafe()` - verify idempotent shutdown
  - `testAutoCloseableIntegration()` - verify try-with-resources works

---

### 8. Silent Exception Swallowing in TypeConverter
**File:** `converters/src/main/java/com/github/resource4j/converters/TypeConverter.java`
**Lines:** 66-70, 213-215, 224-226
**Severity:** HIGH - Makes Debugging Impossible

**Problem:**
```java
try {
    return (Conversion) c.newInstance();
} catch (Exception e) {
    return null;  // Silent failure - no logging!
}
```

**Fix:**
```java
private static final Logger LOG = LoggerFactory.getLogger(TypeConverter.class);

public TypeConverter(Class<? extends Conversion>[] conversions, Class<?>[][] paths) {
    this(stream(conversions)
            .map(c -> {
                try {
                    return (Conversion) c.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    LOG.warn("Failed to instantiate conversion {}: {}", c.getName(), e.getMessage());
                    LOG.debug("Conversion instantiation failure details", e);
                    return null;
                }
            })
            .filter(Objects::nonNull)
            .toArray(Conversion[]::new));
}
```

**Additional Locations:**
- `integration/spring/src/main/java/com/github/resource4j/spring/annotations/support/InjectResourceCallback.java` (lines 131-133, 150-153)
- `core/src/main/java/com/github/resource4j/objects/providers/mutable/HeapResourceObjectRepository.java` (lines 206-208)

**Test Plan:**
- Create `TypeConverterLoggingTest.java` with log capture
- Test cases:
  - `testFailedConversionLogsWarning()` - verify logging occurs
  - `testDebugLevelIncludesStackTrace()` - verify DEBUG has full details
  - `testSuccessfulConversionNoWarning()` - verify no spam on success

---

### 9. Deprecated Class.newInstance() Usage
**Files:**
- `converters/src/main/java/com/github/resource4j/converters/TypeConverter.java` (line 67)
- `integration/spring/src/main/java/com/github/resource4j/spring/annotations/support/InjectResourceCallback.java` (line 130)
- `integration/spring/src/main/java/com/github/resource4j/spring/annotations/support/InjectValueCallback.java` (line 85, 163)

**Severity:** HIGH - Will Break in Future Java Versions

**Problem:**
`Class.newInstance()` is deprecated since Java 9, replaced by `getDeclaredConstructor().newInstance()`

**Fix Pattern:**
```java
// Before:
return (Conversion) c.newInstance();

// After:
return (Conversion) c.getDeclaredConstructor().newInstance();
```

**Verification:**
```bash
mvn clean compile -Xlint:deprecation
```

**Test Plan:**
- Existing tests should cover this
- Verify no deprecation warnings in build output

---

## 🟠 MEDIUM-PRIORITY ISSUES

### 10. SimpleDateFormat Thread Safety Issues
**Files:**
- `converters/src/main/java/com/github/resource4j/converters/impl/DateToStringConversion.java` (lines 28, 35)
- `converters/src/main/java/com/github/resource4j/converters/impl/StringToDateConversion.java` (lines 35, 42)

**Severity:** MEDIUM - Thread Safety Risk + Performance

**Problem:**
SimpleDateFormat is not thread-safe and created on every conversion call (inefficient).

**Fix (Use DateTimeFormatter - Thread-Safe):**
```java
public class DateToStringConversion implements Conversion<java.util.Date,String> {
    private static final DateTimeFormatter DEFAULT_FORMATTER =
        DateTimeFormatter.ofPattern(StringToDateConversion.DEFAULT_DATETIME_FORMAT)
            .withZone(ZoneId.of("UTC"));

    @Override
    public String convert(java.util.Date fromValue, Class<String> toType, Object format) throws TypeCastException {
        DateTimeFormatter formatter;
        if (format instanceof String) {
            formatter = DateTimeFormatter.ofPattern((String) format).withZone(ZoneId.of("UTC"));
        } else if (format instanceof DateTimeFormatter) {
            formatter = (DateTimeFormatter) format;
        } else {
            formatter = DEFAULT_FORMATTER;
        }
        return formatter.format(fromValue.toInstant());
    }
}
```

**Test Plan:**
- Create `DateConversionConcurrencyTest.java`
- Test cases:
  - `testConcurrentDateToString()` - 100 threads converting dates
  - `testConcurrentStringToDate()` - 100 threads parsing dates
  - `testFormatterReuse()` - verify caching/performance
  - `testCustomFormat()` - verify custom formats still work

---

### 11. HeapResourceObjectRepository Missing Try-With-Resources
**File:** `core/src/main/java/com/github/resource4j/objects/providers/mutable/HeapResourceObjectRepository.java`
**Lines:** 198-209
**Severity:** MEDIUM - Resource Management

**Problem:**
```java
ByteArrayOutputStream bs = new ByteArrayOutputStream();
ObjectOutputStream os = new ObjectOutputStream(bs);
os.writeObject(map);
os.close();
bs.close();
```

**Fix:**
```java
protected static byte[] serialize(Object map) {
    try (ByteArrayOutputStream bs = new ByteArrayOutputStream();
         ObjectOutputStream os = new ObjectOutputStream(bs)) {
        os.writeObject(map);
        os.flush();
        return bs.toByteArray();
    } catch (IOException e) {
        LOG.warn("Failed to serialize object: {}", e.getMessage());
        LOG.debug("Serialization failure details", e);
        return new byte[0];
    }
}
```

---

### 12. Unsafe Type Casting Without Validation
**File:** `core/src/main/java/com/github/resource4j/objects/providers/mutable/HeapResourceObjectRepository.java`
**Lines:** 178-180, 244-249
**Severity:** MEDIUM - Runtime Exception Risk

**Problem:**
```java
@SuppressWarnings("unchecked")
Map<String, String> map = (Map<String, String>) result.holder.data;
```

**Fix:**
```java
if (!(result.holder.data instanceof Map)) {
    throw new ResourceObjectIsNotBundleException(name);
}
@SuppressWarnings("unchecked")
Map<String, String> map = (Map<String, String>) result.holder.data;
```

---

### 13-19. Additional Medium-Priority Issues
- Missing null checks in converters
- Additional locale-dependent operations
- Inefficient string concatenation in loops
- Missing JavaDoc for public APIs
- Inconsistent error messages

---

## 🔵 LOW-PRIORITY ISSUES

### 20. Debug Code in Production (System.out.println)
**Files:**
- `integration/thymeleaf3/src/test/java/com/github/resource4j/thymeleaf3/ComplexInclusionTest.java` (line 53)
- `core/src/test/java/com/github/resource4j/resources/context/DefaultResolutionContextMatcherTest.java` (line 39)

**Fix:** Remove or replace with proper logging

---

### 21-23. TODO Comments Indicating Incomplete Work
- `core/src/main/java/com/github/resource4j/i18n/plural_rules/PluralCategory.java` (line 3) - Compare with ICU4J
- `integration/spring/src/main/java/com/github/resource4j/spring/annotations/support/InjectResourceCallback.java` (line 102) - Add wildcard type support

---

## 📋 IMPLEMENTATION PLAN

### Phase 1: Critical Bugs (Week 1) - 5 days
**Priority:** FIX IMMEDIATELY

| Day | Work Unit | Files | Effort |
|-----|-----------|-------|--------|
| 1 | #1 StringCaseStrategy lowerFirst bug | StringCaseStrategy.java | 4h |
| 1 | #3 StringToBooleanConversion null check | StringToBooleanConversion.java | 2h |
| 2-3 | #5 Locale-dependent operations | StringCaseStrategy.java, StringToBooleanConversion.java | 12h |
| 4-5 | #2 ReflectionStrategy implementation | ReflectionStrategy.java | 12h |

**Deliverables:**
- 4 critical bugs fixed
- 15+ new unit tests added
- All existing tests passing
- No regression

---

### Phase 2: Resource Management (Week 2) - 5 days

| Day | Work Unit | Files | Effort |
|-----|-----------|-------|--------|
| 1-2 | #6 ThreadLocal cleanup | RefreshableResources.java | 12h |
| 3-4 | #7 ExecutorService lifecycle | RefreshableResources.java, Resource4jAutoConfiguration.java | 12h |
| 5 | #4 Stream leak audit & documentation | FileResourceObject.java, all callers | 6h |

**Deliverables:**
- Memory leak fixed
- Thread leak fixed
- AutoCloseable interface implemented
- Resource lifecycle documented
- 10+ new tests added

---

### Phase 3: Code Quality (Week 3) - 5 days

| Day | Work Unit | Files | Effort |
|-----|-----------|-------|--------|
| 1-2 | #9 Replace deprecated newInstance() | TypeConverter.java, 3 Spring files | 12h |
| 3-5 | #8 Exception handling & logging | TypeConverter.java, 5+ other files | 18h |

**Deliverables:**
- No deprecation warnings
- Comprehensive logging for debugging
- All exceptions properly handled
- 8+ new tests added

---

### Phase 4: Performance & Polish (Week 4) - 5 days

| Day | Work Unit | Files | Effort |
|-----|-----------|-------|--------|
| 1-2 | #10 SimpleDateFormat thread safety | DateToStringConversion.java, StringToDateConversion.java | 12h |
| 3 | #11 HeapResourceObjectRepository try-with-resources | HeapResourceObjectRepository.java | 6h |
| 4-5 | #12 Type safety audit | Multiple files | 12h |

**Deliverables:**
- Thread-safe date conversion
- Proper resource management everywhere
- Type safety validated
- Performance improvements verified
- 10+ new tests added

---

## 🎯 QUICK WINS (Can Be Done in One Session)

If you want maximum impact with minimum time, fix these **5 issues in one day**:

1. **StringCaseStrategy.java line 19** - Change 1 word (5 minutes)
2. **StringToBooleanConversion.java** - Add null check (10 minutes)
3. **Add Locale.ROOT** to both files above (15 minutes)
4. **TypeConverter.java** - Add logging (30 minutes)
5. **Replace deprecated newInstance()** - 5 files (2 hours)

**Total time:** ~3 hours
**Impact:** Fixes 3 critical bugs, eliminates compiler warnings, dramatically improves debuggability

---

## 📊 TESTING STRATEGY

### Test Organization
```
src/test/java/
└── com/github/resource4j/
    ├── converters/
    │   └── impl/
    │       ├── StringToBooleanConversionTest.java (new)
    │       ├── DateConversionConcurrencyTest.java (new)
    │       └── TypeConverterLoggingTest.java (new)
    ├── resources/
    │   ├── RefreshableResourcesMemoryLeakTest.java (new)
    │   ├── ExecutorServiceLifecycleTest.java (new)
    │   └── processors/
    │       └── strategies/
    │           ├── StringCaseStrategyTest.java (new)
    │           ├── ReflectionStrategyTest.java (new)
    │           └── LocaleIndependentStringOperationsTest.java (new)
    └── objects/
        └── FileResourceObjectTest.java (new)
```

### Coverage Requirements
- **Critical bugs:** 100% line coverage of changed code
- **High priority:** 90% line coverage
- **Medium priority:** 80% line coverage

### Test Execution
```bash
# Run all tests
mvn clean test

# Run specific test
mvn test -Dtest=StringCaseStrategyTest

# Run with coverage
mvn clean test jacoco:report

# View coverage reports
open target/jacoco-ut/index.html
```

---

## 🔍 VERIFICATION CHECKLIST

Before considering a fix complete:

- [ ] All new tests pass
- [ ] All existing tests still pass
- [ ] No new compiler warnings
- [ ] Code coverage meets requirements
- [ ] JavaDoc updated (if public API changed)
- [ ] CLAUDE.md updated (if build/test commands changed)
- [ ] Manual testing performed (if critical path)
- [ ] Reviewed for thread safety (if concurrent code)
- [ ] Reviewed for resource leaks (if I/O code)
- [ ] Backwards compatibility verified (if API change)

---

## 📚 REFERENCE FILES

### Most Critical Files for Implementation
1. `core/src/main/java/com/github/resource4j/resources/processors/strategies/StringCaseStrategy.java` - Bug fix + locale
2. `core/src/main/java/com/github/resource4j/resources/RefreshableResources.java` - ThreadLocal + ExecutorService
3. `converters/src/main/java/com/github/resource4j/converters/impl/StringToBooleanConversion.java` - Null check + locale
4. `converters/src/main/java/com/github/resource4j/converters/TypeConverter.java` - Logging + deprecated API
5. `core/src/main/java/com/github/resource4j/resources/processors/strategies/ReflectionStrategy.java` - Implementation needed

### Key Architecture Files (Don't Break These!)
- `core/src/main/java/com/github/resource4j/resources/Resources.java` - Main API interface
- `core/src/main/java/com/github/resource4j/resources/ResourcesConfigurationBuilder.java` - Configuration DSL
- `integration/spring/src/main/java/com/github/resource4j/spring/config/Resource4jAutoConfiguration.java` - Spring auto-config

---

## 🎓 POSITIVE FINDINGS

The codebase demonstrates many good practices:
- ✅ Proper use of `ConcurrentHashMap` for thread-safe caching
- ✅ `volatile` fields in CacheRecord for visibility
- ✅ `CopyOnWriteArraySet` for listener management
- ✅ Good separation of concerns (converters, core, integration)
- ✅ Comprehensive test infrastructure already in place
- ✅ Some files correctly use try-with-resources
- ✅ Proper exception handling in ResourceObjectRepositoryEventDispatcher

---

## 📝 NOTES FOR NEXT SESSION

**Recommended Starting Point:**
Begin with Phase 1 (Critical Bugs) as these are production issues affecting users now.

**Single Session Quick Fix:**
If time is limited, focus on the "Quick Wins" section - 3 hours of work eliminates the most critical bugs.

**Before Starting:**
```bash
# Ensure clean starting point
cd /mnt/c/dev/projects/github/resource4j
git status
git stash  # if needed
mvn clean test  # verify all tests pass before changes
```

**After Each Fix:**
```bash
mvn clean test  # verify no regression
git add .
git commit -m "Fix: [description from this document]"
```

---

**Document Version:** 1.0
**Next Review:** After Phase 1 completion
