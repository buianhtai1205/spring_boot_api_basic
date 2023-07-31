package com.dev.studyspringboot;

import com.dev.studyspringboot.model.Brand;
import com.dev.studyspringboot.repository.BrandRepository;
import com.dev.studyspringboot.service.IBrandService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ContextConfiguration(classes = BrandServiceTest.BrandServiceTestConfiguration.class)
public class BrandServiceTest {

    @MockBean
    BrandRepository brandRepository;
    @Autowired
    private IBrandService brandService;

    @TestConfiguration
    public static class BrandServiceTestConfiguration {
        @Bean
        @Primary
        public IBrandService mockService() {
            return Mockito.mock(IBrandService.class);
        }
    }

    @BeforeEach
    public void setUp() {
        // Mock the `findAll()` method on the `BrandRepository` to return a list of brands.
        Mockito.when(brandRepository.findAllByDeletedAtIsNull())
                .thenReturn(IntStream.range(0, 2)
                        .mapToObj(i -> Brand.builder().name("Brand " + i).build())
                        .collect(Collectors.toList())
                );
    }

    @Test
    public void getAllBrands() {

        // Call the `getAllBrands()` method on the `BrandService`.
        List<Brand> brands = brandService.getAllBrand();

        // Assert that the list of brands contains the expected brands.
        assertEquals(2, brands.size());
        assertEquals("Brand 0", brands.get(0).getName());
        assertEquals("Brand 1", brands.get(1).getName());
    }
}
