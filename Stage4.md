## Stage 4: Optimize Performance and Caching
### Observability
Đầu tiên ta sẽ tiến hành đo thời gian các xử lý của chúng ta với database, từ đó ta mới tìm giải pháp
để rút ngắn thời gian đó.

Đầu tiên ta add các dependence vào pom
```
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-actuator</artifactId>
	<version>3.0.6</version>
</dependency>
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-aop</artifactId>
	<version>3.0.6</version>
</dependency>
```

Tiếp theo ta cần thêm các cấu hình vào `application.properties`
```
management.endpoints.web.exposure.include = *
management.endpoint.health.show-details=always
```
Ta tạo một package `aspect` để chứa các file xử lý. package này ta có thể chỉ
s dụng tại máy cá nhân (local) để tiến hành tự test tốc độ của ứng dụng, không
cần commit lên nhánh dev, và tất nhiên trên nhánh production không nên có. Chính
vì vậy mà trong best structure practice thường không đề cập đến nó.

Đầu tiên ta tạo `PerformanceTrackerHandler` implements `ObservationHandler<Observation.Context>`

Sau đó ta tiến hành @Override tất cả các phương thức mà framwork hỗ trợ.

```
package com.dev.studyspringboot.aspect;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public class PerformanceTrackerHandler implements ObservationHandler<Observation.Context> {

    @Override
    public void onStart(Observation.Context context) {
        log.info("execution started {}", context.getName());
        context.put("time", System.currentTimeMillis());
    }

    @Override
    public void onError(Observation.Context context) {
        log.info("Error occurred {}", Objects.requireNonNull(context.getError()).getMessage());
    }

    @Override
    public void onEvent(Observation.Event event, Observation.Context context) {
        ObservationHandler.super.onEvent(event, context);
    }

    @Override
    public void onScopeOpened(Observation.Context context) {
        ObservationHandler.super.onScopeOpened(context);
    }

    @Override
    public void onScopeClosed(Observation.Context context) {
        ObservationHandler.super.onScopeClosed(context);
    }

    @Override
    public void onScopeReset(Observation.Context context) {
        ObservationHandler.super.onScopeReset(context);
    }

    @Override
    public void onStop(Observation.Context context) {
        log.info("execution stopped "
                + context.getName()
                + " duration "
                + (System.currentTimeMillis() - context.getOrDefault("time", 0L))
        );
    }

    @Override
    public boolean supportsContext(Observation.Context context) {
        return true;
    }
}
```

Trong phương thức `supportsContext` ta return true để hỗ trợ context

Sau đó ta tiến hành sửa các method như `onStart`, `onError`, `onStop` để ứng dụng
log ra khi chúng ta chạy service để đo thời gian. Ta cần phải có annotation
`@Slf4j` để xử dụng hàm log.

Tương tự như `Security`, trong package `aspect` ta cần tạo một class `ObservationAspectConfig`
để xử lý.
```
package com.dev.studyspringboot.aspect;

import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.aop.ObservedAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObservationAspectConfig {

    @Bean
    public ObservedAspect observedAspect(ObservationRegistry observationRegistry) {
        observationRegistry.observationConfig()
                .observationHandler(new PerformanceTrackerHandler());
        return new ObservedAspect(observationRegistry);
    }
}
```

Đến đây thì mọi thứ đã hoàn tất, nếu muốn đo time của methos nào trong service
ta chỉ cần thêm annotation @Observed trước method đó. 

Ví dụ mình muốn đo thời gian của hai method `getAllProduct` và `getOneProduct`
trong `ProductServiceImpl`
```
@Observed(name = "get.products")
@Override
public List<Product> getAllProduct() {
    return productRepository.findAllByDeletedAtIsNull();

@Observed(name = "get.product")
@Override
public Product getOneProduct(Long productId) {
    Product product = productRepository.findByIdAndDeletedAtIsNull(productId);
    if (product != null) {
        return productRepository.findByIdAndDeletedAtIsNull(productId);
    } else throw new ResourceNotFoundException("Product has id: " + productId + " NOT exist!");
}
```

Ta sẽ thêm name để khi log trên console ta biết đó là log của method nào

Ta sẽ test với Postman và đây là thông tin hiện trong console
```
c.d.s.aspect.PerformanceTrackerHandler   : execution started get.products
c.d.s.aspect.PerformanceTrackerHandler   : execution stopped spring.security.http.secured.requests duration 165
```