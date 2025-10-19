package no.gjensidige.product.service;

import no.gjensidige.product.entity.Product;
import no.gjensidige.product.model.FinancialReport;
import no.gjensidige.product.repository.ProductRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

/**
 * Tests for FinancialReportService (Exercise 2)
 */
public class FinancialReportServiceTest {

    @InjectMocks
    private FinancialReportService financialReportService;

    @Mock
    private ProductRepository productRepository;

    private List<Product> products;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        products = new ArrayList<>();

        products.add(buildProduct("Superforsikring", 100.0, 50.0, 100));          // margin per unit 50 -> total 5000
        products.add(buildProduct("Superrisiko", 150.0, 95.0, 100));              // margin per unit 55 -> total 5500
        products.add(buildProduct("Superforsikring+", 100.0, 25.0, 1000));        // margin per unit 75 -> total 75000
        products.add(buildProduct("Helse1", 100.0, 75.0, 5000));                  // margin per unit 25 -> total 125000
        products.add(buildProduct("Helse++", 50.0, 50.0, 1000));                  // margin per unit 0 -> total 0
    }

    private Product buildProduct(String name, Double unitPrice, Double unitCost, int numberSold) {
        Product p = new Product();
        p.setProductName(name);
        p.setUnitPrice(unitPrice);
        p.setUnitCost(unitCost);
        p.setNumberSold(BigInteger.valueOf(numberSold));
        return p;
    }

    @Test
    public void generateReport_allFieldsComputed() {
        when(productRepository.findAll()).thenReturn(products);

        FinancialReport report = financialReportService.genFinancialReport();

        // Verify only one call to db. Reduce db load
        verify(productRepository).findAll(); 

        double expectedRevenue = 100*100 + 100*150 + 1000*100 + 5000*100 + 1000*50; 
        assertEquals(expectedRevenue, report.getTotalTurnover(), 0.0001);

        double expectedCost = 100*50 + 100*95 + 1000*25 + 5000*75 + 1000*50; 
        assertEquals(expectedCost, report.getTotalCost(), 0.0001);

        assertEquals(expectedRevenue - expectedCost, report.getTotalMargin(), 0.0001);

        assertEquals("Helse1", report.getMostSoldProduct().getProductName());

        assertEquals("Superforsikring", report.getLeastSoldProduct().getProductName()); 

        assertEquals("Helse1", report.getHighestMarginProduct().getProductName());

        assertEquals("Helse++", report.getLowestMarginProduct().getProductName());
        assertNotNull(report.getTimestamp());
    }

    @Test
    public void generateReport_emptyProducts() {
        when(productRepository.findAll()).thenReturn(new ArrayList<>());

        FinancialReport report = financialReportService.genFinancialReport();
        assertEquals(Double.valueOf(0.0), report.getTotalTurnover());
        assertEquals(Double.valueOf(0.0), report.getTotalCost());
        assertEquals(Double.valueOf(0.0), report.getTotalMargin());
        assertNull(report.getMostSoldProduct());
        assertNull(report.getLeastSoldProduct());
        assertNull(report.getHighestMarginProduct());
        assertNull(report.getLowestMarginProduct());
        assertNotNull(report.getTimestamp());
    }
}
