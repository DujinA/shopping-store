package com.consulteer.shoppingstore.mapper;

import com.consulteer.shoppingstore.dtos.interfaces.CityReport;
import com.consulteer.shoppingstore.dtos.CityDto;
import org.springframework.stereotype.Component;

@Component
public class CityMapper {
    public CityDto convert(CityReport cityReport, Double price) {
        return new CityDto(cityReport.getCityName(),
                cityReport.getSoldQuantity(),
                getTotalIncome(cityReport.getSoldQuantity(), price)
                );
    }

    private Double getTotalIncome(Integer sold, Double price) {
        return sold * price;
    }
}
