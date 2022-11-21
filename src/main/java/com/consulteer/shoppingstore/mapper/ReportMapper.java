package com.consulteer.shoppingstore.mapper;

import com.consulteer.shoppingstore.dtos.interfaces.ProductReport;
import com.consulteer.shoppingstore.dtos.CityDto;
import com.consulteer.shoppingstore.dtos.ReportDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReportMapper {
    public ReportDto convert(List<CityDto> cityDtos, ProductReport productReport) {
        return new ReportDto(productReport.getProductName(),
                productReport.getProductPrice(),
                cityDtos,
                calculateTotalIncome(cityDtos)
        );
    }

    private Double calculateTotalIncome(List<CityDto> cityDtos) {
        return cityDtos.stream().map(CityDto::income).toList().stream().reduce(0.0, Double::sum);
    }
}
