package no.gjensidige.product.controller;

import no.gjensidige.product.model.FinancialReport;
import no.gjensidige.product.service.FinancialReportService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 *  RestController for the new report endpoint in exercise 2
 *
 */


@RestController
@RequestMapping(name = "Report", value = "reports")
public class ReportController {

    @Autowired
    FinancialReportService financialReportService;


    /**
     * Todo Create implementation for Financial report
     * as stated in exercise 2.
     * 
     * @return
     */
    @GetMapping(value = "/financial")
    public FinancialReport getFinancialReport(){
        financialReportService.genFinancialReport();
        return financialReportService.genFinancialReport();
    }

}
