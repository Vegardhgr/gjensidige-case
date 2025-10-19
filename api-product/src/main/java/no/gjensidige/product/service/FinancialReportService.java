package no.gjensidige.product.service;

import no.gjensidige.product.repository.ProductRepository;
import no.gjensidige.product.model.FinancialReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import no.gjensidige.product.entity.Product;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.sql.Timestamp;

@Service
public class FinancialReportService {

    @Autowired
    ProductRepository productRepository;

    private BigDecimal getProductRevenue(Product p) {
        return new BigDecimal(p.getNumberSold()).multiply(BigDecimal.valueOf(p.getUnitPrice()));
    }

    private BigDecimal getRevenue(List<Product> products) {
        BigDecimal revenue = products.stream()
            .map(p->getProductRevenue(p))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        return revenue;
    }

    private BigDecimal getProductCost(Product p) {
        return new BigDecimal(p.getNumberSold()).multiply(BigDecimal.valueOf(p.getUnitCost()));
    }

    private BigDecimal getTotCost(List<Product> products) {
        BigDecimal cost = products.stream()
            .map(p->getProductCost(p))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        return cost;
    }

    private Product getMostSoldProduct(List<Product> products) {
        return products.stream()
            .max(Comparator.comparing(Product::getNumberSold))
            .orElse(null);
    }

    private Product getLeastSoldProduct(List<Product> products) {
        return products.stream()
            .min(Comparator.comparing(Product::getNumberSold))
            .orElse(null);
    }

    private Product getHighestMargin(List<Product> products) {
        return products.stream()
            .max(Comparator.comparingDouble(p-> getProductRevenue(p).doubleValue() - getProductCost(p).doubleValue()))
            .orElse(null);
    }

    private Product getLeastMargin(List<Product> products) {
        return products.stream()
            .min(Comparator.comparingDouble(p-> getProductRevenue(p).doubleValue() - getProductCost(p).doubleValue()))
            .orElse(null);
    }


    public FinancialReport genFinancialReport() {
        List<Product> products = productRepository.findAll();

        FinancialReport financialReport = new FinancialReport();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Double revenue = this.getRevenue(products).doubleValue();
        Double cost = this.getTotCost(products).doubleValue();
        Double margin = revenue - cost;


        financialReport.setTotalTurnover(revenue);
        financialReport.setTotalCost(cost);
        financialReport.setTotalMargin(margin);
        financialReport.setMostSoldProduct(this.getMostSoldProduct(products));
        financialReport.setLeastSoldProduct(this.getLeastSoldProduct(products));
        financialReport.setHighestMarginProduct(this.getHighestMargin(products));
        financialReport.setLowestMarginProduct(this.getLeastMargin(products));
        financialReport.setTimestamp(timestamp);

        return financialReport;
    }
}